package ru.vsultanyarov.nearby_backend.domain;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    private ObjectId id;
    private boolean isActive;
    @Singular
    private List<User> users;
}
