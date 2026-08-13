const CameraView = ({ device, roomName = 'Living Room', camera = {} }) => {
  const displayCamera = device || camera

  return (
    <div className="panel camera-panel">
      <div className="panel-header">
        <h3>{displayCamera.name || 'Camera View'}</h3>
        <span>{displayCamera.status || camera.status || 'ONLINE'}</span>
      </div>

      <div className="camera-screen">
        <img
          src="https://via.placeholder.com/300x200?text=Mock+Snapshot"
          alt="Camera"
          style={{ width: '100%', height: '100%', objectFit: 'cover', display: 'block' }}
        />

        <div className="camera-overlay">
          <span className="camera-label">{roomName}</span>
          <span className="camera-dot" />
        </div>
      </div>

      <div className="camera-info">
        <div>
          <small>Frame</small>
          <strong>{camera.frame || 'HD'}</strong>
        </div>
        <div>
          <small>Motion</small>
          <strong>{camera.motion ? 'Detected' : 'Idle'}</strong>
        </div>
      </div>
    </div>
  )
}

export default CameraView
