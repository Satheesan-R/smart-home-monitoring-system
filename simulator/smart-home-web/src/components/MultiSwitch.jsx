const MultiSwitch = ({ devices = [], onToggle }) => {
  const gangBoxDevices = devices.filter((device) => {
    const type = (device.type || '').toLowerCase()
    const name = (device.name || '').toLowerCase()

    return type === 'lamp' || type === 'fan' || name.includes('lamp') || name.includes('fan') || name.includes('light')
  })

  return (
    <div className="panel">
      <div className="panel-header">
        <h3>Gang Box</h3>
        <span>{gangBoxDevices.filter((device) => device.status === 'on').length} active</span>
      </div>

      <div className="switch-list">
        {gangBoxDevices.map((device) => {
          const isOn = device.status === 'on'

          return (
            <div key={device.id || device.name} className="switch-row">
              <div>
                <strong>{device.name}</strong>
                <small>{device.type}</small>
              </div>

              <button
                type="button"
                className={`switch-toggle ${isOn ? 'on' : 'off'}`}
                onClick={() => onToggle?.(device.id, !isOn)}
              >
                {isOn ? 'ON' : 'OFF'}
              </button>
            </div>
          )
        })}
      </div>
    </div>
  )
}

export default MultiSwitch
