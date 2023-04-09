package S10.VVSS.lab1.exception;

public class ClientException extends RuntimeException{
    private String clientMessage;
    private int code;

    public ClientException(String message, int code) {
        this(message, message, code);
    }

    public ClientException(String message, String clientMessage, int code) {
        super(message);
        this.clientMessage = clientMessage;
        this.code = code;
    }

    public ClientException(String message, String clientMessage, int code, Throwable cause) {
        super(message, cause);
        this.clientMessage = clientMessage;
        this.code = code;
    }

    public ClientException(String message, int code, Throwable cause) {
        this(message, message, code, cause);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getClientMessage() {
        return clientMessage;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }
}
