# 🌿 CareLanka – Digital Healthcare Companion 🇱🇰

<p align="center">
  <img src="screenshorts/app_logo.png" width="120"/>
</p>

<p align="center">
  <b>Smart • Reliable • Sri Lankan Healthcare at Your Fingertips</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Backend-Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black"/>
  <img src="https://img.shields.io/badge/Database-Firestore-FFCA28?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Region-Sri_Lanka-8D153A?style=for-the-badge"/>
</p>

---

## 🌍 About the Project

CareLanka is a Sri Lanka-focused digital healthcare Android application built using Java & Firebase.

It connects patients, caregivers, hospitals, and pharmacies into one centralized healthcare ecosystem.

This project demonstrates real-world mobile development skills including authentication, cloud database integration, notifications, and location services.

---

# 🎥 Live App Preview

<p align="center">
  <img src="screenshorts/demo.gif" width="320"/>
</p>

---

# 🚀 Project Highlights

✔ Secure Firebase Authentication  
✔ Real-time Firestore Database  
✔ Hospital & Doctor Directory  
✔ Google Maps Integration  
✔ Emergency Quick Access  
✔ Medicine Reminder System  
✔ Clean Material UI Design  

---

# 🏥 Core Features

## 🔐 Authentication
- Email & Password Login
- Secure Registration
- Role-based user access

---

## 🏥 Find Hospitals
- Government & Private Hospitals
- Colombo District coverage
- Direct call feature
- Google Maps navigation

<p align="center">
  <img src="screenshorts/find_hospital.jpeg" width="280"/>
</p>

---

## 👨‍⚕️ Doctor & Caregiver Directory
- Specialty filtering (Cardiologist, Pediatrician, etc.)
- Doctor profile view
- Hospital association
- Contact details display

<p align="center">
  <img src="screenshorts/caregiver_details.jpeg" width="280"/>
</p>

---

## 💊 Online Medicine Services
- Healthguard
- Laugfs Wellness
- QuickMed.lk
- PickMe Flash Delivery

---

## ⏰ Smart Reminder System
- Medication alarms
- Background AlarmManager
- High-priority notifications

---

## 🚑 Emergency Access
- One-tap emergency call
- 1990 Suwa Seriya Ambulance support
- Fast-access emergency UI

---

# 🗄 Database Structure (Firestore Example)

```json
users {
  userId: {
    name: "Kavindu",
    email: "user@email.com",
    role: "patient"
  }
}

hospitals {
  hospitalId: {
    name: "Asiri Hospital",
    type: "Private",
    contact: "+94xxxxxxxx"
  }
}

doctors {
  doctorId: {
    name: "Dr. Example",
    specialty: "Cardiologist",
    hospital: "Asiri Hospital"
  }
}
```

---

# 🛠 Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java |
| UI | XML + Material Design |
| Backend | Firebase Firestore |
| Authentication | Firebase Auth |
| Notifications | AlarmManager |
| Maps | Google Maps Intent API |

---

# ⚙️ Installation

```bash
git clone https://github.com/sampleritgithubl/CareLanka.git
```

1. Open in Android Studio  
2. Sync Gradle  
3. Connect Firebase  
4. Add `google-services.json`  
5. Run project  

---

# 🧠 What I Learned

- Firebase Authentication integration
- Firestore CRUD operations
- Android lifecycle handling
- Background services & notifications
- Real-world healthcare app architecture
- Google Maps integration

---

# 📈 Future Improvements

- Appointment booking system  
- AI symptom checker  
- Prescription cloud storage  
- Sinhala & Tamil language support  

---

# 👨‍💻 Developer

Kavindu Rasanjana  
📧 kavindu20rasanjana@gmail.com  
🔗 https://github.com/sampleritgithubl  

---

# ⭐ Support

If you like this project, please ⭐ star the repository!

---

<p align="center">
  Made with ❤️ in Sri Lanka 🇱🇰
</p>
