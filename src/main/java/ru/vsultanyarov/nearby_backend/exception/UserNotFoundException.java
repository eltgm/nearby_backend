package ru.vsultanyarov.nearby_backend.exception;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import ru.vsultanyarov.nearby_backend.handler.BusinessError;

@BusinessError(
        status = HttpStatus.NOT_FOUND,
        errorCode = "userNotFound",
        message = "Запрашиваемый пользователь не найден")
public class UserNotFoundException extends LogException {
    public UserNotFoundException(String userId, ObjectId roomId) {
        logMessage("User with id {} not found in room {}", userId, roomId);
    }
}
