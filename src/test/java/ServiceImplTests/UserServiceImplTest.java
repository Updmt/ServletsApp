package ServiceImplTests;

import org.example.dao.UserDao;
import org.example.model.User;
import org.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testSaveUser() {
        User user = new User(null, "John", "Doe", null);

        when(userDao.save(user)).thenReturn(new User(1L, "John", "Doe", null));
        User result = userService.save(user);

        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user.getLastName());

    }

    @Test
    public void testGetUserById() {
        User user = new User(1L, "John", "Doe", null);
        when(userDao.getById(1L)).thenReturn(user);

        User result = userService.get(1L);
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user.getLastName());
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User(1L, "abc", "abc", null),
                new User(2L, "def", "def", null)
        );
        when(userDao.getAll()).thenReturn(users);

        List<User> result = userService.getAll();
        assertThat(result).isEqualTo(users);
    }

    @Test
    public void testUpdateUser() {
        User oldUser = new User(1L, "John", "Doe", null);
        User updatedUser = new User(1L, "newJohn", "newDoe", null);
        when(userDao.update(oldUser)).thenReturn(updatedUser);

        User result = userService.update(oldUser);
        assertThat(result.getFirstName()).isEqualTo(updatedUser.getFirstName());
        assertThat(result.getLastName()).isEqualTo(updatedUser.getLastName());
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(userDao).deleteById(1L);

        userService.delete(1L);

        verify(userDao, times(1)).deleteById(1L);
    }
}
