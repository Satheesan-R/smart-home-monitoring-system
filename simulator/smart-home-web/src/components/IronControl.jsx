const IronControl = ({ device, onToggle, onTemperatureChange }) => {
  if (!device) {
    return (
      <div className="panel empty-panel">
        <h3>Iron Control</h3>
        <p>No iron device detected in this room.</p>
      </div>
    )
  }

  const isOn = String(device.status).toLowerCase() === 'on'
  const statusLabel = isOn ? 'ON' : 'OFF'
  const maxMinutes = Math.round((device.maxOnDuration || 1800) / 60)
  const turnedOnAt = device.turnedOnAt ? new Date(device.turnedOnAt) : null
  const elapsedMinutes = turnedOnAt
    ? Math.max(0, Math.floor((Date.now() - turnedOnAt.getTime()) / 60000))
    : 0

  return (
    <div className="panel">
      <div className="panel-header">
        <h3>Iron</h3>
        <span>{statusLabel}</span>
      </div>

      <div className="iron-content">
        <div className="iron-summary">
          <p>Status: <strong>{statusLabel}</strong></p>
          <p>Max: <strong>{maxMinutes} min</strong></p>
          <p>Elapsed: <strong>{elapsedMinutes} min</strong></p>
          {isOn && <p className="safety-alert">⚠️ Safety Active</p>}
        </div>

        <button
          type="button"
          className="toggle-button"
          onClick={() => onToggle?.(device.id, !isOn)}
        >
          {isOn ? 'Turn Off' : 'Turn On'}
        </button>
      </div>
    </div>
  )
}

export default IronControl
