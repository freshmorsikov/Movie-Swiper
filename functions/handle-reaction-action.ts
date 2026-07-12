import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { createClient } from "jsr:@supabase/supabase-js@2";

type ReactionAction = "Liked" | "Disliked";

type HandleReactionActionRequest = {
  userId: string;
  movieId: number;
  action: ReactionAction;
};

type UserRow = {
  id: string;
  room: string;
};

type MatchedRow = {
  id: string;
};

Deno.serve(async (req) => {
  try {
    const supabase = createClient(
      Deno.env.get("SUPABASE_URL") ?? "",
      Deno.env.get("SUPABASE_ANON_KEY") ?? "",
      { global: { headers: { Authorization: req.headers.get("Authorization") ?? "" } } },
    );

    const { userId, movieId, action } = await req.json() as HandleReactionActionRequest;
    validateRequest(userId, movieId, action);

    await createReaction(supabase, userId, movieId, action);

    const pairedUser = await getPairedUser(supabase, userId);
    if (pairedUser == null) {
      return successResponse();
    }

    switch (action) {
      case "Liked": {
        const lastReactionLiked = await isLastReactionLiked(supabase, pairedUser.id, movieId);
        if (lastReactionLiked) {
          await activateMatched(supabase, pairedUser.room, movieId);
        }
        break;
      }

      case "Disliked":
        await updateMatchedActive(supabase, pairedUser.room, movieId, false);
        break;
    }

    return successResponse();
  } catch (err) {
    const message = err instanceof Error ? err.message : String(err);

    return new Response(JSON.stringify({ message }), {
      headers: { "Content-Type": "application/json" },
      status: 500,
    });
  }
});

function validateRequest(
  userId: string,
  movieId: number,
  action: ReactionAction,
) {
  if (typeof userId !== "string" || userId.length === 0) {
    throw new Error("Missing userId");
  }
  if (typeof movieId !== "number" || !Number.isFinite(movieId)) {
    throw new Error("Invalid movieId");
  }
  if (action !== "Liked" && action !== "Disliked") {
    throw new Error("Invalid reaction action");
  }
}

async function createReaction(
  supabase: ReturnType<typeof createClient>,
  userId: string,
  movieId: number,
  action: ReactionAction,
) {
  const { error } = await supabase
    .from("reaction")
    .insert({
      user: userId,
      movie: movieId,
      action,
    });

  if (error) {
    throw error;
  }
}

async function getPairedUser(
  supabase: ReturnType<typeof createClient>,
  userId: string,
): Promise<UserRow | null> {
  const { data: currentUser, error: currentUserError } = await supabase
    .from("user")
    .select("room")
    .eq("id", userId)
    .single();

  if (currentUserError) {
    throw currentUserError;
  }

  const { data: pairedUser, error: pairedUserError } = await supabase
    .from("user")
    .select("id, room")
    .eq("room", currentUser.room)
    .neq("id", userId)
    .limit(1)
    .maybeSingle<UserRow>();

  if (pairedUserError) {
    throw pairedUserError;
  }

  return pairedUser;
}

async function isLastReactionLiked(
  supabase: ReturnType<typeof createClient>,
  userId: string,
  movieId: number,
): Promise<boolean> {
  const { data: reaction, error } = await supabase
    .from("reaction")
    .select("action")
    .eq("user", userId)
    .eq("movie", movieId)
    .order("created_at", { ascending: false })
    .limit(1)
    .maybeSingle<{ action: ReactionAction }>();

  if (error) {
    throw error;
  }

  return reaction?.action === "Liked";
}

async function activateMatched(
  supabase: ReturnType<typeof createClient>,
  roomId: string,
  movieId: number,
) {
  const matched = await getMatched(supabase, roomId, movieId);
  if (matched == null) {
    await createMatched(supabase, roomId, movieId);
  } else {
    await updateMatchedActive(supabase, roomId, movieId, true);
  }
}

async function getMatched(
  supabase: ReturnType<typeof createClient>,
  roomId: string,
  movieId: number,
): Promise<MatchedRow | null> {
  const { data: matched, error } = await supabase
    .from("matched")
    .select("id")
    .eq("room", roomId)
    .eq("movie", movieId)
    .limit(1)
    .maybeSingle<MatchedRow>();

  if (error) {
    throw error;
  }

  return matched;
}

async function createMatched(
  supabase: ReturnType<typeof createClient>,
  roomId: string,
  movieId: number,
) {
  const { error } = await supabase
    .from("matched")
    .insert({
      room: roomId,
      movie: movieId,
      active: true,
    });

  if (error) {
    throw error;
  }
}

async function updateMatchedActive(
  supabase: ReturnType<typeof createClient>,
  roomId: string,
  movieId: number,
  active: boolean,
) {
  const { error } = await supabase
    .from("matched")
    .update({ active })
    .eq("room", roomId)
    .eq("movie", movieId);

  if (error) {
    throw error;
  }
}

function successResponse(): Response {
  return new Response(JSON.stringify({ success: true }), {
    headers: { "Content-Type": "application/json" },
    status: 200,
  });
}
