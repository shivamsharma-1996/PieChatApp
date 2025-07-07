💬 **PieChatApp**

PieChatApp is an Android chat application that supports real-time messaging via sockets along with offline message queuing functionality.

---

📱 Download APK

Click below to install the app on your Android device:

🔗 [Download APK](https://github.com/shivamsharma-1996/PieChatApp/raw/main/apk/pie-chat-app.apk)

---

🎥 Project Demo

Watch the demo video for a walkthrough of the project and its features:

▶️ [View Demo Video](https://drive.google.com/file/d/132D9aZTMvDWpxxNtKeGnaR_g9GFZmGru/view)

---
## ✨ Features

💬 Real-Time Messaging: Socket-based communication using [PieHost-android-client](https://piehost.com/docs/3.0/android-websocket)

🔄 Offline Functionality: Message queuing and auto-retry when back online

📊 Chat Previews: Display latest message previews for each conversation

🔔 Unread Messages: Visual indicators for unread message count

🚫 Error Handling: Comprehensive error states and network failure alerts

📵 Empty States: Proper handling for no conversations and no internet scenarios

🔄 Auto-Retry: Intelligent message retry mechanism

---

## 🖼️ Screenshots

<div align="center">
  <table>
    <tr>
      <td><img src="screenshots/empty-conversations-ui.jpeg" width="200" height="450" alt="" /></td>
      <td><img src="screenshots/socket-connection-cofirmation.jpeg" width="200" height="450" alt="" /></td>
      <td><img src="screenshots/unread-messages.jpeg" width="200" height="450" alt="" /></td>
      <td><img src="screenshots/no-internet-indicator.jpeg" width="200" height="450" alt="" /></td>
    </tr>
    <tr>
      <td><img src="screenshots/chat-Inbox.jpeg" width="200" height="450" alt="" /></td>
      <td><img src="screenshots/simulated-message-queuing-using-toggle.jpeg" width="200" height="450" alt="" /></td>
       <td><img src="screenshots/offline-message-queuing-when-no-internet.jpeg" width="200" height="450" alt="" /></td>
      <td><img src="screenshots/device-back-online-indicator.jpeg" width="200" height="450" alt="" /></td>
    </tr>
  </table>
</div>

---

## 📌 Tasks & Pull Requests

| Task Name                              | Pull Request Title                                                                 |
|----------------------------------------|-------------------------------------------------------------------------------------|
| `initial-setup`                        | [Feat] [Initial project setup and base structure](https://github.com/shivamsharma-1996/PieChatApp/pull/1) |
| `feat/navigation-architecture`         | [Feat] [Navigation Architecture](https://github.com/shivamsharma-1996/PieChatApp/pull/2) |
| `feat/implement-p0-domain-layer`       | [Feat] [Implement domain layer for conversations](https://github.com/shivamsharma-1996/PieChatApp/pull/3) |
| `feat/integrate-piesocket-sdk`         | [Feat] [Integrate PieSocket SDK](https://github.com/shivamsharma-1996/PieChatApp/pull/4) |
| `feat/conversation-logic`             | [Feat] [Implement Conversation logic to observe all conversations](https://github.com/shivamsharma-1996/PieChatApp/pull/5) |
| `feat/chat-ui-logic`                   | [Feat] [Implement chat UI and message handling logic](https://github.com/shivamsharma-1996/PieChatApp/pull/6) |
| `refactor/chat-ui`                     | [Refactor] [Restructure data classes and refactor Chat UI handling](https://github.com/shivamsharma-1996/PieChatApp/pull/7) |
| `feat/offline-queue`                   | [Feat] [Offline Message Queue Management System](https://github.com/shivamsharma-1996/PieChatApp/pull/8) |
| `feat/ui-alert-handling`               | [Feat] [Add alert handling for UI state changes](https://github.com/shivamsharma-1996/PieChatApp/pull/9) |
| `feat/queue-mode-simulation-toggle`    | [Feat] [Implement Queue Mode Simulation Toggle](https://github.com/shivamsharma-1996/PieChatApp/pull/10) |
| `feat/mark-as-read`                    | [Feat] [Implement unread count logic & mark conversation read on navigation to Chat Inbox](https://github.com/shivamsharma-1996/PieChatApp/pull/11) |


---

## 🔧 Tech Stack & Dependencies

- **Language:** Kotlin
- **Architecture:** MVVM + Clean Architecture + SOLID Principles
- **UI:** Jetpack Compose, Material Design Components
- **Real-Time Communication:** Socket.IO
- **Async Operations:** Kotlin Coroutines + Flow
- **DI:** Dagger-hilt
- **Other Libraries:** Navigation-compose
---


## Setup

 Signup on PieHost [dashboard](https://piehost.com/). Create a free PieSocket project which will provide a websocket URL. Both Android and web client will connect to the same websocket URL with same room-id. 

 ---
## 📱 Usage
**Online Mode**
1. Connect both the Android client and [PieHost Web client](https://piehost.com/websocket-tester) to the same websocket URL - `wss://s14881.blr1.piesocket.com/v3/chat-shivam?api_key=XuGHlUqVmqBDlHxSP9Sn5hXKGCD6DohwYe62IxE9&user=some_user_name`. Here, "chat-shivam" is the room-id that the Android client uses to establish the socket connection.
2. Initiate the conversation from the Web client. You can find the valid **payload structure** at the end of the documentation below.
3. The conversations page displays all available conversations with latest received message as a preview.
4. Tap on any chat to view/send messages
5. Messages are exchanged in real-time over the socket connection.

**Offline Mode**
1. When network is unavailable, messages are queued locally
2. Visual indicators show pending messages
3. Once network is restored, queued messages are automatically sent
4. Chat remains functional with local message history
---

## Payload Structure for PieHost Web client
```json
{
  "event": "new-message",
  "data": {
    "sender_name": "shivam-web",
    "message": "Hi!"
  }
}
