# ⚡ HoldOn – Anti-Theft Charging Protection App  

> 🔒 **Your phone stays safe, even while charging in public.**  
HoldOn is a smart Android app that protects your phone from theft when charging outside. If someone unplugs it, a **loud alarm** starts — and only *you* can stop it with fingerprint or lock-screen authentication.  

---

## 🎥 Demo Video  
📹 *See HoldOn in action*  

[![Watch the demo](https://img.youtube.com/vi/your_demo_video_id/maxresdefault.jpg)](https://youtu.be/your_demo_video_id)  
*(Click the image to watch the video)*  

---

## 📱 Screenshots  

| Home Screen | Alarm Triggered | Authentication Prompt |  
|-------------|-----------------|------------------------|  
| ![Home](assets/screens/home.png) | ![Alarm](assets/screens/alarm.png) | ![Auth](assets/screens/auth.png) |  

---

## ✨ Key Features  

✅ **Anti-Theft Protection** – Detects charger unplug, triggers loud alarm.  
✅ **Biometric Security** – Fingerprint/Face unlock to stop alarm.  
✅ **Device Lock Fallback** – PIN, Pattern, or Password authentication.  
✅ **Custom Alarm Sound** – Uses your own `alarm_sound.mp3` from `res/raw/`.  
✅ **Foreground Service** – Keeps app alive with notification protection.  
✅ **Modern UI** – Built with Jetpack Compose.  

---

## 📂 Project Structure  
com.silentchaos.holdon
├── appNavigation/ → Navigation (NavGraph, Route)
├── receiver/ → AlarmReceiver, ChargingReceiver
├── service/ → AlarmService
├── ui/ → Home, Settings, Info screens
├── utils/ → ChargingHelper, FingerPrintHelper, PermissionUtils
└── MainActivity.kt


---

## 🚀 Getting Started  

### Prerequisites  
- Android Studio Ladybug+  
- Kotlin 1.9+  
- Android 8.0 (API 26) or above  

### Installation  

1. Clone this repository:  
   ```bash
   git clone https://github.com/yourusername/HoldOn.git
   cd HoldOn
Add your alarm sound file:

Place alarm_sound.mp3 in app/src/main/res/raw/.

Build & Run on your device 🎉

🔐 Authentication Flow

Connect your phone to a charger.

If unplugged → alarm rings loudly.

To stop → Authenticate with fingerprint/face or device credentials.

🛡️ Permissions

FOREGROUND_SERVICE – Keeps alarm running.

USE_BIOMETRIC – Fingerprint/Face unlock.

WAKE_LOCK – Ensures alarm is always active.

🤝 Contributing

Pull requests are welcome!

Fork the repo

Create a feature branch

Submit a PR 🚀

📜 License

MIT License © 2025 [Your Name]
