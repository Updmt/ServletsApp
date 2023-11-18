package ServiceImplTests;

import org.example.dao.EventDao;
import org.example.model.Event;
import org.example.model.File;
import org.example.model.User;
import org.example.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

    @Mock
    private EventDao eventDao;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    public void testSaveEvent() {
        Event event = new Event(1L, "2023-10-28", new User(), new File());
        event.setCreatedAt("2023-10-27");

        Event savedEvent = new Event(1L, "2023-10-28", new User(), new File());
        savedEvent.setId(1L);
        savedEvent.setCreatedAt("2023-10-27");

        when(eventDao.save(event)).thenReturn(savedEvent);

        Event result = eventService.save(event);

        assertThat(result.getCreatedAt()).isEqualTo(event.getCreatedAt());
    }

    @Test
    public void testGetEventById() {
        Event event = new Event(1L, "2023-10-28", new User(), new File());
        event.setId(1L);
        event.setCreatedAt("2023-10-27");

        when(eventDao.getById(1L)).thenReturn(event);

        Event result = eventService.get(1L);

        assertThat(result.getCreatedAt()).isEqualTo(event.getCreatedAt());
    }

    @Test
    public void testGetAllEvents() {
        List<Event> events = Arrays.asList(
                new Event(1L, "2023-10-27", null, null),
                new Event(2L, "2023-10-28", null, null)
        );
        when(eventDao.getAll()).thenReturn(events);

        List<Event> result = eventService.getAll();
        assertThat(result).isEqualTo(events);
    }
}
