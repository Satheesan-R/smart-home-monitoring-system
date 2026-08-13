import {
  collection,
  doc,
  getDocs,
  getDoc,
  updateDoc,
  addDoc,
  deleteDoc,
  query,
  orderBy,
  onSnapshot,
} from 'firebase/firestore'

import { db } from '../firebase/firebase'

const ensureDb = () => {
  if (!db) {
    throw new Error(
      'Firebase Firestore is not initialized. Add your Firebase web config to the .env file and restart the app.',
    )
  }

  return db
}

export const getCollection = async (collectionName) => {
  ensureDb()
  const snapshot = await getDocs(query(collection(db, collectionName), orderBy('createdAt', 'asc')))
  return snapshot.docs.map((docSnap) => ({ id: docSnap.id, ...docSnap.data() }))
}

export const getDocument = async (collectionName, id) => {
  ensureDb()
  const snapshot = await getDoc(doc(db, collectionName, id))
  return snapshot.exists() ? { id: snapshot.id, ...snapshot.data() } : null
}

export const createDocument = async (collectionName, data) => {
  ensureDb()
  const ref = await addDoc(collection(db, collectionName), {
    ...data,
    createdAt: new Date(),
  })
  return ref.id
}

export const updateDocument = async (collectionName, id, data) => {
  ensureDb()
  await updateDoc(doc(db, collectionName, id), data)
  return true
}

export const deleteDocument = async (collectionName, id) => {
  ensureDb()
  await deleteDoc(doc(db, collectionName, id))
  return true
}

export const subscribeToCollection = (collectionName, callback) => {
  ensureDb()
  const q = query(collection(db, collectionName), orderBy('createdAt', 'asc'))
  return onSnapshot(q, (snapshot) => {
    const items = snapshot.docs.map((docSnap) => ({ id: docSnap.id, ...docSnap.data() }))
    callback(items)
  })
}
