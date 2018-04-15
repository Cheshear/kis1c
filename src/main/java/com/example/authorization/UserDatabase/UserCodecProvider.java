package com.example.authorization.UserDatabase;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;


public class UserCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> type, CodecRegistry cr) {
        if(type == User.class){
            return (Codec<T>) new UserCodec(cr);
        }
        return null;
    }
}
