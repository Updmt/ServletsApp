package ControllerTests;

import org.example.controller.EventRestControllerV1;
import org.example.model.Event;
import org.example.model.File;
import org.example.model.User;
import org.example.service.EventService;
import org.example.service.impl.UserManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventRestControllerV1Test {

    private static final String USER_ID_HEADER = "USER-ID";
    private static final String EVENT_ID = "1";

    @Mock
    private UserManagementService userManagementService;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventRestControllerV1 eventController;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;

    @BeforeEach
    public void setup() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        mockRequestBody("{}");
    }

    @Test
    public void testDoGetWithUserIdHeader_ReturnsUserEvents() throws IOException {
        when(request.getHeader(USER_ID_HEADER)).thenReturn("1");
        List<Event> events = Arrays.asList(new Event(1L, "2023-10-28", new User(), new File()));
        when(userManagementService.getAllEventsFromUser(1L)).thenReturn(events);

        eventController.doGet(request, response);

        String output = getResponseOutput();
        assertThat(output).contains("\"created_at\":\"2023-10-28\"");
    }

    @Test
    public void testDoGetWithEventIdInBody_ReturnsSingleEvent() throws IOException {
        mockRequestBody("{\"id\":\"" + EVENT_ID + "\"}");

        Event event = new Event(1L, "2023-10-28", new User(), new File());
        when(eventService.get(1L)).thenReturn(event);

        eventController.doGet(request, response);

        String output = getResponseOutput();
        assertThat(output).contains("\"created_at\":\"2023-10-28\"");
    }

    @Test
    public void testDoGetWithoutUserIdAndEventId_ReturnsAllEvents() throws IOException {
        when(request.getHeader(USER_ID_HEADER)).thenReturn(null);

        List<Event> events = Arrays.asList(new Event(1L, "2023-10-28", new User(), new File()));
        when(eventService.getAll()).thenReturn(events);

        eventController.doGet(request, response);

        String output = getResponseOutput();
        assertThat(output).contains("\"created_at\":\"2023-10-28\"");
    }

    private void mockRequestBody(String body) throws IOException {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(body)));
    }

    private String getResponseOutput() {
        return stringWriter.getBuffer().toString();
    }
}
