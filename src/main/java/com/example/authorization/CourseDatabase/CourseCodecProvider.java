package com.example.authorization.CourseDatabase;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class CourseCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> type, CodecRegistry cr) {
        if (type == Course.class) {
            return (Codec<T>) new CourseCodec(cr);
        }
        return null;
    }
}
