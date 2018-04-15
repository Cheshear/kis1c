package com.example.authorization.TokenObjects;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

public class ResetPasswordTokenObject {
    @Getter @Setter private ObjectId userId;
    @Getter @Setter private String token;
    @Getter @Setter private String expiredTime;

    public ResetPasswordTokenObject() {
    }

    public ResetPasswordTokenObject(ObjectId userId, String token, String expiredTime) {
        this.userId = userId;
        this.token = token;
        this.expiredTime = expiredTime;
    }
}
