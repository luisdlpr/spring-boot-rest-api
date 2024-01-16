package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cglib.core.Local;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.StudentService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Optional;

import org.checkerframework.checker.units.qual.h;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

@SpringBootTest
public class StudentServiceTest {

        @Mock
        private StudentRepository studentRepository;
        private StudentService underTest;

        @BeforeEach
        void setUp() {
                underTest = new StudentService(studentRepository);
        }

        @Test
        void getStudents() {
                underTest.getStudents();
                verify(studentRepository).findAll();
        }

        @Test
        void newStudent() {
                Student student = new Student(
                                "Iona",
                                "ionskee@gmail.com",
                                LocalDate.of(1999, 6, 15));

                underTest.addNewStudent(student);

                // use argument captor to capture the argument saved by studentRepository
                ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
                verify(studentRepository).save(studentArgumentCaptor.capture());

                Student capturedStudent = studentArgumentCaptor.getValue();

                assertEquals(student, capturedStudent);
        }

        @Test
        void updateStudent() throws Exception {
                Long id = 10L;
                Student testStudent = new Student("Luis", "l@g.com", LocalDate.of(1999, 5, 21));
                given(studentRepository.findById(id))
                                .willReturn(Optional.of(testStudent));

                underTest.updateStudent(id, "Lewis", "l@p.com");

                testStudent.setName("Lewis");
                testStudent.setEmail("l@p.com");

                verify(studentRepository).save(testStudent);
        }

        @Test
        void deleteStudent() throws Exception {
                Long id = 10L;
                given(studentRepository.existsById(id)).willReturn(true);

                underTest.deleteStudent(id);

                verify(studentRepository).deleteById(id);
        }
}
