package com.example.authorization.CourseDatabase;

import lombok.Getter;
import lombok.Setter;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

public class Course implements Bson{
    @Getter @Setter private ObjectId _id;
    @Getter @Setter private int number;
    @Getter @Setter private int level;
    @Getter @Setter private String name;
    @Getter @Setter private String description;
    @Getter @Setter private ArrayList<String> requiredCourses;

    public Course() {}

    public Course(ObjectId _id, int number, int level, String name,
                  String description, ArrayList<String> requiredCourses) {
        this._id = _id;
        this.number = number;
        this.level = level;
        this.name = name;
        this.description = description;
        this.requiredCourses = requiredCourses;
    }

    public Course withNewObjectId(){
        set_id(new ObjectId());
        return this;
    }

    @Override
    public <TDocument> BsonDocument toBsonDocument(Class<TDocument> type, CodecRegistry cr) {
        if(type == Course.class){
            return new BsonDocumentWrapper<Course>(this, cr.get(Course.class));
        }
        return null;
    }
}
