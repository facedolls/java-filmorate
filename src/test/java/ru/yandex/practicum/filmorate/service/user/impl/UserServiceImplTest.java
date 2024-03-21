package ru.yandex.practicum.filmorate.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.yandex.practicum.filmorate.dao.user.impl.UserStorageDbImpl;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {
    private UserService userService;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations parameter;
    private UserStorage userStorage;

    @BeforeEach
    public void setUp() {
        userStorage = new UserStorageDbImpl(jdbcTemplate, parameter);
        userService = new UserServiceImpl(userStorage);
    }

    @DisplayName("Не должен найти пользователя по id, которого нет")
    @Test
    public void shouldNotFindUserId() {
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(55L)
        );
        assertEquals("User with id=55 not found", exception.getMessage());
    }

    @DisplayName("Не должен обновить пользователя с несуществующим id")
    @Test
    public void shouldNotUpdateUser() {
        User user = new User(55,"livanova@email.ru", "liv4mar123", "Мария Ливанова",
                LocalDate.of(1994, 9, 17));
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(user)
        );
        assertEquals("User with id=55 not found", exception.getMessage());
    }

    @DisplayName("Не должен удалить пользователя с несуществующим id")
    @Test
    public void shouldNotDeleteUser() {
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(55L)
        );
        assertEquals("User with id=55 not found", exception.getMessage());
    }
}