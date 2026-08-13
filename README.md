# Smart Home Monitoring & Control System

> SCS 3311 – Mobile Application Design & Development  
> University Mini Project

A smart-home monitoring and control system built around a shared Firebase / Firestore backend. The project focuses on monitoring and controlling smart devices across multiple floors and rooms, with a web-based simulator representing the physical smart-home environment.

---

## Project Overview

This system allows users to monitor and control smart-home devices in real time through a cloud-connected architecture.

The repository currently contains the web simulator component, which communicates with Firebase Firestore to mirror device state changes and simulate real smart-home hardware behavior.

### System Architecture

```text
       📱 Android App (optional/client app)
                 ↓
          ☁️ Firebase / Firestore
                 ↓
       🖥️ Web Hardware Simulator
```

Both the mobile client and the simulator use the same Firebase backend, allowing synchronized state updates without direct app-to-app communication.

---

## Main Features

### Multi-floor smart home layout
- Multiple floors supported
- Ground Floor and First Floor included
- Rooms grouped under each floor
- Devices represented in a floor-based dashboard

### Device control
Supported device types include:
- Lights
- Electrical outlets
- Multi-switch units
- Safety-critical iron
- Security cameras

### Device state management
Each device can be in one of the following states:
- ON
- OFF
- ERROR
- DISCONNECTED

### Multi-switch control
A single physical switch unit may include multiple switches, each independently controllable.

Example:
- Main light
- Fan
- Lamp

Each switch keeps its own state and does not reset the others.

### Iron safety management
The iron is treated as a safety-critical device and supports:
- Maximum ON duration
- Turned-on timestamp tracking
- Automatic cutoff after timeout
- Status update to OFF
- Safety alert generation

### Scheduling
Some lights can include scheduled start and stop times for automatic operation.

### Security camera simulation
A mock camera implementation is used to simulate:
- ONLINE
- OFFLINE
- DISCONNECTED

---

## Web Hardware Simulator

The simulator represents the physical smart-home hardware and performs the following roles:
- Reads device states from Firestore
- Displays current device status
- Simulates physical changes to hardware devices
- Writes updated states back to Firebase
- Tests the synchronization flow between the client and cloud backend

This repository includes the frontend simulator built with React + Vite.

---

## Technologies Used

### Web simulator
- React
- JavaScript
- HTML
- CSS
- Vite
- Firebase / Firestore

### Backend / shared storage
- Firebase Cloud Firestore

---

## Repository Structure

```text
smart-home-monitoring-system/
├── README.md
├── simulator/
│   └── smart-home-web/
│       ├── index.html
│       ├── package.json
│       ├── vite.config.js
│       ├── .env.example
│       ├── public/
│       └── src/
│           ├── App.css
│           ├── App.jsx
│           ├── index.css
│           ├── main.jsx
│           ├── assets/
│           ├── components/
│           ├── firebase/
│           ├── hooks/
│           ├── pages/
│           └── services/
└── documentation/   (if added later)
```

---

## Firestore Structure

A typical Firestore design for this project is:

```text
devices/
├── bedroom_light_01/
│   ├── name
│   ├── type
│   ├── status
│   ├── floorId
│   ├── roomId
│   └── position
├── bedroom_iron_01/
│   ├── name
│   ├── type
│   ├── status
│   ├── floorId
│   ├── roomId
│   ├── maxOnDuration
│   └── turnedOnAt
├── living_switch_01/
│   ├── name
│   ├── type
│   ├── switchCount
│   └── switches
└── bedroom_camera_01/
    ├── name
    ├── type
    ├── status
    └── cameraUri
```

### Example multi-switch data

```json
"switches": [
  { "id": "switch_1", "name": "Main Light", "status": "OFF" },
  { "id": "switch_2", "name": "Fan", "status": "ON" },
  { "id": "switch_3", "name": "Lamp", "status": "OFF" }
]
```

---

## Setup Instructions

### 1. Open the simulator folder

```bash
cd simulator/smart-home-web
```

### 2. Install dependencies

```bash
npm install
```

### 3. Configure Firebase environment variables

Copy the example environment file:

```bash
cp .env.example .env
```

Then update the values in `.env` with your Firebase project configuration.

Example:

```bash
VITE_FIREBASE_API_KEY=your_api_key_here
VITE_FIREBASE_AUTH_DOMAIN=your-project.firebaseapp.com
VITE_FIREBASE_PROJECT_ID=your-project-id
VITE_FIREBASE_STORAGE_BUCKET=your-project.appspot.com
VITE_FIREBASE_MESSAGING_SENDER_ID=your_sender_id
VITE_FIREBASE_APP_ID=your_app_id_here
```

### 4. Run the app

```bash
npm run dev -- --host 0.0.0.0
```

Then open:

```text
http://localhost:5173/
```

### 5. Production build

```bash
npm run build
```

To preview the production version:

```bash
npm run preview -- --host 0.0.0.0
```

---

## Firebase Configuration Notes

- Use the same Firebase project for all connected apps.
- Enable Cloud Firestore.
- Configure proper Firestore rules for read/write access.
- Never commit private credentials or secret keys to GitHub.

---

## Testing Scenarios

The system should be tested for the following synchronization workflows:

1. Web simulator changes a device status and the client reflects the update.
2. Client changes a device and the simulator updates automatically.
3. Multi-switch control updates only the selected switch.
4. Iron exceeds the maximum allowed ON duration and turns OFF automatically.
5. Camera state updates correctly between ONLINE, OFFLINE, and DISCONNECTED.
6. Scheduled light behavior triggers according to configured times.

---

## Team Information

This project is developed as part of the SCS 3311 coursework.

- Project: Smart Home Monitoring & Control System
- Course: SCS 3311 – Mobile Application Design & Development
- Team Size: 2 Members

> Replace the team details with the actual members of your group if needed.

---

## Final Note

This repository focuses on the web-based smart-home simulator and its Firebase integration. If the Android mobile client is developed separately, it should connect to the same Firebase project to maintain real-time synchronization across the full system.
