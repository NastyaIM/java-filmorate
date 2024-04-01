package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotation.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @NotNull
    private Mpa mpa;

    private LinkedHashSet<Genre> genres;

    public Film(String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
}