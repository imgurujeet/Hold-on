# ⚡ HoldOn – Anti-Theft Charging Protection App
![Logo](https://raw.githubusercontent.com/imgurujeet/Hold-on/refs/heads/main/app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp)

> In train, cafés, airport, or crowded places, your phone is at risk. Hold On acts as your phone’s security guard — if someone unplugs it, a loud alarm goes off instantly to deter theft

>  **Your phone stays safe, even while charging in public.**  
HoldOn is a smart Android app that protects your phone from theft when charging outside. If someone unplugs it, a **loud alarm** starts — and only *you* can stop it with fingerprint or lock-screen authentication.


---

## 📱 Screenshots

<img src="app/src/main/res/Assets/ss1.png" width="240"/> <img src="app/src/main/res/Assets/ss2.png" width="240"/> <img src="app/src/main/res/Assets/ss3.png" width="240"/>


---

## ✨ Key Features

 **Anti-Theft Protection** – Detects charger unplug, triggers loud alarm.  
 **Biometric Security** – Fingerprint/Face unlock to stop alarm.  
 **Device Lock Fallback** – PIN, Pattern, or Password authentication.  
 **Custom Alarm Sound** – Uses your own `alarm_sound.mp3` from `res/raw/`.
 **Foreground Service** – Keeps app alive with notification protection.  
 **Modern UI** – Built with Jetpack Compose.

---

## 📂 Project Structure

```plaintext
com.silentchaos.holdon
├── appNavigation/        → Navigation (NavGraph, Route)
│   ├── NavGraph.kt
│   └── Route.kt
│
├── receiver/             → BroadcastReceivers for events
│   ├── AlarmReceiver.kt
│   └── ChargingReceiver.kt
│
├── service/              → Foreground service for alarm
│   └── AlarmService.kt
│
├── ui/                   → UI screens and components
│   ├── Components/
│   │   ├── CustomDropDown.kt
│   │   ├── SocialCard.kt
│   │   └── TopBar.kt
│   │
│   ├── theme/
│   │   ├── HomeScreen.kt
│   │   ├── InfoScreen.kt
│   │   └── SettingScreen.kt
│
├── utils/                → Helpers & utilities
│   ├── Auth.kt
│   ├── ChargingHelper.kt
│   └── SharedPreferences.kt
│
└── MainActivity.kt       → App entry point

```


## License

[MIT](LICENSE)


## Contributing

Want to help? Please check out the notes in CONTRIBUTE.md first.

## FAQ

#### Q1: Does HoldOn work when the app is closed?
Yes, HoldOn runs as a **foreground service**, so it keeps monitoring even if you close the app.

#### Q2: Can I stop the alarm without fingerprint/lock authentication?
No, the alarm can only be stopped via **biometric or lock-screen authentication** for maximum security.

#### Q3: Will the app drain my battery?
No, HoldOn is lightweight and only monitors the charging state, so it uses minimal battery.

#### Q4: Can I customize the alarm sound?
Yes, you can set your own alarm sound from `res/raw/` or future settings updates.  
