import { useEffect, useState } from 'react'

import { subscribeToCollection } from '../services/firestoreService'

export const useFirestore = (collectionName) => {
  const [data, setData] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    if (!collectionName) {
      setLoading(false)
      return undefined
    }

    const unsubscribe = subscribeToCollection(collectionName, (items) => {
      setData(items)
      setLoading(false)
      setError(null)
    })

    const handleError = (err) => {
      setError(err)
      setLoading(false)
    }

    unsubscribe.catch?.(handleError)

    return () => {
      unsubscribe.then?.((unsub) => unsub())
    }
  }, [collectionName])

  return { data, loading, error }
}

export default useFirestore
