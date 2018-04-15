package com.example.authorization.UserDatabase;


import lombok.Getter;
import lombok.Setter;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;


public class User implements Bson{
    @Getter @Setter private ObjectId _id;
    @Getter @Setter private String _class;
    @Getter @Setter private String surename;
    @Getter @Setter private String name;
    @Getter @Setter private String patronymic;
    @Getter @Setter private String role;
    @Getter @Setter private int course;
    @Getter @Setter private String direction;
    @Getter @Setter private String email;
    @Getter @Setter private String password;

    public User() {
    }

    public User(ObjectId _id, String _class, String surename, String name, String patronymic, String role, int course, String direction,
                String email, String password) {
        this._id = _id;
        this._class = _class;
        this.surename = surename;
        this.name = name;
        this.patronymic = patronymic;
        this.role = role;
        this.course = course;
        this.direction = direction;
        this.email = email;
        this.password = password;
    }

    public User withNewObjectId(){
        set_id(new ObjectId());
        return this;
    }

    @Override
    public <TDocument> BsonDocument toBsonDocument(Class<TDocument> type, CodecRegistry cr) {
        if (type == User.class) {
            return new BsonDocumentWrapper<User>(this, cr.get(User.class));
        }
        return null;
    }
}
