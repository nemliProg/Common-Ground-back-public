package io.cg.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginSuccess {
    private String token;

    private String username;

    private String role;

    private String email;

    public LoginSuccess(String token, String username, String role, String email) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.email = email;
    }

}
