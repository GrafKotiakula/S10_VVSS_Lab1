package S10.VVSS.lab1.exception;

import S10.VVSS.lab1.entities.AbstractEntity;

public class UnprocessableEntityException extends ClientException {
    private static final int codeOffset = 1200;

    protected UnprocessableEntityException(String message, int code) {
        super(message, codeOffset + code);
    }

    protected UnprocessableEntityException(String clientMessage, String message, int code) {
        super(clientMessage, message, codeOffset + code);
    }

    protected UnprocessableEntityException(String clientMessage, String message, int code, Throwable cause) {
        super(clientMessage, message, codeOffset + code, cause);
    }

    protected UnprocessableEntityException(String message, int code, Throwable cause) {
        super(message, codeOffset + code, cause);
    }


    public static UnprocessableEntityException nullValue(String parameter, String entityName, Throwable cause) {
        return new UnprocessableEntityException(String.format("%s must be presented and must not be null", parameter),
                String.format("%s#%s is null", entityName, parameter), 0, cause);
    }
    public static UnprocessableEntityException nullValue(String parameter, String entityName) {
        return nullValue(parameter, entityName, null);
    }
    public static UnprocessableEntityException nullValue(String parameter, Class<? extends AbstractEntity> entity) {
        return nullValue(parameter, entity.getSimpleName());
    }
    public static UnprocessableEntityException nullValue(String parameter, Class<? extends AbstractEntity> entity,
                                                         Throwable cause) {
        return nullValue(parameter, entity.getSimpleName(), cause);
    }


    public static UnprocessableEntityException usernameInvalidCharacters(){
        return usernameInvalidCharacters(null);
    }
    public static UnprocessableEntityException usernameInvalidCharacters(Throwable cause){
        return new UnprocessableEntityException(
                "username can contain latin letters, digits, dashes and underscores only",
                "Username contains invalid symbols", 1, cause);
    }


    public static UnprocessableEntityException passwordNoRequiredSymbols() {
        return passwordNoRequiredSymbols(null);
    }
    public static UnprocessableEntityException passwordNoRequiredSymbols(Throwable cause) {
        return new UnprocessableEntityException(
                "password must contain digits, lowercased and uppercased latin letters",
                "Password does not contain required symbols", 2, cause);
    }


    public static UnprocessableEntityException duplicatedField(String parameter, String value, String entityName){
        return duplicatedField(parameter, entityName, value, null);
    }
    public static UnprocessableEntityException duplicatedField(String parameter, String value, String entityName,
                                                               Throwable cause){
        return new UnprocessableEntityException(String.format("%s '%s' is already used", parameter, value),
                String.format("%s#%s = %s is duplicated", entityName, parameter, value), 3, cause);
    }
    public static UnprocessableEntityException duplicatedField(String parameter, String value,
                                                               Class<? extends AbstractEntity> entity){
        return duplicatedField(parameter, value, entity.getSimpleName());
    }
    public static UnprocessableEntityException duplicatedField(String parameter, String value,
                                                               Class<? extends AbstractEntity> entity, Throwable cause){
        return duplicatedField(parameter, value, entity.getSimpleName(), cause);
    }


    public static UnprocessableEntityException stringTooShort(String parameter, int minLength, String entityName){
        return stringTooShort(parameter, minLength, entityName, null);
    }
    public static UnprocessableEntityException stringTooShort(String parameter, int minLength, String entityName,
                                                              Throwable cause){
        return new UnprocessableEntityException(String.format("%s must not be shorter than %d", parameter, minLength),
                String.format("%s#%s is too short", entityName, parameter), 4, cause);
    }
    public static UnprocessableEntityException stringTooShort(String parameter, int minLength,
                                                              Class<? extends AbstractEntity> entity){
        return stringTooShort(parameter, minLength, entity.getSimpleName());
    }
    public static UnprocessableEntityException stringTooShort(Class<? extends AbstractEntity> entity, String parameter,
                                                              int minLength, Throwable cause){
        return stringTooShort(parameter, minLength, entity.getSimpleName(), cause);
    }


    public static UnprocessableEntityException stringTooLong(String parameter, int maxLength, String entityName){
        return stringTooLong(parameter, maxLength, entityName, null);
    }
    public static UnprocessableEntityException stringTooLong(String parameter, int maxLength, String entityName,
                                                             Throwable cause){
        return new UnprocessableEntityException(String.format("%s must not be longer than %d", parameter, maxLength),
                String.format("%s#%s is too long", entityName, parameter), 5, cause);
    }
    public static UnprocessableEntityException stringTooLong(String parameter, int maxLength,
                                                             Class<? extends AbstractEntity> entity){
        return stringTooLong(parameter, maxLength, entity.getSimpleName());
    }
    public static UnprocessableEntityException stringTooLong(String parameter, int maxLength,
                                                             Class<? extends AbstractEntity> entity, Throwable cause){
        return stringTooLong(parameter, maxLength, entity.getSimpleName(), cause);
    }


    public static UnprocessableEntityException stringIsBlank(String parameter, String entityName){
        return stringIsBlank(parameter, entityName, null);
    }
    public static UnprocessableEntityException stringIsBlank(String parameter, String entityName, Throwable cause){
        return new UnprocessableEntityException(String.format("%s must not be blank", parameter),
                String.format("%s#%s is blank", entityName, parameter), 6, cause);
    }
    public static UnprocessableEntityException stringIsBlank(String parameter, Class<? extends AbstractEntity> entity){
        return stringIsBlank(parameter, entity.getSimpleName());
    }
    public static UnprocessableEntityException stringIsBlank(String parameter, Class<? extends AbstractEntity> entity,
                                                             Throwable cause){
        return stringIsBlank(parameter, entity.getSimpleName(), cause);
    }
}
