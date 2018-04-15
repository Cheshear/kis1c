package com.example.authorization.controllers;

import com.example.authorization.CourseDatabase.Course;
import com.example.authorization.CourseDatabase.CourseDB;
import com.example.authorization.CourseDatabase.CourseList;
import com.example.authorization.DeleteCourseObject;
import com.example.authorization.Responses.CourseResponse;
import com.example.authorization.Responses.Response;
import com.example.authorization.UpdateCourseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping
public class CourseController {
    private CourseDB courseDB = new CourseDB();

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/add_course")
    public ResponseEntity<Response> addCourse(@RequestBody Course course) {
        Response response = new Response();
        String message;
        boolean status = courseDB.addCourse(course);
        if (status) {
            message = "Course added";

        } else {
            message = "Course already added";
        }
        response.setStatus(status);
        response.setResponse(message);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/update_course")
    public ResponseEntity<Response> updateCourse(@RequestBody UpdateCourseObject updateCourseObject){
        Response response = new Response();
        String message;
        courseDB.updateCourse(updateCourseObject.getUpdatingCourse(), updateCourseObject.getChanges());
        response.setStatus(true);
        message = "course updated";
        response.setResponse(message);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/delete_course")
    public ResponseEntity<Response> deleteCourse(@RequestBody DeleteCourseObject deleteCourseObject){
        Response response = new Response();
        String message = "element deleted";
        courseDB.deleteSpecificCourse(deleteCourseObject.getDeletingCourse());
        response.setResponse(message);
        response.setStatus(true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/get_courses")
    public ArrayList<CourseList> getCourses(@RequestParam int level){
        return courseDB.GetCourses(level);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/get_details")
    public Course getCourse(@RequestParam String id){
        int number = Integer.parseInt(id);
        return courseDB.getSpecificCourseByNumber(number);
    }

//    @CrossOrigin(origins = "*")
//    @PostMapping(value = "/select_course")
//    public CourseResponse selectCourses(@RequestBody String courseName, @RequestHeader String token) {
//        return
//    }
}
