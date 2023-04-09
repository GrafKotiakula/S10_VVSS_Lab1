package S10.VVSS.lab1.exception;

import S10.VVSS.lab1.database.AbstractEntity;

import java.util.UUID;

public class NotFoundError extends ClientError{
    private static final int codeOffset = 1100;

    protected NotFoundError(String message, int code) {
        super(message, codeOffset + code);
    }

    protected NotFoundError(String message, String clientMessage, int code) {
        super(message, clientMessage, codeOffset + code);
    }

    protected NotFoundError(String message, String clientMessage, int code, Throwable cause) {
        super(message, clientMessage, codeOffset + code, cause);
    }

    protected NotFoundError(String message, int code, Throwable cause) {
        super(message, codeOffset + code, cause);
    }

    public static NotFoundError entityNotFound(UUID id, String entityName) {
        return new NotFoundError(String.format("%s with id %s not found", entityName, id), 0);
    }

    public static NotFoundError entityNotFound(UUID id, String entityName, Throwable cause) {
        return new NotFoundError(String.format("%s with id %s not found", entityName, id), 0, cause);
    }

    public static NotFoundError entityNotFound(UUID id, Class<? extends AbstractEntity> clazz) {
        return entityNotFound(id, clazz.getSimpleName());
    }

    public static NotFoundError entityNotFound(UUID id, Class<? extends AbstractEntity> clazz, Throwable cause) {
        return entityNotFound(id, clazz.getSimpleName(), cause);
    }
}
