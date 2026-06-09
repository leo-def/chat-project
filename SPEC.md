# Chat Project - Technical Specification

> Android chat application with Firebase Realtime Database and Google authentication.
> Demonstrates Android development with real-time messaging and social login.

## Executive Summary

Chat Project is a **native Android application** built with Java/Kotlin using **Firebase Realtime Database** for real-time messaging and **Firebase Authentication + Google Sign-In**. It implements a multi-screen chat flow: login → dashboard (conversation list) → conversation view → new conversation creation.

---

## 1. Problem Statement

### Context
A real-time mobile chat application demonstrating Android development patterns, Firebase integration, and Google OAuth authentication.

### Goals
- Implement secure user authentication via Google Sign-In + Firebase Auth
- Provide real-time bidirectional messaging via Firebase Realtime Database
- List conversations and messages in a clean Android UI
- Demonstrate RecyclerView patterns with custom row layouts

### Success Metrics
- [x] Google Sign-In + Firebase Auth integration
- [x] Firebase Realtime Database for messages
- [x] 4 Activities (login, dashboard, conversation, new conversation)
- [x] Custom RecyclerView row layouts
- [ ] Push notifications (FCM)
- [ ] Message read receipts
- [ ] Offline support

---

## 2. Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Platform | Android | Java/Kotlin |
| Build | Gradle (Android) | Latest |
| Authentication | Firebase Auth + Google Sign-In | Latest |
| Database | Firebase Realtime Database | Latest |
| UI | Android Views + RecyclerView | Latest |
| Image Loading | (Firebase profile pictures) | - |

---

## 3. Architecture

```
┌──────────────────────────────────────────────────┐
│              Android Application                  │
├──────────────────────────────────────────────────┤
│  MainActivity          (Login screen)             │
│  DashboardActivity     (Conversation list)        │
│  ConversationActivity  (Chat view)                │
│  NewConversationActivity (Start new chat)         │
└──────────────────────┬───────────────────────────┘
                       │ Firebase SDK
           ┌───────────┴───────────┐
           ▼                       ▼
┌──────────────────┐    ┌──────────────────────┐
│ Firebase Auth    │    │ Firebase Realtime DB │
│ Google Sign-In   │    │ /conversations       │
└──────────────────┘    │ /messages            │
                        │ /users               │
                        └──────────────────────┘
```

---

## 4. Screen / Activity Structure

| Activity | Layout | Purpose |
|----------|--------|---------|
| `MainActivity` | `activity_main.xml` | Login with Google Sign-In button |
| `DashboardActivity` | `activity_dashboard.xml` | List of conversations (`conversation_row.xml`) |
| `ConversationActivity` | `activity_conversation.xml` | Real-time message view (`message_row.xml`) |
| `NewConversationActivity` | `activity_new_conversation.xml` | Select user to start chat (`person_row.xml`) |

---

## 5. Data Structure (Firebase Realtime Database)

```json
{
  "users": {
    "uid1": { "name": "...", "email": "...", "photoUrl": "..." }
  },
  "conversations": {
    "convId1": {
      "participants": { "uid1": true, "uid2": true },
      "lastMessage": "...",
      "lastMessageAt": 1234567890
    }
  },
  "messages": {
    "convId1": {
      "msgId1": {
        "senderId": "uid1",
        "text": "Hello",
        "timestamp": 1234567890
      }
    }
  }
}
```

---

## 6. Authentication Flow

```
App launch
    ↓
Check FirebaseAuth.getCurrentUser()
    ↓ null
Show Google Sign-In button
    ↓ user signs in
GoogleSignInAccount → Firebase credential
    ↓
FirebaseAuth.signInWithCredential()
    ↓ success
Navigate to DashboardActivity
```

---

## 7. Testing Strategy

No automated tests found. Manual testing only.

```bash
./gradlew connectedAndroidTest    # Instrumented tests (requires device/emulator)
./gradlew test                    # Unit tests
```

---

## 8. Deployment & Operations

```bash
./gradlew assembleDebug       # Build debug APK
./gradlew assembleRelease     # Build release APK (requires signing config)
```

**Requirements:**
- `google-services.json` in `app/` (Firebase project config — not committed)
- Firebase project with Realtime Database and Auth enabled
- Google Sign-In OAuth client configured in Firebase Console

---

## 9. Issues Found

### Security
- Firebase Realtime Database security rules must be configured. Default rules allow read/write to all authenticated users — conversation isolation (only participants can read/write) requires custom rules.
- No input sanitization on message text — XSS-style payloads could be stored (Firebase stores raw strings).

### Missing Features
- No FCM push notifications — users must have the app open to receive messages.
- No message deletion or editing.
- No typing indicators.
- No read receipts.
- No pagination for message history (loading all messages at once will fail for long conversations).

### Code Quality
- `google-services.json` should be in `.gitignore` — if accidentally committed, Firebase API keys are exposed.
- No error handling displayed to users for failed sign-in or network errors.
