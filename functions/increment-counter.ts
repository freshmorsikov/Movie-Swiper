import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { createClient } from "jsr:@supabase/supabase-js@2";

Deno.serve(async (req) => {
  try {
    const supabase = createClient(
        Deno.env.get('SUPABASE_URL') ?? '',
        Deno.env.get('SUPABASE_ANON_KEY') ?? '',
        { global: { headers: { Authorization: req.headers.get('Authorization')! } } }
    );

    const { data: counter, error: fetchError } = await supabase
      .from('counter')
      .select('value')
      .eq('id', 1)
      .single();

    if (fetchError) {
      throw fetchError;
    }

    const nextValue = (counter?.value ?? 0) + 1;

    const { data: counterValue, error: updateError } = await supabase
      .from('counter')
      .update({ value: nextValue })
      .eq('id', 1)
      .select('value')
      .single();

    if (updateError) {
      throw updateError;
    }

    return new Response(JSON.stringify({ value: counterValue.value }), {
      headers: { "Content-Type": "application/json" },
      status: 200,
    });
  } catch (err) {
    const message = err instanceof Error ? err.message : String(err);

    return new Response(JSON.stringify({ message }), {
      headers: { "Content-Type": "application/json" },
      status: 500,
    });
  }
});
