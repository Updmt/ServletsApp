package org.example.service.impl;

import org.example.constants.Constants;
import org.example.dto.FileDTO;
import org.example.model.Event;
import org.example.model.File;
import org.example.model.User;
import org.example.service.EventService;
import org.example.service.FileService;
import org.example.service.UserService;

import java.time.Instant;
import java.util.List;

public class UserManagementService {

    private final UserService userService = new UserServiceImpl();
    private final EventService eventService = new EventServiceImpl();
    private final FileService fileService = new FileServiceImpl();


    public File createFileAndEvent(Long userId, String text, String fileName) {
        User user = userService.get(userId);

        File file = new File();
        file.setText(text);
        file.setFileName(fileName);

        Event event = new Event();
        event.setCreatedAt(Instant.now().toString());
        event.setUser(user);
        event.setFile(file);

        fileService.save(file);
        eventService.save(event);
        return file;
    }

    public List<FileDTO> getAllFilesDTOFromUser(Long userId) {
        User user = userService.get(userId);
        List<Event> userEventList = user.getEvents();
        List<File> fileList = userEventList.stream()
                .map(Event::getFile)
                .toList();
        return fileList.stream()
                .map(FileDTO::fromEntity)
                .toList();
    }

    public List<Event> getAllEventsFromUser(Long userId) {
        User user = userService.get(userId);
        return user.getEvents();
    }
}
