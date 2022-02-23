package ru.vsultanyarov.nearby_backend.exception;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import ru.vsultanyarov.nearby_backend.handler.BusinessError;

@BusinessError(
        status = HttpStatus.NOT_FOUND,
        errorCode = "roomNotExist",
        message = "Запрашиваемая комната не существует")
public class RoomNotExistException extends LogException {
    public RoomNotExistException(ObjectId roomId) {
        logMessage("Room with id - {} not exist", roomId);
    }
}
