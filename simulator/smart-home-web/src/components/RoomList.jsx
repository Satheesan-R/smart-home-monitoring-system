const RoomList = ({ rooms = [], selectedRoom, onSelectRoom }) => {
  return (
    <div className="panel">
      <div className="panel-header">
        <h3>Rooms</h3>
      </div>

      <div className="room-list">
        {rooms.map((room) => (
          <button
            key={room.id || room.name}
            type="button"
            className={`room-item ${selectedRoom === room.id ? 'active' : ''}`}
            onClick={() => onSelectRoom?.(room.id)}
          >
            <span>{room.icon || '🛋️'}</span>
            <span>{room.name}</span>
          </button>
        ))}
      </div>
    </div>
  )
}

export default RoomList
