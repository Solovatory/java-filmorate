package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dal.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.dal.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.dal.user.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmStorage;
    private final MpaDbStorage mpaStorage;
    private final GenreDbStorage genreStorage;
    private User testUser;
    private Film testFilm;
    private Genre testGenre;
    private final String testFilmName = "Test Film";
    private final String updatedFilmName = "Updated Test Film";
    private final String updatedFilmDescription = "Updated description";

    @BeforeEach
    void setUp() {
        Mpa testMpa = new Mpa();
        testMpa.setId(1);
        testGenre = new Genre();
        testGenre.setId(1);

        Set<Genre> genres = new HashSet<>();
        genres.add(testGenre);

        testFilm = new Film();
        testFilm.setName(testFilmName);
        testFilm.setDescription("This is a test film");
        testFilm.setReleaseDate(LocalDate.of(2020, 1, 1));
        testFilm.setDuration(120);
        testFilm.setMpa(testMpa);
        testFilm.setGenres(genres);

        testUser = new User();
        testUser.setEmail("sss@mail.ru");
        testUser.setLogin("Login");
        testUser.setName("Test User");
        testUser.setBirthday(LocalDate.of(1997, 4, 12));
        userDbStorage.addUser(testUser);
    }

    @Test
    public void testAddFilm() {
        Film addedFilm = filmStorage.addFilm(testFilm);
        assertThat(addedFilm.getId()).isNotNull();
        assertThat(addedFilm.getName()).isEqualTo(testFilmName);
        assertThat(addedFilm.getMpa().getName()).isEqualTo("G");
        assertThat(addedFilm.getGenres()).contains(testGenre);
    }

    @Test
    public void testGetFilmById() {
        Film addedFilm = filmStorage.addFilm(testFilm);
        Film retrievedFilm = filmStorage.getFilm(addedFilm.getId());

        assertThat(retrievedFilm).isNotNull();
        assertThat(retrievedFilm.getId()).isEqualTo(addedFilm.getId());
        assertThat(retrievedFilm.getName()).isEqualTo(testFilmName);
        assertThat(retrievedFilm.getMpa().getName()).isEqualTo("G");
        assertThat(retrievedFilm.getGenres()).contains(testGenre);
    }

    @Test
    public void testUpdateFilm() {
        Film addedFilm = filmStorage.addFilm(testFilm);
        addedFilm.setName(updatedFilmName);
        addedFilm.setDescription("Updated description");

        Film updatedFilm = filmStorage.updateFilm(addedFilm);

        assertThat(updatedFilm.getName()).isEqualTo(updatedFilmName);
        assertThat(updatedFilm.getDescription()).isEqualTo(updatedFilmDescription);

        Film retrievedFilm = filmStorage.getFilm(updatedFilm.getId());
        assertThat(retrievedFilm.getName()).isEqualTo(updatedFilmName);
        assertThat(retrievedFilm.getDescription()).isEqualTo(updatedFilmDescription);
    }

    @Test
    public void testGetFilms() {
        filmStorage.addFilm(testFilm);
        List<Film> films = filmStorage.getFilms();
        assertThat(films).isNotEmpty();
    }

    @Test
    public void testAddAndRemoveLike() {
        Film addedFilm = filmStorage.addFilm(testFilm);
        Long filmId = addedFilm.getId();

        filmStorage.addLike(testUser.getId(), filmId);
        Film likedFilm = filmStorage.getFilm(filmId);
        assertThat(likedFilm.getLikes()).isEqualTo(1);

        filmStorage.removeLike(testUser.getId(), filmId);
        Film unlikedFilm = filmStorage.getFilm(filmId);
        assertThat(unlikedFilm.getLikes()).isEqualTo(0);
    }

    @Test
    public void testGetPopularFilms() {
        filmStorage.addFilm(testFilm);
        List<Film> popularFilms = filmStorage.getPopularFilms(1);
        assertThat(popularFilms).isNotEmpty();
    }
}
