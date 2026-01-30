import SwiftUI
import Firebase
import ComposeApp

@main
struct iOSApp: App {
    
    init() {
        FirebaseApp.configure()
        InitKt.doInitKoinIos()
        Start.shared.startApp()
    }
    
    var body: some Scene {
        
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    ExternalUriHandler.shared.onNewUri(uri: url.absoluteString)
                }
        }
    }
}
