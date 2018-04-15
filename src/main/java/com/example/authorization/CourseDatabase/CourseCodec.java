package com.example.authorization.CourseDatabase;

import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class CourseCodec implements CollectibleCodec<Course> {
    private CodecRegistry registry;

    public CourseCodec(CodecRegistry cr){
        registry = cr;
    }

    @Override
    public Course generateIdIfAbsentFromDocument(Course course) {
        return documentHasId(course) ? course : course.withNewObjectId();
    }

    @Override
    public boolean documentHasId(Course course) {
        return course.get_id() != null;
    }

    @Override
    public BsonValue getDocumentId(Course course) {
        if(!documentHasId(course)){
            throw new IllegalStateException();
        }
        return new BsonString(course.get_id().toHexString());
    }

    @Override
    public Course decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        reader.readName();
        ObjectId _id = reader.readObjectId();
        reader.readName();
        int number = reader.readInt32();
        reader.readName();
        int level = reader.readInt32();
        reader.readName();
        String name = reader.readString();
        reader.readName();
        String description = reader.readString();
        reader.readName();
        reader.readStartArray();
        ArrayList<String> courseNames = new ArrayList<>();
        Codec<String> objectIdCodec = registry.get(String.class);
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            courseNames.add(objectIdCodec.decode(reader, decoderContext));
        }
        reader.readEndArray();
        reader.readEndDocument();
        return new Course(_id, number, level, name, description, courseNames);
    }

    @Override
    public void encode(BsonWriter writer, Course course, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeName("_id");
        writer.writeObjectId(course.get_id());
        writer.writeName("number");
        writer.writeInt32(course.getNumber());
        writer.writeName("level");
        writer.writeInt32(course.getLevel());
        writer.writeName("name");
        writer.writeString(course.getName());
        writer.writeName("description");
        writer.writeString(course.getDescription());
        writer.writeName("requiredCourses");
        writer.writeStartArray();
        for(String courseName: course.getRequiredCourses()){
            encoderContext.encodeWithChildContext(registry.get(String .class), writer, courseName);
        }
        writer.writeEndArray();
        writer.writeEndDocument();
    }

    @Override
    public Class<Course> getEncoderClass() {
        return Course.class;
    }
}
