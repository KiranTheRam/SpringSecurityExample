package com.example.amigoscodesecurity.controller;

import com.example.amigoscodesecurity.model.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "James Bond"),
            new Student(2, "Maria Jones"),
            new Student(3, "Anna Smith")
    );

    @GetMapping(path = "{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId) {
        return STUDENTS.stream() // Take our List of students and make a stream out of it
                .filter(student -> studentId.equals(student.getStudentId())) // Look at each studdent in the stream and find one with an id that matches what was passed
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Student " + studentId + " does not exist")); // If the id isn't matched, throw exception
    }
}
