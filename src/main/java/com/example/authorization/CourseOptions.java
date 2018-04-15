package com.example.authorization;

import lombok.Getter;
import lombok.Setter;

public class CourseOptions {
    @Setter @Getter boolean update;
    @Setter @Getter boolean add;
    @Getter @Setter String updatingCourse;
}
