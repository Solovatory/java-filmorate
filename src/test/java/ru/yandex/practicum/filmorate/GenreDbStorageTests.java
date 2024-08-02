package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dal.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class GenreDbStorageTests {

    private final GenreDbStorage genreStorage;

    @Test
    public void testGetGenres() {
        Collection<Genre> genres = genreStorage.getGenres();
        assertThat(genres).isNotEmpty();
    }

    @Test
    public void testGetGenreById() {
        Optional<Genre> retrievedGenre = genreStorage.getGenre(1L);
        assertThat(retrievedGenre).isPresent();
        assertThat(retrievedGenre.get().getName()).isEqualTo("Комедия");
    }
}
