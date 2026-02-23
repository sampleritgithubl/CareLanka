# CareLanka - Digital Healthcare Companion 🇱🇰

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com/)

**CareLanka** is a comprehensive healthcare mobile application designed specifically for the Sri Lankan community. It bridges the gap between patients and healthcare providers while offering essential medical tools at your fingertips.

---

## 🚀 Main Features

### 1. Dual User Roles (Patient & Caregiver)
* **Patients:** Can browse doctors, hospitals, and set reminders.
* **Caregivers:** Can register to offer their services to those in need.

### 2. Pharma Express (Online Medicine)
Direct integration with Sri Lanka's leading online pharmacies and delivery services:
* **Healthguard Pharmacy**
* **Laugfs Wellness**
* **QuickMed.lk**
* **PickMe Flash** (For rapid prescription delivery)

### 3. Pill Reminder ⏰
* Never miss a dose again! Users can set custom alarms for their medications.
* Receives high-priority notifications when it's time to take medicine.

### 4. Smart Hospital Finder 🏥
* Locate the nearest **Government and Private hospitals** using Google Maps integration.
* Filter searches based on your current location.

### 5. Specialist Doctor Directory
* Find doctors based on specialties (Cardiologists, Pediatricians, etc.).
* View doctor details and their practicing hospitals.

### 6. Emergency Services
* Quick access to emergency contact numbers and ambulance services in Sri Lanka.

---

## 🛠 Tech Stack

*   **Language:** Java
*   **UI Layout:** XML (Material Design Components)
*   **Database:** Firebase Firestore (Cloud Database)
*   **Authentication:** Firebase Auth
*   **Notifications:** Android AlarmManager & Notification Manager
*   **Maps API:** Google Maps Intent API

---

## 📸 App Screenshots

### 🔐 Authentication

| Login | Register |
|-------|----------|
| <img src="screenshorts/login.jpeg" width="250"/> | <img src="screenshorts/register.jpeg" width="250"/> |

---

### 🏠 Main Features

| Dashboard | Caregiver | Online Medicine | Emergency Calls| Reminder |
|-----------|-----------|--------|-----------|----------|
| <img src="screenshorts/dashboard.jpeg" width="180"/> | <img src="screenshorts/caregiver.jpeg" width="180"/> | <img src="screenshorts/online.jpeg" width="180"/> | <img src="screenshorts/emergency.jpeg" width="180"/> | <img src="screenshorts/reminder.jpeg" width="180"/> |


## ⚙️ Installation & Setup

1.  **Clone the project:**
    ```bash
    git clone https://github.com/YourUsername/CareLanka.git
    ```
2.  **Open in Android Studio:**
    Wait for Gradle sync to complete.
3.  **Firebase Configuration:**
    *   Create a project on [Firebase Console](https://console.firebase.google.com/).
    *   Add an Android App with package name `com.example.carelanka`.
    *   Download `google-services.json` and place it in the `app/` directory.
    *   Enable **Email/Password Auth** and **Cloud Firestore**.
4.  **Run the App:**
    Connect an Android device or use an emulator (API 23 or higher).

---

## 🤝 Contributing

Contributions are welcome! If you have ideas to improve the app:
1. Fork the Project.
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`).
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the Branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## 📧 Contact

**Developer Name** - [Your Email Address]  
**Project Link:** [https://github.com/YourUsername/CareLanka](https://github.com/YourUsername/CareLanka)

---
*Developed with ❤️ for a Healthier Sri Lanka.*
