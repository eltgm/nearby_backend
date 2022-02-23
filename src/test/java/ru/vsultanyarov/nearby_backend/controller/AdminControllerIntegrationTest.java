package ru.vsultanyarov.nearby_backend.controller;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.vsultanyarov.nearby_backend.domain.Coordinates;
import ru.vsultanyarov.nearby_backend.domain.Room;
import ru.vsultanyarov.nearby_backend.domain.User;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "spring.mongodb.embedded.version=3.0.1")
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
class AdminControllerIntegrationTest {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        List<Room> testingRooms = new ArrayList<>();
        Coordinates user1Coordinates = new Coordinates(15.0, 16.1);
        Coordinates user2Coordinates = new Coordinates(35.9, 32.2);
        User user1 = new User("u1", false, user1Coordinates);
        User user2 = new User("u2", true, user2Coordinates);
        User user3 = new User("u3", true, null);
        Room room1 = Room.builder()
                .id(new ObjectId("6215fc4644456e66e61f91d3"))
                .user(user1)
                .user(user2)
                .isActive(false)
                .build();
        Room room2 = Room.builder()
                .id(new ObjectId("6215fc4644456e66e61f91d4"))
                .user(user1)
                .user(user3)
                .isActive(true)
                .build();
        Room room3 = Room.builder()
                .isActive(false)
                .build();
        testingRooms.add(room1);
        testingRooms.add(room2);
        testingRooms.add(room3);

        mongoTemplate.createCollection("room");
        mongoTemplate.insert(testingRooms, "room");
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection("room");
    }

    @Test
    @DisplayName("Success activate room")
    void roomActivate() throws Exception {
        mockMvc.perform(
                        post("/admin/{roomId}", "6215fc4644456e66e61f91d3")
                                .param("userId", "u2")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    @DisplayName("Trying to activate already active room")
    void activateActiveRoom() throws Exception {
        mockMvc.perform(
                        post("/admin/{roomId}", "6215fc4644456e66e61f91d4")
                                .param("userId", "u3")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.applicationErrorCode").value("roomAlreadyActive"));
    }

    @Test
    @DisplayName("Trying to activate non existed active")
    void activateNonExistedRoom() throws Exception {
        mockMvc.perform(
                        post("/admin/{roomId}", "6215fc4644456e66e61f91d6")
                                .param("userId", "u3")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.applicationErrorCode").value("roomNotExist"));
    }

    @Test
    @DisplayName("Trying activate room by non admin")
    void roomActivateByNonAdmin() throws Exception {
        mockMvc.perform(
                        post("/admin/{roomId}", "6215fc4644456e66e61f91d3")
                                .param("userId", "u1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.applicationErrorCode").value("userNotAdmin"));
    }

    @Test
    @DisplayName("Success delete room")
    void deleteRoom() throws Exception {
        mockMvc.perform(
                        delete("/admin/{roomId}", "6215fc4644456e66e61f91d4")
                                .param("userId", "u3")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive").value(false))
                .andExpect(jsonPath("$.users").isEmpty());
    }

    @Test
    @DisplayName("Trying to delete already deleted room")
    void deleteDeletedRoom() throws Exception {
        mockMvc.perform(
                        delete("/admin/{roomId}", "6215fc4644456e66e61f91d3")
                                .param("userId", "u1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.applicationErrorCode").value("roomAlreadyDeactivated"));
    }

    @Test
    @DisplayName("Trying to delete room by non admin")
    void deleteRoomByNotAdmin() throws Exception {
        mockMvc.perform(
                        delete("/admin/{roomId}", "6215fc4644456e66e61f91d4")
                                .param("userId", "u1")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.applicationErrorCode").value("userNotAdmin"));
    }

    @Test
    @DisplayName("Trying to delete room by non existed user")
    void deleteRoomByNonExistedUser() throws Exception {
        mockMvc.perform(
                        delete("/admin/{roomId}", "6215fc4644456e66e61f91d4")
                                .param("userId", "u5")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.applicationErrorCode").value("userNotFound"));
    }
}
