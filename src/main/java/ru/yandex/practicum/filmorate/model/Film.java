package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.annotation.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

/**
 * Film.
 */
@Component
@Data
public class Film {
    @EqualsAndHashCode.Exclude
    private int id;

    @NotBlank
    private String name;

    @EqualsAndHashCode.Exclude
    @Length(max = 200)
    private String description;

    @ReleaseDate
    private LocalDate releaseDate;

    @PositiveOrZero
    private int duration;
}
