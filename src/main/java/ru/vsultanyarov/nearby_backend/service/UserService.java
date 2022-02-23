package ru.vsultanyarov.nearby_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import ru.vsultanyarov.nearby_backend.domain.Coordinates;
import ru.vsultanyarov.nearby_backend.domain.Room;
import ru.vsultanyarov.nearby_backend.domain.User;
import ru.vsultanyarov.nearby_backend.exception.RoomNotExistException;
import ru.vsultanyarov.nearby_backend.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final RoomRepository roomRepository;

    public Room createRoom(String userId) {
        return roomRepository.save(
                Room.builder()
                        .isActive(true)
                        .users(
                                List.of(User.builder()
                                        .isAdmin(true)
                                        .id(userId)
                                        .build())
                        )
                        .build()
        );
    }

    public List<Room> getRooms(String userId) {
        return roomRepository.findAllByIsActiveIsTrueAndUsers_Id(userId);
    }

    public Room enterRoom(ObjectId roomId, String userId) {
        Room room = findRoom(roomId);
        List<User> users = room.getUsers();
        Optional<User> existedUser = users
                .stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
        if (existedUser.isPresent()) {
            return room;
        }

        users.add(User.builder()
                .id(userId)
                .isAdmin(false)
                .build());

        return roomRepository.save(room);
    }

    public Room leaveRoom(ObjectId roomId, String userId) {
        Room room = findRoom(roomId);
        List<User> updatedUsers = room.getUsers()
                .stream()
                .filter(user -> !user.getId().equals(userId))
                .collect(toList());
        room.setUsers(updatedUsers);

        return roomRepository.save(room);
    }

    public Room updateCoordinates(ObjectId roomId, String userId, Coordinates coordinates) {
        Room room = findRoom(roomId);
        List<User> users = room.getUsers();
        users.forEach(user -> {
            if (user.getId().equals(userId)) {
                user.setCoordinates(coordinates);
            }
        });

        return roomRepository.save(room);
    }

    private Room findRoom(ObjectId roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isEmpty()) {
            throw new RoomNotExistException(roomId);
        }

        return roomOptional.get();
    }
}
