import { useEffect, useMemo, useState } from 'react'
import { collection, onSnapshot, query, updateDoc, where } from 'firebase/firestore'

import FloorList from '../components/FloorList'
import RoomList from '../components/RoomList'
import DeviceCard from '../components/DeviceCard'
import MultiSwitch from '../components/MultiSwitch'
import IronControl from '../components/IronControl'
import CameraView from '../components/CameraView'
import { db } from '../firebase/firebase'

const defaultFloors = [
  {
    id: 'floor-1',
    name: 'Ground Floor',
    icon: '🏠',
    rooms: [
      { id: 'room-1', name: 'Living Room', icon: '🛋️' },
      { id: 'room-2', name: 'Kitchen', icon: '🍳' },
      { id: 'room-4', name: 'Laundry', icon: '🧺' },
    ],
  },
  {
    id: 'floor-2',
    name: 'First Floor',
    icon: '🏡',
    rooms: [
      { id: 'room-3', name: 'Bedroom', icon: '🛏️' },
      { id: 'room-5', name: 'Entertainment', icon: '🎮' },
    ],
  },
]

const defaultDevices = [
  {
    id: 'lamp-1',
    name: 'Lamp',
    type: 'Lamp',
    location: 'Living Room',
    status: 'on',
    power: '42 W',
    schedule: { startTime: '18:00', endTime: '22:00', enabled: true },
  },
  {
    id: 'tv-1',
    name: 'TV Outlet',
    type: 'Entertainment',
    location: 'Living Room',
    status: 'off',
    power: '120 W',
  },
  {
    id: 'camera-1',
    name: 'Security Camera',
    type: 'Camera',
    location: 'Living Room',
    status: 'online',
    power: '8 W',
  },
  {
    id: 'iron-1',
    name: 'Iron',
    type: 'Iron',
    location: 'Kitchen',
    status: 'on',
    temperature: 180,
    power: '900 W',
    turnedOnAt: new Date(Date.now() - 12 * 60 * 1000),
    maxOnDuration: 30 * 60,
  },
  {
    id: 'washer-1',
    name: 'Washer',
    type: 'Outlet',
    location: 'Laundry',
    status: 'off',
    power: '0 W',
  },
  {
    id: 'bedlight-1',
    name: 'Bedroom Light',
    type: 'Lamp',
    location: 'Bedroom',
    status: 'on',
    power: '40 W',
    schedule: { startTime: '18:00', endTime: '22:00', enabled: true },
  },
  {
    id: 'fan-1',
    name: 'Ceiling Fan',
    type: 'Fan',
    location: 'Bedroom',
    status: 'off',
    power: '18 W',
  },
  {
    id: 'switch-1',
    name: 'S1: Lamp',
    type: 'Gang Box',
    location: 'Entertainment',
    status: 'on',
  },
  {
    id: 'switch-2',
    name: 'S2: Fan',
    type: 'Gang Box',
    location: 'Entertainment',
    status: 'off',
  },
  {
    id: 'switch-3',
    name: 'S3: TV',
    type: 'Gang Box',
    location: 'Entertainment',
    status: 'on',
  },
  {
    id: 'switch-4',
    name: 'S4: Sub',
    type: 'Gang Box',
    location: 'Entertainment',
    status: 'off',
  },
]

