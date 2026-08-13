# 🏠 Smart Home Monitoring & Control System

> 📚 **SCS 3311 – Mobile Application Design & Development**
> 🎓 **University Mini Project**

A smart-home monitoring and control system built around a shared **Firebase / Firestore backend**. The project focuses on monitoring and controlling smart devices across multiple floors and rooms, with a **web-based hardware simulator** representing the physical smart-home environment.

---

## 📖 Project Overview

This system allows users to **monitor and control smart-home devices in real time** through a cloud-connected architecture.

The repository currently contains the **Web Hardware Simulator**, which communicates with Firebase Firestore to mirror device state changes and simulate real smart-home hardware behavior.

### 🏗️ System Architecture

```text
        📱 Android App
             │
             ▼
      ☁️ Firebase / Firestore
             │
             ▼
    🖥️ Web Hardware Simulator
```

Both the mobile client and simulator use the **same Firebase backend**, allowing synchronized state updates without direct app-to-app communication.

---

## ✨ Main Features

### 🏢 Multi-Floor Smart Home

* 🏠 Multiple floors supported
* ⬇️ Ground Floor
* ⬆️ First Floor
* 🚪 Rooms grouped under each floor
* 📱 Floor-based device dashboard

### 🎛️ Device Control

Supported device types include:

* 💡 Lights
* 🔌 Electrical outlets
* 🎚️ Multi-switch units
* 🔥 Safety-critical iron
* 📹 Security cameras

### 🔄 Device State Management

Each device can be in one of the following states:

| Status             | Meaning                |
| ------------------ | ---------------------- |
| 🟢 **ON**          | Device is active       |
| ⚪ **OFF**          | Device is inactive     |
| 🔴 **ERROR**       | Device has an error    |
| ⚫ **DISCONNECTED** | Device is disconnected |

### 🎚️ Multi-Switch Control

A single physical switch unit may contain multiple independently controllable switches.

Example:

* 💡 Main Light
* 🌀 Fan
* 🛋️ Lamp

Each switch maintains its own state without affecting the other switches.

### 🔥 Iron Safety Management

The iron is treated as a **safety-critical device** and supports:

* ⏱️ Maximum ON duration
* 🕐 Turned-on timestamp tracking
* 🛑 Automatic cutoff after timeout
* ⚪ Automatic status update to OFF
* 🚨 Safety alert generation

### ⏰ Scheduling

Some lights can include scheduled:

* ▶️ Start time
* ⏹️ Stop time
* 🔄 Automatic operation

### 📹 Security Camera Simulation

The mock camera implementation supports:

* 🟢 ONLINE
* ⚪ OFFLINE
* ⚫ DISCONNECTED

---

## 🖥️ Web Hardware Simulator

The simulator represents the **physical smart-home hardware**.

Its responsibilities include:

* 📥 Reading device states from Firestore
* 📊 Displaying current device status
* 🔧 Simulating physical hardware changes
* 📤 Writing updated states back to Firebase
* 🔄 Testing synchronization between the client and cloud backend

This repository contains the frontend simulator built with:

**⚛️ React + ⚡ Vite + 🔥 Firebase / Firestore**

---

## 🛠️ Technologies Used

### 🌐 Web Simulator

* ⚛️ React
* 🟨 JavaScript
* 🌐 HTML
* 🎨 CSS
* ⚡ Vite
* 🔥 Firebase / Firestore

### ☁️ Backend / Shared Storage

* 🔥 Firebase Cloud Firestore

---

## 📁 Repository Structure

```text
smart-home-monitoring-system/
│
├── 📄 README.md
│
├── 📂 simulator/
│   └── 📂 smart-home-web/
│       │
│       ├── 📄 index.html
│       ├── 📄 package.json
│       ├── 📄 vite.config.js
│       ├── 📄 .env.example
│       │
│       ├── 📂 public/
│       │
│       └── 📂 src/
│           ├── 🎨 App.css
│           ├── ⚛️ App.jsx
│           ├── 🎨 index.css
│           ├── ⚛️ main.jsx
│           │
│           ├── 📂 assets/
│           ├── 📂 components/
│           ├── 📂 firebase/
│           ├── 📂 hooks/
│           ├── 📂 pages/
│           └── 📂 services/
│
└── 📂 documentation/
```

---

## 🔥 Firestore Structure

A typical Firestore design for this project is:

