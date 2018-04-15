package com.example.authorization.Responses;


import lombok.Getter;
import lombok.Setter;

public class ResetPasswordResponse {
    @Setter @Getter private String resetPasswordToken;
    @Setter @Getter private boolean status;
    @Setter @Getter private String response;
}
