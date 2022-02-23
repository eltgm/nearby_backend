package ru.vsultanyarov.nearby_backend.exception;

import org.bson.types.ObjectId;

public class RoomNotExistException extends LogException {
    public RoomNotExistException(ObjectId roomId) {
        logMessage("Room with id - {} not exist", roomId);
    }
}
