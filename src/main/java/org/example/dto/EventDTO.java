package org.example.dto;


import com.google.gson.annotations.Expose;
import lombok.*;
import org.example.model.Event;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {

    @Expose
    private Long id;
    @Expose
    private String createdAt;
    @Expose
    private FileDTO file;

    public static EventDTO fromEntity(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .createdAt(event.getCreatedAt())
                .file(FileDTO.fromEntity(event.getFile()))
                .build();
    }

    public static List<EventDTO> fromEntityList(List<Event> events) {
        return events.stream()
                .map(EventDTO::fromEntity)
                .toList();
    }
}
