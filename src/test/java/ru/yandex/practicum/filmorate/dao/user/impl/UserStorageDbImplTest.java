package ru.yandex.practicum.filmorate.dao.user.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import java.time.LocalDate;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserStorageDbImplTest {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations parameter;
    private UserStorage userStorage;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        userStorage = new UserStorageDbImpl(jdbcTemplate, parameter);
        user1 = new User("petrov@email.ru", "vanya123", "Иван Петров",
                LocalDate.of(1990, 1, 1));
        user2 = new User("livanova@email.ru", "liv4mar123", "Мария Ливанова",
                LocalDate.of(1994, 9, 17));
        user3 = new User("nikitin@email.ru", "sr4nik123", "Сергей Никитин",
                LocalDate.of(2000, 12, 24));
    }

    @DisplayName("Должен создать пользователя")
    @Test
    public void shouldCreateUser() {
        User user3 = new User(1L, "petrov@email.ru", "vanya123", "Иван Петров",
                LocalDate.of(1990, 1, 1), new HashSet<>());
        userStorage.createUser(user1);

        User result = userStorage.getUserById(1L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user3);
    }

    @DisplayName("Должен обновить пользователя")
    @Test
    public void shouldUpdateUser() {
        User userForUpdate = new User(1L, "petrovIvan@email.ru", "vanya789",
                "Иван Петров Александрович", LocalDate.of(1991, 2, 2), new HashSet<>());
        userStorage.createUser(user1);
        userStorage.updateUser(userForUpdate);

        User result = userStorage.getUserById(1L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userForUpdate);
    }

    @DisplayName("Должен вернуть пользователя по id")
    @Test
    public void shouldReturnUserById() {
        User user3 = new User(1L, "petrov@email.ru", "vanya123", "Иван Петров",
                LocalDate.of(1990, 1, 1), new HashSet<>());
        userStorage.createUser(user1);

        User result1 = userStorage.getUserById(1L);
        assertThat(result1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user3);

        User result2 = userStorage.getUserById(57L);
        assertThat(result2)
                .isNull();
    }

    @DisplayName("Должен вернуть всех пользователей")
    @Test
    public void shouldReturnAllUsers() {
        List<User> users = List.of(new User(1,"petrov@email.ru", "vanya123", "Иван Петров",
                        LocalDate.of(1990, 1, 1), new HashSet<>()),
                new User(2,"livanova@email.ru", "liv4mar123", "Мария Ливанова",
                        LocalDate.of(1994, 9, 17), new HashSet<>()),
                new User(3,"nikitin@email.ru", "sr4nik123", "Сергей Никитин",
                        LocalDate.of(2000, 12, 24), new HashSet<>()));

        Collection<User> result1 = userStorage.getAllUsers();
        assertThat(result1)
                .isEmpty();

        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);

        Collection<User> result2 = userStorage.getAllUsers();
        assertThat(result2)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(users);
    }

    @DisplayName("Должен вернуть всех друзей пользователя с id = 1")
    @Test
    public void shouldReturnAllFriendsUser() {
        List<User> users = List.of(new User(3,"nikitin@email.ru", "sr4nik123", "Сергей Никитин",
                LocalDate.of(2000, 12, 24), new HashSet<>()));

        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);
        userStorage.addInFriend(1L, 3L);

        Collection<User> result = userStorage.getFriends(1L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(users);
    }

    @DisplayName("Должен вернуть общих друзей пользователя с id = 1 и пользователя с id = 3")
    @Test
    public void shouldReturnUsersMutualFriends() {
        List<User> users = List.of(new User(2,"livanova@email.ru", "liv4mar123", "Мария Ливанова",
                LocalDate.of(1994, 9, 17), new HashSet<>()));

        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);

        userStorage.addInFriend(1L, 2L);
        userStorage.addInFriend(3L, 2L);

        Collection<User> result = userStorage.getMutualFriends(1L, 3L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(users);
    }

    @DisplayName("Должен добавить пользователя с id = 2 в друзья пользователю с id = 1")
    @Test
    public void shouldAddInFriend() {
        User user = new User(1L, "petrov@email.ru", "vanya123", "Иван Петров",
                LocalDate.of(1990, 1, 1), Set.of(2L));
        userStorage.createUser(user1);
        userStorage.createUser(user2);

        User result = userStorage.addInFriend(1L, 2L);
        assertThat(result)
                .isNotNull()
                .isEqualTo(user);
    }

    @DisplayName("Должен удалить пользователя с id = 1")
    @Test
    public void shouldDeleteUser() {
        userStorage.createUser(user1);
        userStorage.deleteUser(1L);

        User result = userStorage.getUserById(1L);
        assertThat(result)
                .isNull();
    }

    @DisplayName("Должен удалить пользователя с id = 2 из друзей пользователя с id = 1")
    @Test
    public void shouldDeleteFromFriends() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.addInFriend(1L, 2L);

        boolean result1 = userStorage.isExistsFriendship(1L, 2L);
        assertThat(result1)
                .isNotNull()
                .isEqualTo(true);

        userStorage.deleteFromFriends(1L, 2L);
        boolean result2 = userStorage.isExistsFriendship(1L, 2L);
        assertThat(result2)
                .isNotNull()
                .isEqualTo(false);
    }

    @DisplayName("Должен проверить существование id пользователя")
    @Test
    public void shouldCheckForExistenceOfUserId() {
        User user = userStorage.createUser(user1);

        User result1 = userStorage.getUserById(1L);
        assertThat(result1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);

        User result2 = userStorage.getUserById(200L);
        assertThat(result2)
                .isNull();
    }

    @DisplayName("Должен проверить, что пользователь с id = 2 в друзьях у пользователя с id = 1")
    @Test
    public void shouldCheckTheExistenceOfFriendship() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.addInFriend(1L, 2L);

        boolean result = userStorage.isExistsFriendship(1L, 2L);
        assertThat(result)
                .isNotNull()
                .isEqualTo(true);
    }
}