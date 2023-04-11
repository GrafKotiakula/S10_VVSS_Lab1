package S10.VVSS.lab1.dto;

import S10.VVSS.lab1.entities.user.User;

public class AuthResponseDto {
    private User user;
    private String token;

    public AuthResponseDto(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
