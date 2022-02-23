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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "spring.mongodb.embedded.version=3.0.1")
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@SpringBootTest
class UserControllerIntegrationTest {
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
                .isActive(true)
                .build();
        Room room2 = Room.builder()
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
    @DisplayName("Success create room")
    void createRoom() throws Exception {
        mockMvc.perform(
                        post("/user/room")
                                .param("userId", "u4")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive").value(false))
                .andExpect(jsonPath("$.users[0].id").value("u4"))
                .andExpect(jsonPath("$.users[0].isAdmin").value(true));
    }

    @Test
    @DisplayName("Success get room")
    void getRoom() throws Exception {
        mockMvc.perform(
                        get("/user/room")
                                .param("userId", "u1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].isActive").value(true));
    }

    @Test
    @DisplayName("Trying get room for non existed user")
    void getNotExistedRoom() throws Exception {
        mockMvc.perform(
                        get("/user/room")
                                .param("userId", "u5")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[*]").isEmpty());
    }

    @Test
    @DisplayName("Success enter room")
    void enterRoom() throws Exception {
        mockMvc.perform(
                        get("/user/room/{roomId}/enter", "6215fc4644456e66e61f91d3")
                                .param("userId", "u5")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[2].id").value("u5"))
                .andExpect(jsonPath("$.users[2].isAdmin").value(false));
    }

    @Test
    @DisplayName("Trying enter non existed room")
    void enterNonExistedRoom() throws Exception {
        mockMvc.perform(
                        get("/user/room/{roomId}/enter", "6215fc4644456e66e61f91d4")
                                .param("userId", "u5")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.applicationErrorCode").value("roomNotExist"));
    }

    @Test
    @DisplayName("Success leave room")
    void leaveRoom() throws Exception {
        mockMvc.perform(
                        get("/user/room/{roomId}/leave", "6215fc4644456e66e61f91d3")
                                .param("userId", "u1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users.length()").value(1));
    }

    @Test
    @DisplayName("Trying leave non existed room")
    void leaveNonExistedRoom() throws Exception {
        mockMvc.perform(
                        get("/user/room/{roomId}/leave", "6215fc4644456e66e61f91d4")
                                .param("userId", "u1")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.applicationErrorCode").value("roomNotExist"));
    }

    @Test
    @DisplayName("Success update room")
    void updateCoordinates() throws Exception {
        Map<String, Double> expectedCoordinates = new LinkedHashMap<>();
        expectedCoordinates.put("longitude", 99.64);
        expectedCoordinates.put("latitude", 99.76);

        mockMvc.perform(
                        post("/user/room/{roomId}/update", "6215fc4644456e66e61f91d3")
                                .contentType(APPLICATION_JSON)
                                .content("{\n" +
                                        "  \"longitude\": 99.64,\n" +
                                        "  \"latitude\": 99.76\n" +
                                        "}")
                                .param("userId", "u1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].id").value("u1"))
                .andExpect(jsonPath("$.users[0].lastCoordinates").value(expectedCoordinates));
    }

    @Test
    @DisplayName("Trying update non existed room")
    void updateCoordinatesInNonExistedRoom() throws Exception {
        Map<String, Double> expectedCoordinates = new LinkedHashMap<>();
        expectedCoordinates.put("longitude", 99.64);
        expectedCoordinates.put("latitude", 99.76);

        mockMvc.perform(
                        post("/user/room/{roomId}/update", "6215fc4644456e66e61f91d4")
                                .contentType(APPLICATION_JSON)
                                .content("{\n" +
                                        "  \"longitude\": 99.64,\n" +
                                        "  \"latitude\": 99.76\n" +
                                        "}")
                                .param("userId", "u1")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.applicationErrorCode").value("roomNotExist"));
    }
}