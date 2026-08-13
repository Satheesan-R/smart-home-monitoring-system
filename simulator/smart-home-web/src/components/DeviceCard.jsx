import { doc, updateDoc } from 'firebase/firestore'

import { db } from '../firebase/firebase'

const DeviceCard = ({ device, onToggle, onScheduleChange }) => {
  const statusColor = {
    ON: 'green',
    OFF: 'gray',
    ERROR: 'red',
    DISCONNECTED: '#a3a3a3',
  }[device.status] || 'gray'

  const isOn = device.status === 'on' || device.status === 'ON'
  const schedule = device.schedule || { startTime: '18:00', endTime: '22:00', enabled: true }
  const isLamp = device.type === 'Lamp' || device.name?.toLowerCase().includes('light')

  const formatTimeLabel = (timeValue) => {
    const [hours, minutes] = (timeValue || '18:00').split(':').map(Number)
    const date = new Date()
    date.setHours(hours, minutes, 0, 0)

    return new Intl.DateTimeFormat('en-US', {
      hour: 'numeric',
      minute: '2-digit',
    }).format(date)
  }

  const startLabel = formatTimeLabel(schedule.startTime)
  const endLabel = formatTimeLabel(schedule.endTime)
  const nextActionText = schedule.enabled
    ? isOn
      ? `Next action: Turn OFF at ${endLabel}`
      : `Next action: Turn ON at ${startLabel}`
    : 'Next action: Schedule disabled'

  const setStatus = async (newStatus) => {
    await updateDoc(doc(db, 'devices', device.id), { status: newStatus })
  }

  return (
    <div className="device-card">
      <div className="device-top-row">
        <div>
          <p className="device-type">{device.type || 'Device'}</p>
          <h4>{device.name}</h4>
        </div>
        <span
          className={`status-badge ${isOn ? 'on' : 'off'}`}
          style={{
            color: statusColor,
            backgroundColor:
              device.status === 'ERROR'
                ? 'rgba(239, 68, 68, 0.15)'
                : device.status === 'DISCONNECTED'
                  ? 'rgba(163, 163, 163, 0.2)'
                  : 'transparent',
          }}
        >
          {device.status || (isOn ? 'On' : 'Off')}
        </span>
      </div>

      <div className="device-meta">
        <span>{device.location || 'Home'}</span>
        <span>{device.power || '0 W'}</span>
      </div>

      <button
        type="button"
        className="toggle-button"
        onClick={() => onToggle?.(device.id, !isOn)}
      >
        {isOn ? 'Turn off' : 'Turn on'}
      </button>

      {isLamp && (
        <div className="schedule-panel">
          <div className="schedule-title">💡 {device.name} ({isOn ? 'ON' : 'OFF'})</div>

          <div className="schedule-row">
            <span>Schedule:</span>
            <label className="schedule-checkbox">
              <input
                type="checkbox"
                checked={Boolean(schedule.enabled)}
                onChange={(event) => onScheduleChange?.(device.id, 'enabled', event.target.checked)}
              />
              Enable
            </label>
          </div>

          <div className="schedule-row">
            <span>Start:</span>
            <input
              type="time"
              value={schedule.startTime || '18:00'}
              onChange={(event) => onScheduleChange?.(device.id, 'startTime', event.target.value)}
            />
          </div>

          <div className="schedule-row">
            <span>End:</span>
            <input
              type="time"
              value={schedule.endTime || '22:00'}
              onChange={(event) => onScheduleChange?.(device.id, 'endTime', event.target.value)}
            />
          </div>

          <div className="next-action-text">{nextActionText}</div>
        </div>
      )}

      <div style={{ display: 'flex', gap: '8px', marginTop: '12px' }}>
        <button type="button" onClick={() => setStatus('ERROR')}>
          Error
        </button>
        <button type="button" onClick={() => setStatus('DISCONNECTED')}>
          Disconnect
        </button>
      </div>
    </div>
  )
}

export default DeviceCard
