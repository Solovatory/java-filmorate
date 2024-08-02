package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MpaService {
    private MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MpaDto> getAllMpa() {
        log.info("Получение списка всех рейтингов из базы");
        return mpaStorage.getMpas().stream().map(MpaMapper::mapToDto).toList();
    }

    public MpaDto getMpaById(int id) {
        log.trace("Получение из базы рейтинга с id = {}", id);
        Optional<Mpa> optionalMpa = mpaStorage.getMpa(id);
        if (optionalMpa.isEmpty()) {
            log.warn("Рейтинг с id = {} не найден в базе", id);
            throw new NotFoundException(String.format("Рейтинга с id = %d не существует", id));
        }
        Mpa mpa = optionalMpa.get();
        log.trace("Из базы получен рейтинг {}", mpa);
        return MpaMapper.mapToDto(mpa);
    }
}
