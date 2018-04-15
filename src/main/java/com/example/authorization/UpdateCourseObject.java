package com.example.authorization;

import com.example.authorization.CourseDatabase.Course;
import lombok.Getter;
import lombok.Setter;

public class UpdateCourseObject {
    @Getter @Setter Course updatingCourse;
    @Getter @Setter Course changes;
}
