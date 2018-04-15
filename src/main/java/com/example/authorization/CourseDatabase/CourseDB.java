package com.example.authorization.CourseDatabase;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.StringCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class CourseDB {

    private MongoClient mongoClient;
    private MongoDatabase mongoDB;
    private MongoCollection<Course> courses;

    public CourseDB(){
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(new StringCodec()),
                CodecRegistries.fromProviders(
                        new CourseCodecProvider()),
                MongoClient.getDefaultCodecRegistry());
        MongoClientOptions options = MongoClientOptions.builder()
                .codecRegistry(codecRegistry).build();
        mongoClient = new MongoClient(new ServerAddress(), options);
        mongoDB = mongoClient.getDatabase("local");
        courses = mongoDB.getCollection("Courses", Course.class);
    }

    public boolean addCourse(Course course){
        Iterable<Course> equalCourses = courses.find(eq("name", course.getName()));
        boolean exist = false;
        for (Course equalCourse : equalCourses) {
            if (course.getName().equals(equalCourse.getName())) {
                exist = true;
            }
        }
        if(!exist) {
            courses.insertOne(course);
            return true;
        } else{
            return false;
        }
    }

    public void updateCourse(Course updatingCourse, Course course){
        Iterable<Course> equalCourses = courses.find(eq("name", updatingCourse.getName()));
        for(Course courseForUpdating: equalCourses){
            courses.deleteOne(courseForUpdating);
            courses.insertOne(course);
        }
    }

    public ArrayList<CourseList> GetCourses(int level){
        ArrayList<CourseList> courseLists = new ArrayList<>();
        Iterable<Course> coursesOfThisLevel =courses.find(eq("level", level));
        for(Course course: coursesOfThisLevel){
            CourseList courseList = new CourseList();
            courseList.setName(course.getName());
            courseList.setNumber(course.getNumber());
            courseList.setOpen(false);
            courseList.setRequiredCourses(new ArrayList<>());
            if(level > 1) {
                setCourseList(course, courseList.getRequiredCourses());
            }
            courseLists.add(courseList);
        }
        return courseLists;
    }

    private void setCourseList(Course course, ArrayList<CourseList> list){
        for(String courseName: course.getRequiredCourses()){
            Course specificCourse = getSpecificCourse(courseName);
            if(specificCourse != null) {
                if (course.getLevel() > 2) {
                    ArrayList<CourseList> childList = new ArrayList<>();
                    setCourseList(specificCourse, childList);
                    CourseList cl = new CourseList();
                    cl.setNumber(specificCourse.getNumber());
                    cl.setName(specificCourse.getName());
                    cl.setOpen(false);
                    cl.setRequiredCourses(childList);
                    list.add(cl);
                } else {
                    CourseList cl = new CourseList();
                    System.out.println(specificCourse.getName());
                    cl.setNumber(specificCourse.getNumber());
                    cl.setName(specificCourse.getName());
                    cl.setOpen(false);
                    cl.setRequiredCourses(null);
                    list.add(cl);
                }
            }
        }
    }

    public Course getSpecificCourse(String name){
        Iterable<Course> specificCourse = courses.find(eq("name", name));
        for (Course course: specificCourse){
            return course;
        }
        return null;
    }

    public Course getSpecificCourseByNumber(int number){
        Iterable<Course> specificCourse = courses.find(eq("number", number));
        for (Course course: specificCourse){
            return course;
        }
        return null;
    }

    public boolean deleteSpecificCourse(String name){
        Iterable<Course> specificCourse = courses.find(eq("name", name));
        for (Course course: specificCourse){
            courses.deleteOne(course);
            return true;
        }
        return false;
    }
}
