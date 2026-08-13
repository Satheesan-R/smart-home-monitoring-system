const FloorList = ({ floors = [], selectedFloor, onSelectFloor }) => {
  return (
    <div className="panel">
      <div className="panel-header">
        <h3>Floors</h3>
      </div>

      <div className="floor-list">
        {floors.map((floor) => (
          <button
            key={floor.id || floor.name}
            type="button"
            className={`floor-item ${selectedFloor === floor.id ? 'active' : ''}`}
            onClick={() => onSelectFloor?.(floor.id)}
          >
            <span>{floor.icon || '🏠'}</span>
            <span>{floor.name}</span>
          </button>
        ))}
      </div>
    </div>
  )
}

export default FloorList
