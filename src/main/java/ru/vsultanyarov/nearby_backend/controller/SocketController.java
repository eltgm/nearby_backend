package ru.vsultanyarov.nearby_backend.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {
    @MessageMapping("/activate/{roomId}")
    @SendTo("/room/activation/{roomId}")
    public String greeting(@DestinationVariable String roomId) {
        return roomId;
    }
}
