package com.example.demo.student;

import java.util.List;
import java.util.Optional;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component; also valid but use service for semantics
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        // check valid email is given
        EmailValidator emailValidator = EmailValidator.getInstance();

        if (student.getEmail() != null && !emailValidator.isValid(student.getEmail())) {
            throw new IllegalArgumentException("invalid email given");
        } else if (student.getName().length() <= 0) {
            throw new IllegalArgumentException("invalid name given");
        }

        Optional<Student> studentByEmail = studentRepository
                .findStudentByEmail(student.getEmail());

        if (studentByEmail.isPresent()) { // example validation
            throw new IllegalStateException("email taken");
        }

        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new IllegalStateException("student with id " + studentId + " does not exist");
        }

        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "student with id " + studentId + " does not exist"));

        if (name != null && name.length() > 0) {
            student.setName(name);
        }

        EmailValidator emailValidator = EmailValidator.getInstance();
        if (email != null &&
                email.length() > 0 &&
                !studentRepository.findStudentByEmail(email).isPresent() &&
                emailValidator.isValid(email)) {
            student.setEmail(name);
        }

        studentRepository.save(student);
    }
}
