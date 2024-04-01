package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
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

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}