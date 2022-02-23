package ru.vsultanyarov.nearby_backend.exception;

import org.bson.types.ObjectId;
import ru.vsultanyarov.nearby_backend.handler.BusinessError;

@BusinessError(
        errorCode = "roomAlreadyActive",
        message = "Комната уже активна")
public class RoomAlreadyActiveException extends LogException {
    public RoomAlreadyActiveException(ObjectId roomId) {
        logMessage("Room with id {} already active", roomId);
    }
}
