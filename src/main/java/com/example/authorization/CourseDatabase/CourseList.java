package com.example.authorization.CourseDatabase;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class CourseList {
    @Getter @Setter private String name;
    @Getter @Setter private int number;
    @Getter @Setter private boolean isOpen;
    @Getter @Setter private ArrayList<CourseList> requiredCourses = new ArrayList<>();
}