```text
devices/
│
├── 💡 bedroom_light_01/
│   ├── name
│   ├── type
│   ├── status
│   ├── floorId
│   ├── roomId
│   └── position
│
├── 🔥 bedroom_iron_01/
│   ├── name
│   ├── type
│   ├── status
│   ├── floorId
│   ├── roomId
│   ├── maxOnDuration
│   └── turnedOnAt
│
├── 🎚️ living_switch_01/
│   ├── name
│   ├── type
│   ├── switchCount
│   └── switches
│
└── 📹 bedroom_camera_01/
    ├── name
    ├── type
    ├── status
    └── cameraUri
```

### 🎚️ Example Multi-Switch Data

```json
{
  "switches": [
    {
      "id": "switch_1",
      "name": "Main Light",
      "status": "OFF"
    },
    {
      "id": "switch_2",
      "name": "Fan",
      "status": "ON"
    },
    {
      "id": "switch_3",
      "name": "Lamp",
      "status": "OFF"
    }
  ]
}
```

---

## 🚀 Setup Instructions

### 1️⃣ Open the Simulator Folder

```bash
cd simulator/smart-home-web
```

### 2️⃣ Install Dependencies

```bash
npm install
```

### 3️⃣ Configure Firebase

Copy the example environment file:

```bash
cp .env.example .env
```

Then update `.env` with your Firebase project configuration.

```env
VITE_FIREBASE_API_KEY=your_api_key_here
VITE_FIREBASE_AUTH_DOMAIN=your-project.firebaseapp.com
VITE_FIREBASE_PROJECT_ID=your-project-id
VITE_FIREBASE_STORAGE_BUCKET=your-project.appspot.com
VITE_FIREBASE_MESSAGING_SENDER_ID=your_sender_id
VITE_FIREBASE_APP_ID=your_app_id_here
```

### 4️⃣ ▶️ Run the Application

```bash
npm run dev -- --host 0.0.0.0
```

Then open:

```text
http://localhost:5173/
```

### 5️⃣ 📦 Production Build

```bash
npm run build
```

Preview the production version:

```bash
npm run preview -- --host 0.0.0.0
```

---

## 🔐 Firebase Configuration Notes

* 🔥 Use the **same Firebase project** for all connected applications.
* ☁️ Enable **Cloud Firestore**.
* 🛡️ Configure appropriate Firestore read/write rules.
* 🚫 Never commit private credentials or secret keys to GitHub.
* 🔑 Store Firebase configuration values in `.env`.

---

## 🧪 Testing Scenarios

The system should be tested using the following synchronization workflows:

* [ ] 🔄 Web simulator changes a device status → client reflects the update.
* [ ] 📱 Client changes a device → simulator updates automatically.
* [ ] 🎚️ Multi-switch control updates only the selected switch.
* [ ] 🔥 Iron exceeds maximum ON duration → automatically turns OFF.
* [ ] 🚨 Iron safety alert is generated correctly.
* [ ] 📹 Camera state changes correctly between ONLINE, OFFLINE, and DISCONNECTED.
* [ ] ⏰ Scheduled light behavior triggers according to configured times.
* [ ] ☁️ Firestore updates are synchronized in real time.

---

## 👥 Team Information

This project is developed as part of the **SCS 3311 coursework**.

| 📌               | Details                                            |
| ---------------- | -------------------------------------------------- |
| 🏠 **Project**   | Smart Home Monitoring & Control System             |
| 📚 **Course**    | SCS 3311 – Mobile Application Design & Development |

| 👤 | Team Member |
|---|---|
| 🧑‍💻 **Member 01** | **R. Satheesan** |
| 👩‍💻 **Member 02** | **A.P. Arulpirabakar** |

👥 **Team Size:** 2 Members

---

## 🎯 Project Goal

The main goal of this project is to demonstrate how a **mobile application, cloud database, and simulated smart-home hardware** can work together to provide real-time monitoring and control.

```text
📱 Mobile Application
        ↕
   ☁️ Firebase
        ↕
🖥️ Hardware Simulator
        ↓
🏠 Smart Home Devices
```

---

## 📝 Final Note

This repository focuses on the **Web Hardware Simulator** and its Firebase integration.

If the Android mobile client is developed separately, it should connect to the **same Firebase project** so that device states remain synchronized across the complete smart-home system.

---

## 🏠 Smart Home Monitoring & Control System

**Built with ❤️ using React, Vite & Firebase**

> 🎓 **SCS 3311 – Mobile Application Design & Development**
