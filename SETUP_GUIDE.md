# FlashcardAI — Setup & Run Guide

## What You're Getting

A native Android app (Kotlin + Jetpack Compose) that uses the Anthropic Claude API to generate smart flashcards from any topic or pasted notes. Features include:

- **AI-powered card generation** — Claude creates Q&A pairs with hints
- **Flip animation** — tap cards to reveal answers
- **Self-assessment** — mark each card as "Got It" or "Didn't Know"
- **Score breakdown** — see results with missed-card review
- **Adjustable count** — generate 5–20 cards per session

---

## Prerequisites

| Tool | Version | Download |
|------|---------|----------|
| Android Studio | Hedgehog (2023.1+) | https://developer.android.com/studio |
| JDK | 17 | Bundled with Android Studio |
| Anthropic API Key | — | https://console.anthropic.com/settings/keys |

---

## Step 1 — Install Android Studio

1. Download from https://developer.android.com/studio
2. Run the installer, accept defaults
3. On first launch, let it download the Android SDK (takes ~10 min)
4. Make sure **SDK 34** is installed:
   - `Settings → Languages & Frameworks → Android SDK → SDK Platforms`
   - Check **Android 14.0 (API 34)**

---

## Step 2 — Set Up the Android Emulator

1. Open **Device Manager** (icon in the toolbar, or `Tools → Device Manager`)
2. Click **Create Device**
3. Pick **Pixel 7** (or any phone) → **Next**
4. Select a system image:
   - Tab: **Recommended**
   - Pick **UpsideDownCake (API 34)** or **Tiramisu (API 33)**
   - Click **Download** if not already installed (~1.2 GB)
5. Click **Next → Finish**
6. Hit the **▶ Play** button next to the device to boot it up

> **Performance tip:** Enable hardware acceleration:
> - Windows: Enable **Intel HAXM** or **Windows Hypervisor Platform** in BIOS
> - Settings → search "Windows Features" → enable "Windows Hypervisor Platform" → reboot

---

## Step 3 — Open the Project

1. In Android Studio: `File → Open`
2. Navigate to the `FlashcardAI` folder (the root with `build.gradle.kts`)
3. Click **OK** and wait for Gradle sync to finish (first time takes a few minutes)

---

## Step 4 — Add Your API Key

Open `local.properties` in the project root and add this line:

```properties
ANTHROPIC_API_KEY=sk-ant-api03-YOUR_KEY_HERE
```

> This file is gitignored by default. Never commit API keys to version control.

---

## Step 5 — Run the App

1. Make sure the emulator is running (or a physical device is connected via USB with developer mode on)
2. Select your device in the toolbar dropdown
3. Click the **▶ Run** button (or `Shift+F10`)
4. Wait for build + install (~30–60 seconds first time)
5. The app launches automatically on the emulator

---

## How to Use

1. **Enter a topic** — e.g. "Transformer Architecture", "React Hooks", "US Civil War"
2. **Paste notes** (optional) — lecture notes, textbook excerpts, anything relevant
3. **Adjust card count** — slider from 5 to 20
4. **Tap Generate** — Claude creates your flashcards
5. **Flip cards** — tap to reveal the answer
6. **Self-assess** — mark "Got It" or "Didn't Know"
7. **View results** — see your score and review missed cards
8. **Retry or start fresh** — loop as needed

---

## Project Structure

```
FlashcardAI/
├── app/src/main/java/com/sai/flashcardai/
│   ├── MainActivity.kt              # Entry point + screen router
│   ├── model/
│   │   └── Flashcard.kt             # Data classes
│   ├── network/
│   │   └── AnthropicClient.kt       # Claude API integration
│   ├── viewmodel/
│   │   └── FlashcardViewModel.kt    # Business logic + state
│   ├── ui/theme/
│   │   └── Theme.kt                 # Colors, typography
│   ├── ui/screens/
│   │   ├── InputScreen.kt           # Topic + notes input
│   │   ├── LoadingScreen.kt         # Animated loader
│   │   ├── QuizScreen.kt            # Card flip + self-test
│   │   └── ResultsScreen.kt         # Score + review
│   └── ui/components/
│       └── FlashcardView.kt         # Animated flip card
├── app/build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── local.properties                  # YOUR API KEY GOES HERE
```

---

## Troubleshooting

| Issue | Fix |
|-------|-----|
| Gradle sync fails | `File → Invalidate Caches → Restart` |
| Emulator won't start | Enable virtualization in BIOS; check Device Manager logs |
| API key not working | Ensure `local.properties` has `ANTHROPIC_API_KEY=sk-ant-...` with no quotes around the value |
| Network error in emulator | Emulator uses host network — make sure your PC has internet |
| Build fails on JDK | `File → Settings → Build → Gradle → Gradle JDK` → set to JDK 17 |

---

## Optional: Run on a Physical Android Device

1. Enable **Developer Options**: `Settings → About Phone → tap "Build Number" 7 times`
2. Enable **USB Debugging**: `Settings → Developer Options → USB Debugging`
3. Connect via USB cable
4. Accept the debugging prompt on the phone
5. Select the device in Android Studio and hit Run