const Dashboard = () => {
  const [selectedFloor, setSelectedFloor] = useState(defaultFloors[0].id)
  const [selectedRoom, setSelectedRoom] = useState(defaultFloors[0].rooms[0].id)
  const [devices, setDevices] = useState(defaultDevices)

  const currentFloor = useMemo(
    () => defaultFloors.find((floor) => floor.id === selectedFloor),
    [selectedFloor],
  )

  const currentRoom = useMemo(
    () => currentFloor?.rooms.find((room) => room.id === selectedRoom),
    [currentFloor, selectedRoom],
  )

  useEffect(() => {
    if (!currentFloor) return
    const defaultRoomId = currentFloor.rooms?.[0]?.id
    const roomExistsOnFloor = currentFloor.rooms?.some((room) => room.id === selectedRoom)

    if (defaultRoomId && !roomExistsOnFloor) {
      setSelectedRoom(defaultRoomId)
    }
  }, [currentFloor, selectedRoom])

  const visibleDevices = useMemo(
    () => {
      const selectedFloorName = currentFloor?.name
      const selectedRoomName = currentRoom?.name

      return devices.filter(
        (device) =>
          device.location === selectedRoomName || device.location === selectedFloorName,
      )
    },
    [devices, currentFloor, currentRoom],
  )

  const ironDevice = visibleDevices.find((device) => device.type === 'Iron')
  const cameraDevice = visibleDevices.find((device) => device.type === 'Camera')

  const floorPlanCells = useMemo(() => {
    const floorName = currentFloor?.name || 'Ground Floor'

    if (floorName === 'Ground Floor') {
      return [
        { id: 'kitchen', label: 'Kitchen', devices: visibleDevices.filter((device) => device.location === 'Kitchen') },
        { id: 'living-room', label: 'Living Room', devices: visibleDevices.filter((device) => device.location === 'Living Room') },
        { id: 'laundry', label: 'Laundry', devices: visibleDevices.filter((device) => device.location === 'Laundry') },
        { id: 'hallway', label: 'Hallway', devices: [] },
        { id: 'camera', label: 'Camera', devices: visibleDevices.filter((device) => device.type === 'Camera') },
        { id: 'storage', label: 'Storage', devices: [] },
        { id: 'empty-a', label: '', devices: [] },
        { id: 'empty-b', label: '', devices: [] },
        { id: 'empty-c', label: '', devices: [] },
      ]
    }

    return [
      { id: 'bedroom', label: 'Bedroom', devices: visibleDevices.filter((device) => device.location === 'Bedroom') },
      { id: 'entertainment', label: 'Entertainment', devices: visibleDevices.filter((device) => device.location === 'Entertainment') },
      { id: 'hall', label: 'Hall', devices: [] },
      { id: 'empty-d', label: '', devices: [] },
      { id: 'empty-e', label: '', devices: [] },
      { id: 'empty-f', label: '', devices: [] },
      { id: 'empty-g', label: '', devices: [] },
      { id: 'empty-h', label: '', devices: [] },
      { id: 'empty-i', label: '', devices: [] },
    ]
  }, [currentFloor, visibleDevices])

  const handleToggleDevice = (deviceId, nextStatus) => {
    setDevices((current) =>
      current.map((device) =>
        device.id === deviceId ? { ...device, status: nextStatus ? 'on' : 'off' } : device,
      ),
    )
  }

  const handleTemperatureChange = (deviceId, temperature) => {
    setDevices((current) =>
      current.map((device) =>
        device.id === deviceId ? { ...device, temperature } : device,
      ),
    )
  }

  const handleScheduleChange = (deviceId, field, value) => {
    setDevices((current) =>
      current.map((device) => {
        if (device.id !== deviceId) return device

        const currentSchedule = device.schedule || {
          startTime: '18:00',
          endTime: '22:00',
          enabled: true,
        }

        return {
          ...device,
          schedule: {
            ...currentSchedule,
            [field]: value,
          },
        }
      }),
    )
  }

  useEffect(() => {
    if (!db) return undefined

    const q = query(collection(db, 'devices'), where('type', '==', 'IRON'))
    const unsub = onSnapshot(q, (snapshot) => {
      snapshot.docs.forEach((document) => {
        const data = document.data()

        if (data.status === 'ON') {
          const turnedOnAt = data.turnedOnAt?.toDate?.() || new Date(data.turnedOnAt)
          const maxDuration = data.maxOnDuration || 1800
          const elapsed = (Date.now() - turnedOnAt.getTime()) / 1000

          if (elapsed > maxDuration) {
            updateDoc(document.ref, { status: 'OFF' })
              .then(() => {
                console.log(`Safety cutoff: ${data.name} turned OFF`)
              })
              .catch((error) => {
                console.error('Iron auto-shutoff failed:', error)
              })
          }
        }
      })
    })

    return () => unsub()
  }, [])

  useEffect(() => {
    if (!db) return undefined

    const parseTimeToMinutes = (timeString = '00:00') => {
      const [hours, minutes] = timeString.split(':').map(Number)
      return hours * 60 + minutes
    }

    const getCurrentTimeString = () => {
      const now = new Date()
      return `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`
    }

    const updateScheduledLights = () => {
      const q = query(collection(db, 'devices'), where('type', '==', 'LIGHT'), where('schedule.enabled', '==', true))

      onSnapshot(q, (snapshot) => {
        snapshot.docs.forEach((document) => {
          const data = document.data()
          const schedule = data.schedule

          if (!schedule?.enabled) return

          const now = new Date()
          const currentMinutes = now.getHours() * 60 + now.getMinutes()
          const startMinutes = parseTimeToMinutes(schedule.startTime)
          const endMinutes = parseTimeToMinutes(schedule.endTime)

          const shouldBeOn =
            startMinutes <= endMinutes
              ? currentMinutes >= startMinutes && currentMinutes < endMinutes
              : currentMinutes >= startMinutes || currentMinutes < endMinutes

          const nextStatus = shouldBeOn ? 'ON' : 'OFF'

          if (data.status !== nextStatus) {
            updateDoc(document.ref, { status: nextStatus })
              .then(() => {
                console.log(`Schedule update: ${data.name} set to ${nextStatus}`)
              })
              .catch((error) => {
                console.error('Light scheduling update failed:', error)
              })
          }
        })
      })
    }

    updateScheduledLights()
    const interval = setInterval(() => {
      const now = new Date()
      const timeStr = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`
      const q = query(collection(db, 'devices'), where('type', '==', 'LIGHT'), where('schedule.enabled', '==', true))

      onSnapshot(q, (snapshot) => {
        snapshot.docs.forEach((document) => {
          const data = document.data()
          const schedule = data.schedule

          if (!schedule?.enabled) return

          const start = schedule.startTime
          const end = schedule.endTime
          const shouldBeOn = timeStr >= start && timeStr < end
          const nextStatus = shouldBeOn ? 'ON' : 'OFF'

          if (data.status !== nextStatus) {
            updateDoc(document.ref, { status: nextStatus })
              .then(() => {
                console.log(`Schedule update: ${data.name} set to ${nextStatus}`)
              })
              .catch((error) => {
                console.error('Light scheduling update failed:', error)
              })
          }
        })
      })
    }, 60000)

    return () => clearInterval(interval)
  }, [])

  return (
    <div className="dashboard-layout">
      <aside className="sidebar left">
        <FloorList
          floors={defaultFloors}
          selectedFloor={selectedFloor}
          onSelectFloor={setSelectedFloor}
        />
        <RoomList
          rooms={currentFloor?.rooms || []}
          selectedRoom={selectedRoom}
          onSelectRoom={setSelectedRoom}
        />
      </aside>

      <main className="main-panel">
        <header className="topbar">
          <div>
            <p className="eyebrow">Smart Home</p>
            <h1>Monitoring Dashboard</h1>
          </div>
          <span className="status-pill">System Online</span>
        </header>

        <section className="content-grid">
          <div className="left-stack">
            <div className="panel floor-plan-panel">
              <div className="panel-header">
                <h3>{currentFloor?.name || 'Ground Floor'} (Grid)</h3>
              </div>

              <div className="floor-plan-grid">
                {floorPlanCells.map((cell) => (
                  <div key={cell.id} className="floor-plan-cell">
                    {cell.label && <span className="floor-plan-label">{cell.label}</span>}
                    {cell.devices.length > 0 && (
                      <div className="floor-plan-device-stack">
                        {cell.devices.map((device) => (
                          <div key={device.id} className="floor-plan-device">
                            {device.name}
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                ))}
              </div>
            </div>

            <div className="device-grid">
              {visibleDevices.map((device) => (
                <DeviceCard
                  key={device.id}
                  device={device}
                  onToggle={handleToggleDevice}
                  onScheduleChange={handleScheduleChange}
                />
              ))}
            </div>
          </div>

          <div className="right-stack">
            <MultiSwitch devices={visibleDevices} onToggle={handleToggleDevice} />
            <IronControl
              device={ironDevice}
              onToggle={handleToggleDevice}
              onTemperatureChange={handleTemperatureChange}
            />
            <CameraView
              device={cameraDevice}
              roomName={currentRoom?.name || 'Living Room'}
              camera={{ status: 'ONLINE', frame: 'Mock Snapshot', motion: false }}
            />
            <div className="panel report-panel">
              <div className="panel-header">
                <h3>Reports</h3>
              </div>
              <div className="report-summary">
                Iron: 45 min today | Light: 5.2 kWh | TV: 2.1 kWh
              </div>
            </div>
          </div>
        </section>
      </main>
    </div>
  )
}

export default Dashboard
