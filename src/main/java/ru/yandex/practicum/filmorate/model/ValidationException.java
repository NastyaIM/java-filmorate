package ru.yandex.practicum.filmorate.model;

import org.apache.tomcat.util.digester.RulesBase;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}