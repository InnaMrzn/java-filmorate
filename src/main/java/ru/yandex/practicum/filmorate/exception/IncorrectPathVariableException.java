package ru.yandex.practicum.filmorate.exception;

public class IncorrectPathVariableException extends RuntimeException {
    private String pathVariable;

    public IncorrectPathVariableException(String message, String pathVariable) {
        super(message);
        this.pathVariable = pathVariable;
    }

    public String getPathVariable() {
        return pathVariable;
    }

}
