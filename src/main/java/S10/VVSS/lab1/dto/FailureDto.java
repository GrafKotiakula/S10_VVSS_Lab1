package S10.VVSS.lab1.dto;

import S10.VVSS.lab1.exception.ClientException;

public class FailureDto {
    private Integer code;
    private String message;

    public FailureDto(ClientException exception) {
        this.code = exception.getCode();
        this.message = exception.getClientMessage();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
