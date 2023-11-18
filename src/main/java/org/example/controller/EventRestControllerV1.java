package org.example.controller;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.dao.impl.EventDaoImpl;
import org.example.dto.EventDTO;
import org.example.model.Event;
import org.example.service.EventService;
import org.example.service.impl.EventServiceImpl;
import org.example.service.impl.UserManagementService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.example.util.Util.getDataFromRequest;

@WebServlet("/api/v1/events")
public class EventRestControllerV1 extends HttpServlet {

    private final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private UserManagementService userManagementService;
    private EventService eventService;

    @Override
    public void init() {
        userManagementService = new UserManagementService();
        eventService = new EventServiceImpl(new EventDaoImpl());
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String userId = req.getHeader("USER-ID");
        String body = getDataFromRequest(req);
        Map<String, String> map = null;
        if (!body.isEmpty()) {
            map = gson.fromJson(body, Map.class);
        }

        String eventId = Objects.nonNull(map) ? map.get("id") : null;

        if (Objects.nonNull(userId)) {
            List<Event> usersEventList = userManagementService.getAllEventsFromUser(Long.valueOf(userId));
            List<EventDTO> eventDTOS = EventDTO.fromEntityList(usersEventList);
            String json = gson.toJson(eventDTOS);
            resp.getWriter().write(json);
        }
        if (Objects.nonNull(eventId)) {
            Event event = eventService.get(Long.valueOf(eventId));
            EventDTO eventDTO = EventDTO.fromEntity(event);
            String json = gson.toJson(eventDTO);
            resp.getWriter().write(json);
        }
        if (Objects.isNull(eventId) && Objects.isNull(userId)) {
            List<Event> eventList = eventService.getAll();
            List<EventDTO> eventDTOS = EventDTO.fromEntityList(eventList);
            String json = gson.toJson(eventDTOS);
            resp.getWriter().write(json);
        }
    }
}
