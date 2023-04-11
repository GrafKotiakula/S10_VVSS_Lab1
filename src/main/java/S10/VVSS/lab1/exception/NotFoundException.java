package S10.VVSS.lab1.exception;

import S10.VVSS.lab1.entities.AbstractEntity;

import java.util.UUID;

public class NotFoundException extends ClientException {
    private static final int codeOffset = 1100;

    protected NotFoundException(String message, int code) {
        super(message, codeOffset + code);
    }

    protected NotFoundException(String clientMessage, String message, int code) {
        super(clientMessage, message, codeOffset + code);
    }

    protected NotFoundException(String clientMessage, String message, int code, Throwable cause) {
        super(clientMessage, message, codeOffset + code, cause);
    }

    protected NotFoundException(String message, int code, Throwable cause) {
        super(message, codeOffset + code, cause);
    }

    public static NotFoundException entityNotFound(UUID id, String entityName) {
        return entityNotFound(id, entityName, null);
    }

    public static NotFoundException entityNotFound(UUID id, String entityName, Throwable cause) {
        return new NotFoundException(String.format("%s with id %s not found", entityName, id), 0, cause);
    }

    public static NotFoundException entityNotFound(UUID id, Class<? extends AbstractEntity> clazz) {
        return entityNotFound(id, clazz.getSimpleName());
    }

    public static NotFoundException entityNotFound(UUID id, Class<? extends AbstractEntity> clazz, Throwable cause) {
        return entityNotFound(id, clazz.getSimpleName(), cause);
    }
}
