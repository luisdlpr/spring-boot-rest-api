package com.example.demo.student;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void savesAndChecksStudents() {
        String email = "iq@gmail.com";
        Student expectedStudent = new Student(
                "Iona",
                email,
                LocalDate.of(1999, 06, 15));

        underTest.save(expectedStudent);

        Student foundStudent = underTest.findStudentByEmail(email).get();

        assertEquals(expectedStudent.getName(), foundStudent.getName());
        assertEquals(expectedStudent.getEmail(), foundStudent.getEmail());
        assertEquals(expectedStudent.getId(), foundStudent.getId());
        assertEquals(expectedStudent.getDob(), foundStudent.getDob());
        assertEquals(expectedStudent.getAge(), foundStudent.getAge());
    }

}
