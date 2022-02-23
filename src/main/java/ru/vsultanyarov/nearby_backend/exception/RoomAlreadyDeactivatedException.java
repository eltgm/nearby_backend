package ru.vsultanyarov.nearby_backend.exception;

import org.bson.types.ObjectId;
import ru.vsultanyarov.nearby_backend.handler.BusinessError;

@BusinessError(
        errorCode = "roomAlreadyDeactivated",
        message = "Комната уже удалена")
public class RoomAlreadyDeactivatedException extends LogException {
    public RoomAlreadyDeactivatedException(ObjectId roomId) {
        logMessage("Room with id {} already deactivated", roomId);
    }
}
