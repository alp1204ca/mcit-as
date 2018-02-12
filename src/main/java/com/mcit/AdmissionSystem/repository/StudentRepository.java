package com.mcit.AdmissionSystem.repository;

import com.mcit.AdmissionSystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {


}
