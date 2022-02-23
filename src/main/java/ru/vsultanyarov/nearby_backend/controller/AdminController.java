package ru.vsultanyarov.nearby_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.vsultanyarov.nearby_backend.mapper.RoomMapper;
import ru.vsultanyarov.nearby_backend.mods.AdminApi;
import ru.vsultanyarov.nearby_backend.mods.dto.Room;
import ru.vsultanyarov.nearby_backend.service.AdminService;

import static org.springframework.http.ResponseEntity.ok;

@Validated
@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {
    private final AdminService adminService;
    private final RoomMapper roomMapper;

    @Override
    public ResponseEntity<Room> roomActivate(String roomId, String userId) {
        return ok(roomMapper.domainRoomToDto(adminService.activateRoom(roomMapper.stringToObjectId(roomId), userId)));
    }

    @Override
    public ResponseEntity<Room> roomDelete(String roomId, String userId) {
        return ok(roomMapper.domainRoomToDto(adminService.deleteRoom(roomMapper.stringToObjectId(roomId), userId)));
    }
}
