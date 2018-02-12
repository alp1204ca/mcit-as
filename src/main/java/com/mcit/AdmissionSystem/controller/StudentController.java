package com.mcit.AdmissionSystem.controller;

import com.mcit.AdmissionSystem.model.Student;
import com.mcit.AdmissionSystem.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private StudentService studentService;

    @GetMapping("/student")
    @ResponseBody
    public ModelAndView student( ModelAndView modelAndView ) {
        log.info("/student called");

        if (modelAndView==null)
            modelAndView = new ModelAndView("student");

        try {

            List<Student> studentList = studentService.findAll();
            modelAndView.addObject("students", studentList);
        } catch (Exception e) {
            log.error("Error retrieving students",e);
            modelAndView.addObject("error", "Error retrieving students");
        }

        return modelAndView;
    }

    @PostMapping("/student/new")
    @ResponseBody
    public ModelAndView newStudent(@ModelAttribute Student student) {

        ModelAndView modelAndView = new ModelAndView("student");

        if (student != null && student.getFirstName() != null && student.getLastName() != null) {

            try {
                studentService.add(student);
                modelAndView.addObject("message", "Student successfully added");
            } catch (Exception e) {
                log.error("Could not add student " + student.getFirstName() + " " + student.getLastName(), e);
                modelAndView.addObject("error", "Error adding student");
            }

        } else
            modelAndView.addObject("error", "Invalid student data");

        return student(modelAndView);
    }

    @PostMapping("/student/edit")
    @ResponseBody
    public ModelAndView editStudent(@ModelAttribute Student student) {

        ModelAndView modelAndView = new ModelAndView("student");

        if (student != null && student.getFirstName() != null && student.getLastName() != null) {

            Student student_ = studentService.findById(student.getId());

            if (student_ != null) {
                try {
                    studentService.update(student);
                    modelAndView.addObject("message", "Student successfully updated");
                } catch (Exception e) {
                    log.error("Could not update student " +  student.getFirstName() + " " + student.getLastName(), e);
                    modelAndView.addObject("error", "Error updating student");
                }
            } else
                modelAndView.addObject("error", "Student does not exist");
        } else
            modelAndView.addObject("error", "Invalid student data");

        return student(modelAndView);
    }

    @PostMapping("/student/delete")
    @ResponseBody
    public ModelAndView deleteStudent(@ModelAttribute Student student) {

        ModelAndView modelAndView = new ModelAndView("student");

        if (student != null && student.getId() != null) {

            Student student_ = studentService.findById(student.getId());

            if (student_ != null) {
                try {
                    studentService.deleteById(student.getId());
                    modelAndView.addObject("message", "Student successfully deleted");
                } catch (Exception e) {
                    log.error("Could not delete student " +  student.getFirstName() + " " + student.getLastName(), e);
                    modelAndView.addObject("error", "Error adding student");
                }
            } else
                modelAndView.addObject("error", "Student does not exist");
        } else
            modelAndView.addObject("error", "Invalid student");

        return student(modelAndView);
    }

}
