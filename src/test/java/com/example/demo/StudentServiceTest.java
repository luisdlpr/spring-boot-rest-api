package com.example.demo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.NonNull;

import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.StudentService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        void newStudentInvalidEmail() {
                Student student = new Student(
                                "Iona",
                                "invalidEmail",
                                LocalDate.of(1999, 6, 15));

                Exception exception = assertThrows(
                                IllegalArgumentException.class,
                                () -> {
                                        underTest.addNewStudent(student);
                                });

                assertTrue(exception.getMessage().contains("invalid email given"));
        }

        @Test
        void newStudentNullEmail() {
                Student student = new Student(
                                "Iona",
                                null,
                                LocalDate.of(1999, 6, 15));

                Exception exception = assertThrows(
                                IllegalArgumentException.class,
                                () -> {
                                        underTest.addNewStudent(student);
                                });

                assertTrue(exception.getMessage().contains("invalid email given"));
        }

        @Test
        void newStudentNoName() {
                Student studentA = new Student(
                                "",
                                "iq@email.com",
                                LocalDate.of(1999, 6, 15));

                Exception exception = assertThrows(
                                IllegalArgumentException.class,
                                () -> {
                                        underTest.addNewStudent(studentA);
                                });

                assertTrue(exception.getMessage().contains("invalid name given"));

                Student studentB = new Student(
                                null,
                                "iq@email.com",
                                LocalDate.of(1999, 6, 15));

                exception = assertThrows(
                                IllegalArgumentException.class,
                                () -> {
                                        underTest.addNewStudent(studentB);
                                });

                assertTrue(exception.getMessage().contains("invalid name given"));
        }

        @Test
        void newStudentTakenEmail() {
                Student student = new Student(
                                "Iona",
                                "iq@email.com",
                                LocalDate.of(1999, 6, 15));

                given(studentRepository
                                .findStudentByEmail(student.getEmail())).willReturn(Optional.of(student));

                Exception exception = assertThrows(
                                IllegalStateException.class,
                                () -> {
                                        underTest.addNewStudent(student);
                                });

                assertTrue(exception.getMessage().contains("email taken"));
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
        void updateStudentNotFound() throws Exception {
                Long id = 10L;
                given(studentRepository.findById(id))
                                .willReturn(Optional.empty());

                Exception exception = assertThrows(
                                IllegalStateException.class,
                                () -> {
                                        underTest.updateStudent(id, "Lewis", "l@p.com");
                                });

                assertTrue(exception.getMessage().contains("student with id " + id + " does not exist"));
        }

        @Test
        void deleteStudent() throws Exception {
                Long id = 10L;
                given(studentRepository.existsById(id)).willReturn(true);

                underTest.deleteStudent(id);

                verify(studentRepository).deleteById(id);
        }

        @Test
        void deleteStudentNotFound() throws Exception {
                Long id = 10L;
                given(studentRepository.existsById(id)).willReturn(false);

                Exception exception = assertThrows(
                                IllegalStateException.class,
                                () -> {
                                        underTest.deleteStudent(id);
                                });

                assertTrue(exception.getMessage().contains("student with id " + id + " does not exist"));
        }
}
