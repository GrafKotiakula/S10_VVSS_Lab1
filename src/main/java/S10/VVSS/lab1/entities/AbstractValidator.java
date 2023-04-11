package S10.VVSS.lab1.entities;

import S10.VVSS.lab1.exception.ClientException;
import S10.VVSS.lab1.exception.UnprocessableEntityException;

public abstract class AbstractValidator <E extends  AbstractEntity> {
    protected final Class<E> entityClass;

    public AbstractValidator(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    protected void validateNotNull(String parameter, Object value) throws UnprocessableEntityException {
        if(value == null) {
            throw UnprocessableEntityException.nullValue(parameter, entityClass);
        }
    }

    protected void validateStringLength(String parameter, String value, int maxLength)
            throws UnprocessableEntityException {
        if(value.length() > maxLength) {
            throw UnprocessableEntityException.stringTooLong(parameter, maxLength, entityClass);
        }
    }

    protected void validateStringLength(String parameter, String value, int minLength, int maxLength)
            throws UnprocessableEntityException{
        if(value.length() < minLength) {
            throw UnprocessableEntityException.stringTooShort(parameter, maxLength, entityClass);
        }
        validateStringLength(parameter, value, maxLength);
    }

    protected <T extends ClientException> void validateStringMatches(String value, String regex, T ex) throws T {
        if(!value.matches(regex)) {
            throw ex;
        }
    }

    protected void validateNotBlank(String parameter, String value) throws UnprocessableEntityException{
        if(value.isBlank()) {
            throw UnprocessableEntityException.stringIsBlank(parameter, entityClass);
        }
    }

    public abstract void validate (E entity) throws ClientException;
}
