//Data Layer
package com.example.demo.student;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // SELECT * FROM Student WHERE EMAIL = ?
    // @Query("SELECT s FROM Student s WHERE s.email = ?1") //jql not sql
    Optional<Student> findStudentByEmail(String email);
}
