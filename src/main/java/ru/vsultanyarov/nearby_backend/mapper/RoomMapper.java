package ru.vsultanyarov.nearby_backend.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vsultanyarov.nearby_backend.domain.Coordinates;
import ru.vsultanyarov.nearby_backend.domain.Room;
import ru.vsultanyarov.nearby_backend.domain.User;

import java.util.List;


@Mapper
public interface RoomMapper {
    @Mapping(target = "isActive", source = "active")
    ru.vsultanyarov.nearby_backend.mods.dto.Room domainRoomToDto(Room room);

    @Mapping(target = "isActive", source = "active")
    List<ru.vsultanyarov.nearby_backend.mods.dto.Room> domainRoomsToDto(List<Room> rooms);

    @Mapping(target = "lastCoordinates", source = "coordinates")
    @Mapping(target = "isAdmin", source = "admin")
    ru.vsultanyarov.nearby_backend.mods.dto.User userToUser(User user);

    Coordinates dtoCoordinatesToDomain(ru.vsultanyarov.nearby_backend.mods.dto.Coordinates coordinates);

    default String objectIdToString(ObjectId objectId) {
        return objectId.toHexString();
    }

    default ObjectId stringToObjectId(String string) {
        return new ObjectId(string);
    }
}
