package ru.vsultanyarov.nearby_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import ru.vsultanyarov.nearby_backend.domain.Room;
import ru.vsultanyarov.nearby_backend.domain.User;
import ru.vsultanyarov.nearby_backend.exception.*;
import ru.vsultanyarov.nearby_backend.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final RoomRepository roomRepository;

    public Room activateRoom(ObjectId roomId, String userId) {
        Room room = findRoom(roomId);
        validateIsUserAdmin(room, userId);
        if (room.isActive()) {
            throw new RoomAlreadyActiveException(roomId);
        }
        room.setActive(true);

        return roomRepository.save(room);
    }

    public Room deleteRoom(ObjectId roomId, String userId) {
        Room room = findRoom(roomId);
        if (!room.isActive()) {
            throw new RoomAlreadyDeactivatedException(roomId);
        }
        validateIsUserAdmin(room, userId);

        room.setActive(false);
        room.setUsers(null);

        return roomRepository.save(room);
    }

    private Room findRoom(ObjectId roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isEmpty()) {
            throw new RoomNotExistException(roomId);
        }

        return roomOptional.get();
    }

    private void validateIsUserAdmin(Room room, String userId) {
        List<User> users = room.getUsers();
        Optional<User> adminUserOptional = users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();

        if (adminUserOptional.isEmpty()) {
            throw new UserNotFoundException(userId, room.getId());
        }

        User adminUser = adminUserOptional.get();
        if (!adminUser.isAdmin()) {
            throw new UserNotAdminException(userId, room.getId());
        }
    }
}
