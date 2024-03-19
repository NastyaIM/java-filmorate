package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    private int id;

    @NotBlank
    @Email
    @EqualsAndHashCode.Include
    private String email;

    @NotBlank
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();
}