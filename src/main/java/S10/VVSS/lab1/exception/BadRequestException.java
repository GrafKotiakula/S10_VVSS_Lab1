package S10.VVSS.lab1.exception;

public class BadRequestException extends ClientException {
    private static final int codeOffset = 1300;

    protected BadRequestException(String message, int code) {
        super(message, codeOffset + code);
    }

    protected BadRequestException(String clientMessage, String message, int code) {
        super(clientMessage, message, codeOffset + code);
    }

    protected BadRequestException(String clientMessage, String message, int code, Throwable cause) {
        super(clientMessage, message, codeOffset + code, cause);
    }

    protected BadRequestException(String message, int code, Throwable cause) {
        super(message, codeOffset + code, cause);
    }


    public static BadRequestException wrongFormat(String parameter, String format) {
        return wrongFormat(parameter, format, null);
    }
    public static BadRequestException wrongFormat(String parameter, String format, Throwable cause) {
        return new BadRequestException(String.format("%s is not %s format", parameter, format), 0, cause);
    }
}
