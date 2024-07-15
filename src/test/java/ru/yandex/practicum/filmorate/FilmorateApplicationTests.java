package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    @Test
    public void createNewUserWithWrongEmailShouldThrowException() {
        User user1 = User.builder()
                .name("Kate Olsen")
                .login("KO")
                .email("KOyandex.ru")
                .birthday(LocalDate.of(1980, 11, 12))
                .build();
        User user2 = User.builder()
                .name("Kate Olsen")
                .login("KO")
                .email("")
                .birthday(LocalDate.of(1980, 11, 12))
                .build();
        User user3 = User.builder()
                .name("Kate Olsen")
                .login(null)
                .email("")
                .birthday(LocalDate.of(1980, 11, 12))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.addUser(user1), "При отсутствии " +
                "@ в поле Email исключение не выбрасыватся");
        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.addUser(user2), "При пустом " +
                " поле Email исключение не выбрасыватся");
        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.addUser(user3), "Если поле " +
                "Email равно null исключение не выбрасывается");
    }

    @Test
    public void createNewUserWithWrongLoginShouldThrowException() {
        User user1 = User.builder()
                .name("Kate Olsen")
                .login("")
                .email("KO@yandex.ru")
                .birthday(LocalDate.of(1980, 11, 12))
                .build();
        User user2 = User.builder()
                .name("Kate Olsen")
                .login("Kate Olsen")
                .email("KO@yandex.ru")
                .birthday(LocalDate.of(1980, 11, 12))
                .build();
        User user3 = User.builder()
                .name("Kate Olsen")
                .login(null)
                .email("KO@yandex.ru")
                .birthday(LocalDate.of(1980, 11, 12))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.addUser(user1), "При пустом " +
                "Логине исключение не выбрасыватся");
        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.addUser(user2), "Если в логине " +
                "есть пробелы исключение не выбрасыватся");
        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.addUser(user3), "При значении" +
                "Логина null исключение не выбрасыватся");
    }

    @Test
    public void createNewUserWithWrongBirthdayShouldThrowException() {
        User user1 = User.builder()
                .name("Kate Olsen")
                .login("KO")
                .email("KO@yandex.ru")
                .birthday(LocalDate.of(2034, 11, 12))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> inMemoryUserStorage.addUser(user1), "Если дата " +
                "рождения в будущем исключение не выбрасыватся");
    }

    @Test
    public void createNewFilmWithWrongNameShouldThrowException() {
        Film film1 = Film.builder()
                .name("")
                .description("Good choice")
                .releaseDate(LocalDate.of(2003, 4, 15))
                .duration(120)
                .build();
        Film film2 = Film.builder()
                .name(null)
                .description("Good choice")
                .releaseDate(LocalDate.of(2003, 4, 15))
                .duration(120)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> inMemoryFilmStorage.addFilm(film1), "При пустом " +
                "имени исключение не выбрасывается");
        Assertions.assertThrows(ValidationException.class, () -> inMemoryFilmStorage.addFilm(film2), "При имени " +
                "равном null исключение не выбрасывается");
    }

    @Test
    public void createNewFilmWithWrongDescriptionShouldThrowException() {
        Film film1 = Film.builder()
                .name("")
                .description("«Тачки» — серия компьютерно-анимационных фильмов и медиафраншиза Disney, события " +
                        "которой разворачиваются в мире, населённом антропоморфными автомобилями. Начало франшизы " +
                        "было положено с выпуска одноимённого мультфильма в 2006 году, снятого компанией Pixar и " +
                        "выпущенного Walt Disney Pictures. В 2011 году последовало продолжение. Третий мультфильм, " +
                        "«Тачки 3», вышел в 2017 году. Студией Disneytoon было выпущено два спин-оффа — «Самолёты» " +
                        "(2013) и «Самолёты: Огонь и вода» (2014).")
                .releaseDate(LocalDate.of(2003, 4, 15))
                .duration(120)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> inMemoryFilmStorage.addFilm(film1), "При длине " +
                "описания больше 200 знаков исключение не выбрасывается");
    }

    @Test
    public void createNewFilmWithWrongReleaseDateShouldThrowException() {
        Film film1 = Film.builder()
                .name("Cars")
                .description("Good choice")
                .releaseDate(LocalDate.of(1736, 4, 15))
                .duration(120)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> inMemoryFilmStorage.addFilm(film1), "При " +
                "неправильной дате релиза исключение не выбрасывается");
    }

    @Test
    public void createNewFilmWithWrongDurationShouldThrowException() {
        Film film1 = Film.builder()
                .name("Cars")
                .description("Good choice")
                .releaseDate(LocalDate.of(2003, 4, 15))
                .duration(-120)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> inMemoryFilmStorage.addFilm(film1), "При " +
                "отрицательной продолжительности исключение не выбрасывается");
    }
}
