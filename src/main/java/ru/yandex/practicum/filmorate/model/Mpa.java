package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {
    @EqualsAndHashCode.Exclude
    private int id;

    @NotBlank
    private String name;

    public Mpa(int id) {
        this.id = id;
    }
}
