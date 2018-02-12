package com.mcit.AdmissionSystem.repository;

import com.mcit.AdmissionSystem.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByName(String name);

}
