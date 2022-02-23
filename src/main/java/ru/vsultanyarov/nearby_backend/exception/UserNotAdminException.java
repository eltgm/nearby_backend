package ru.vsultanyarov.nearby_backend.exception;

import org.bson.types.ObjectId;
import ru.vsultanyarov.nearby_backend.handler.BusinessError;

@BusinessError(
        errorCode = "userNotAdmin",
        message = "Данный пользователь не является администратором комнаты")
public class UserNotAdminException extends LogException {
    public UserNotAdminException(String userId, ObjectId roomId) {
        logMessage("User with id {} is not admin of room {}", userId, roomId);
    }
}
