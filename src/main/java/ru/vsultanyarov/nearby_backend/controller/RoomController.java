package ru.vsultanyarov.nearby_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/room")
@RestController
public class RoomController {

    @PostMapping("/create")
    public ResponseEntity<UUID> createRoom(String username, String phoneInfo) {
        return ResponseEntity.ok(UUID.randomUUID());
    }
}
