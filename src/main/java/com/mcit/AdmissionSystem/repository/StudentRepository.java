package com.mcit.AdmissionSystem.repository;

import com.mcit.AdmissionSystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select student from Student student join fetch student.user user join fetch user.roles roles where student.id = ?1")
    Student findOneWithUserAndRoles(long id);
}
