package S10.VVSS.lab1.dto;

import java.util.UUID;

public class LogInResponseDto {
    private UUID id;
    private String username;
    private String token;

    public LogInResponseDto(UUID id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
