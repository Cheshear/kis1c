package com.example.authorization.TokenObjects;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

public class TokenObj {
    @Getter @Setter private ObjectId userId;
    @Getter @Setter private String token;
    @Getter @Setter private String loginTime;

    public TokenObj() {
    }

    public TokenObj(ObjectId userId, String token, String loginTime) {
        this.userId = userId;
        this.token = token;
        this.loginTime = loginTime;
    }
}
