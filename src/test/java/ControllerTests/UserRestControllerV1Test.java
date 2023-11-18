package ControllerTests;

import org.example.controller.UserRestControllerV1;
import org.example.model.User;
import org.example.service.UserService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRestControllerV1Test {

    private static final String FIRST_NAME_JOHN = "John";
    private static final String LAST_NAME_DOE = "Doe";

    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestControllerV1 userController;

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
    }

    @Test
    public void testDoPost_CreatesNewUser() throws IOException {
        String jsonBody = "{\"first_name\":\"" + FIRST_NAME_JOHN + "\", \"last_name\":\"" + LAST_NAME_DOE + "\"}";
        mockRequestBody(jsonBody);

        User user = new User();
        user.setFirstName(FIRST_NAME_JOHN);
        user.setLastName(LAST_NAME_DOE);
        User savedUser = new User(1L, FIRST_NAME_JOHN, LAST_NAME_DOE, null);
        when(userService.save(user)).thenReturn(savedUser);

        userController.doPost(request, response);

        String output = getResponseOutput();
        assertThat(output).contains("\"first_name\":\"" + FIRST_NAME_JOHN + "\"");
        assertThat(output).contains("\"last_name\":\"" + LAST_NAME_DOE + "\"");
    }

    @Test
    public void testDoGetWithUserIdHeader_ReturnsSingleUser() throws IOException {
        String mockUserId = "1";
        when(request.getHeader("USER-ID")).thenReturn(mockUserId);

        User user = new User(1L, FIRST_NAME_JOHN, LAST_NAME_DOE, null);
        when(userService.get(Long.valueOf(mockUserId))).thenReturn(user);

        userController.doGet(request, response);

        String output = getResponseOutput();
        assertThat(output).contains("\"first_name\":\"" + FIRST_NAME_JOHN + "\"");
        assertThat(output).contains("\"last_name\":\"" + LAST_NAME_DOE + "\"");
    }

    @Test
    public void testDoGetWithoutUserIdHeader_ReturnsAllUsers() throws IOException {
        when(request.getHeader("USER-ID")).thenReturn(null);

        List<User> users = Arrays.asList(
                new User(1L, FIRST_NAME_JOHN, LAST_NAME_DOE, null),
                new User(2L, "Jane", "Smith", null)
        );
        when(userService.getAll()).thenReturn(users);

        userController.doGet(request, response);

        String output = getResponseOutput();
        assertThat(output).contains("\"first_name\":\"" + FIRST_NAME_JOHN + "\"");
        assertThat(output).contains("\"last_name\":\"" + LAST_NAME_DOE + "\"");
        assertThat(output).contains("\"first_name\":\"Jane\"");
        assertThat(output).contains("\"last_name\":\"Smith\"");
    }

    @Test
    public void testDoPut_UpdatesExistingUser() throws IOException {
        String jsonBody = "{\"id\":\"1\", \"first_name\":\"UpdatedJohn\", \"last_name\":\"UpdatedDoe\"}";
        mockRequestBody(jsonBody);

        User existingUser = new User(1L, FIRST_NAME_JOHN, LAST_NAME_DOE, null);
        when(userService.get(1L)).thenReturn(existingUser);

        User updatedUser = new User(1L, "UpdatedJohn", "UpdatedDoe", null);
        when(userService.update(existingUser)).thenReturn(updatedUser);

        userController.doPut(request, response);

        String output = getResponseOutput();
        assertThat(output).contains("\"first_name\":\"UpdatedJohn\"");
        assertThat(output).contains("\"last_name\":\"UpdatedDoe\"");
    }

    @Test
    public void testDoDelete_DeletesUserById() throws IOException {
        String jsonBody = "{\"id\":\"1\"}";
        mockRequestBody(jsonBody);

        User userToDelete = new User(1L, FIRST_NAME_JOHN, LAST_NAME_DOE, null);
        when(userService.get(1L)).thenReturn(userToDelete);

        doNothing().when(userService).delete(1L);

        userController.doDelete(request, response);

        String output = getResponseOutput();
        assertThat(output).contains("\"first_name\":\"" + FIRST_NAME_JOHN + "\"");
        assertThat(output).contains("\"last_name\":\"" + LAST_NAME_DOE + "\"");
    }

    private void mockRequestBody(String body) throws IOException {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(body)));
    }

    private String getResponseOutput() {
        return stringWriter.getBuffer().toString();
    }
}
