package ru.vsultanyarov.nearby_backend.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.vsultanyarov.nearby_backend.domain.Room;

import java.util.List;

public interface RoomRepository extends MongoRepository<Room, ObjectId> {
    List<Room> findAllByIsActiveIsTrueAndUsers_Id(String id);
}
