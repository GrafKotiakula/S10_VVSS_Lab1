package S10.VVSS.lab1.exception;

public class AuthException extends ClientException {
    public static final int codeOffset = 1000;

    public AuthException(String message, int code) {
        super(message, codeOffset + code);
    }

    public AuthException(String message, String clientMessage, int code) {
        super(message, clientMessage, codeOffset + code);
    }

    public AuthException(String message, String clientMessage, int code, Throwable cause) {
        super(message, clientMessage, codeOffset + code, cause);
    }

    public AuthException(String message, int code, Throwable cause) {
        super(message, codeOffset + code, cause);
    }

    public static AuthException expiredToken(){
        return expiredToken(null);
    }

    public static AuthException expiredToken(Throwable cause){
        return new AuthException("Token is expired", 0, cause);
    }

    public static AuthException invalidToken(){
        return invalidToken(null);
    }

    public static AuthException invalidToken(Throwable cause){
        return new AuthException("Token is invalid", 1, cause);
    }

    public static AuthException wrongUsernameOrPassword(){
        return wrongUsernameOrPassword(null);
    }

    public static AuthException wrongUsernameOrPassword(Throwable cause){
        return new AuthException("Wrong username or password", 2, cause);
    }
}
