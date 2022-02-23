package ru.vsultanyarov.nearby_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.vsultanyarov.nearby_backend.mapper.RoomMapper;
import ru.vsultanyarov.nearby_backend.mods.UserApi;
import ru.vsultanyarov.nearby_backend.mods.dto.Coordinates;
import ru.vsultanyarov.nearby_backend.mods.dto.Room;
import ru.vsultanyarov.nearby_backend.service.UserService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Validated
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;
    private final RoomMapper roomMapper;

    @Override
    public ResponseEntity<Room> createRoom(String userId) {
        return ok(roomMapper.domainRoomToDto(userService.createRoom(userId)));
    }

    @Override
    public ResponseEntity<List<Room>> getRooms(String userId) {
        return ok(roomMapper.domainRoomsToDto(userService.getRooms(userId)));
    }

    @Override
    public ResponseEntity<Room> enterRoom(String roomId, String userId) {
        return ok(
                roomMapper.domainRoomToDto(
                        userService.enterRoom(roomMapper.stringToObjectId(roomId), userId)
                )
        );
    }

    @Override
    public ResponseEntity<Room> leaveRoom(String roomId, String userId) {
        return ok(
                roomMapper.domainRoomToDto(
                        userService.leaveRoom(roomMapper.stringToObjectId(roomId), userId)
                )
        );
    }

    @Override
    public ResponseEntity<Room> updateCoordinates(String roomId, String userId, Coordinates coordinates) {
        return ok(
                roomMapper.domainRoomToDto(
                        userService.updateCoordinates(
                                roomMapper.stringToObjectId(roomId),
                                userId,
                                roomMapper.dtoCoordinatesToDomain(coordinates)
                        )
                )
        );
    }
}
