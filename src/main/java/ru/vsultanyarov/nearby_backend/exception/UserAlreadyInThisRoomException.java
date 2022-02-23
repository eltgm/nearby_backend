package ru.vsultanyarov.nearby_backend.exception;

import org.bson.types.ObjectId;
import ru.vsultanyarov.nearby_backend.handler.BusinessError;

@BusinessError(errorCode = "userAlreadyInThisRoom", message = "Пользователь с данным идентификатором уже есть в данной комнате")
public class UserAlreadyInThisRoomException extends LogException {
    public UserAlreadyInThisRoomException(String userId, ObjectId roomId) {
        logMessage("User with id - {} already in room - {}", userId, roomId);
    }
}
