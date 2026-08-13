import { getApp, getApps, initializeApp } from 'firebase/app'
import { getFirestore } from 'firebase/firestore'

const firebaseConfig = {
  apiKey: import.meta.env.VITE_FIREBASE_API_KEY,
  authDomain: import.meta.env.VITE_FIREBASE_AUTH_DOMAIN,
  projectId: import.meta.env.VITE_FIREBASE_PROJECT_ID || 'smart-home-monitoring-sy-cad5d',
  storageBucket: import.meta.env.VITE_FIREBASE_STORAGE_BUCKET || 'smart-home-monitoring-sy-cad5d.firebasestorage.app',
  messagingSenderId: import.meta.env.VITE_FIREBASE_MESSAGING_SENDER_ID || '991367632914',
  appId: import.meta.env.VITE_FIREBASE_APP_ID,
}

const hasRequiredConfig = Object.values(firebaseConfig).every(
  (value) => value && !String(value).startsWith('YOUR_') && !String(value).includes('demo-'),
)

const app = hasRequiredConfig ? (getApps().length ? getApp() : initializeApp(firebaseConfig)) : null
export const db = app ? getFirestore(app) : null

if (!hasRequiredConfig) {
  console.warn(
    'Firebase is not fully configured. Add your Firebase web config to the .env file before using Firestore.',
  )
}

export default app
