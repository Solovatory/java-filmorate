package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dal.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class MpaDbStorageTests {

    private final MpaDbStorage mpaStorage;

    @Test
    public void testGetMpas() {
        Collection<Mpa> mpas = mpaStorage.getMpas();
        assertThat(mpas).isNotEmpty();
    }

    @Test
    public void testGetMpaById() {
        Optional<Mpa> retrievedMpa = mpaStorage.getMpa(1);

        assertThat(retrievedMpa).isPresent();
        assertThat(retrievedMpa.get().getName()).isEqualTo("G");
    }
}
