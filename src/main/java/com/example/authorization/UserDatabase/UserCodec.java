package com.example.authorization.UserDatabase;

import com.example.authorization.UserDatabase.User;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

public class UserCodec implements CollectibleCodec<User> {

    private CodecRegistry registry;

    public UserCodec(CodecRegistry cr) {
        registry = cr;
    }

    @Override
    public User generateIdIfAbsentFromDocument(User user) {
        return documentHasId(user) ? user : user.withNewObjectId();
    }

    @Override
    public boolean documentHasId(User user) {
        return user.get_id() != null;
    }

    @Override
    public BsonValue getDocumentId(User user) {
        if (!documentHasId(user)) {
            throw new IllegalStateException();
        }
        return new BsonString(user.get_id().toHexString());
    }

    @Override
    public User decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        reader.readName();
        ObjectId _id = reader.readObjectId();
        reader.readName();
        String _class = reader.readString();
        reader.readName();
        String surename = reader.readString();
        reader.readName();
        String name = reader.readString();
        reader.readName();
        String patronymic = reader.readString();
        reader.readName();
        String role = reader.readString();
        reader.readName();
        int course = reader.readInt32();
        reader.readName();
        String direction = reader.readString();
        reader.readName();
        String email = reader.readString();
        reader.readName();
        String password = reader.readString();
        reader.readEndDocument();
        return new User(_id, _class, surename, name,  patronymic, role, course, direction, email, password);
    }

    @Override
    public void encode(BsonWriter writer, User user, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeName("_id");
        writer.writeObjectId(user.get_id());
        writer.writeName("_class");
        writer.writeString(user.get_class());
        writer.writeName("surename");
        writer.writeString(user.getSurename());
        writer.writeName("name");
        writer.writeString(user.getName());
        writer.writeName("patronymic");
        writer.writeString(user.getPatronymic());
        writer.writeName("role");
        writer.writeString(user.getRole());
        writer.writeName("course");
        writer.writeInt32(user.getCourse());
        writer.writeName("direction");
        writer.writeString(user.getDirection());
        writer.writeName("email");
        writer.writeString(user.getEmail());
        writer.writeName("password");
        writer.writeString(user.getPassword());
        writer.writeEndDocument();
    }

    @Override
    public Class<User> getEncoderClass() {
        return User.class;
    }
}
