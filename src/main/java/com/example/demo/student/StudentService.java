package com.example.demo.student;

import java.util.List;

// import org.springframework.stereotype.Component; also valid but use service for semantics
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;

@Service
public class StudentService {
    public List<Student> getStudents() {
       return List.of(
               new Student(1L, "Mariam", "mariam@gmail.com", 23, LocalDate.of(2000, Month.JANUARY, 21)));
    }
}
