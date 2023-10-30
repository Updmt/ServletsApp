package org.example.dto;

import com.google.gson.annotations.Expose;
import lombok.*;
import org.example.model.Event;
import org.example.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @Expose
    private Long id;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private List<EventDTO> events;

    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .events(Objects.nonNull(user.getEvents()) ? EventDTO.fromEntityList(user.getEvents()) : Collections.emptyList())
                .build();
    }

    public static List<UserDTO> fromEntityList(List<User> users) {
        return users.stream()
                .map(UserDTO::fromEntity)
                .toList();
    }
}
