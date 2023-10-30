import org.example.constants.Constants;
import org.example.dto.EventDTO;
import org.example.dto.FileDTO;
import org.example.dto.UserDTO;
import org.example.model.Event;
import org.example.model.File;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DtoMappingTests {

    @Test
    public void testFileDTOFromEntity() {
        File file = new File();
        file.setId(1L);
        file.setFileName("testFile.txt");

        FileDTO fileDTO = FileDTO.fromEntity(file);

        assertNotNull(fileDTO);
        assertEquals(file.getId(), fileDTO.getId());
        assertEquals(file.getFileName(), fileDTO.getFileName());
        assertEquals(Constants.SAVE_PATH + file.getFileName(), fileDTO.getDownloadLink());
    }

    @Test
    public void testEventDTOFromEntity() {
        File file = new File();
        file.setId(1L);
        file.setFileName("testFile.txt");

        Event event = new Event();
        event.setId(1L);
        event.setCreatedAt("testDate");
        event.setFile(file);

        EventDTO eventDTO = EventDTO.fromEntity(event);

        assertNotNull(eventDTO);
        assertEquals(event.getId(), eventDTO.getId());
        assertEquals(event.getCreatedAt(), eventDTO.getCreatedAt());
        assertEquals(file.getFileName(), eventDTO.getFile().getFileName());
    }

    @Test
    public void testUserDTOFromEntity() {
        File file = new File();
        file.setId(1L);
        file.setFileName("testFile.txt");

        Event event = new Event();
        event.setId(1L);
        event.setCreatedAt("testDate");
        event.setFile(file);

        User user = new User(1L, "John", "Doe", Arrays.asList(event));

        UserDTO userDTO = UserDTO.fromEntity(user);

        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getFirstName(), userDTO.getFirstName());
        assertEquals(user.getLastName(), userDTO.getLastName());
        assertEquals(1, userDTO.getEvents().size());
        assertEquals(event.getCreatedAt(), userDTO.getEvents().get(0).getCreatedAt());
    }
}
