package com.mcit.AdmissionSystem.controller;

import com.mcit.AdmissionSystem.model.Role;
import com.mcit.AdmissionSystem.model.Student;
import com.mcit.AdmissionSystem.model.User;
import com.mcit.AdmissionSystem.service.MailService;
import com.mcit.AdmissionSystem.service.RoleService;
import com.mcit.AdmissionSystem.service.StudentService;
import com.mcit.AdmissionSystem.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MailService mailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${mail.from}")
    private String mailFrom;

    @Value("${as.mcit.url}")
    private String url;

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

        try {

            User user = userService.findByUserName(student.getUser().getUserName());
            if (user != null) {
                log.error("Could not add student " + student.getFirstName() + " "
                        + student.getLastName() + ". Username already in use: "
                        + student.getUser().getUserName());
                modelAndView.addObject("error", "Error adding student - Username already in use");
            } else {
                String password = RandomStringUtils.random(10, true, true);
                String passwordEncrypted = passwordEncoder.encode(password);
                Role adminRole = roleService.findByCode("ROLE_STUDENT");
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                user = new User(student.getUser().getUserName(), passwordEncrypted, student.getUser().getEmail(), roles);
                student.setUser(user);
                studentService.add(student);

                Map<String, Object> params = new HashMap<>();
                params.put("name", student.getFirstName() + " " + student.getLastName());
                params.put("username", student.getUser().getUserName());
                params.put("password", password);
                params.put("url", url);
                params.put("type", "student");

                mailService.send(mailFrom, user.getEmail(), "Welcome to MCIT student", "new_account.html", params);

                modelAndView.addObject("message", "Professor successfully added");
            }
        } catch (Exception e) {
            log.error("Could not add student " + student.getFirstName() + " " + student.getLastName(), e);
            modelAndView.addObject("error", "Error adding student");
        }


        return student(modelAndView);
    }

    @PostMapping("/student/edit")
    @ResponseBody
    public ModelAndView editStudent(@ModelAttribute Student student) {

        ModelAndView modelAndView = new ModelAndView("student");

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

        return student(modelAndView);
    }

    @PostMapping("/student/delete")
    @ResponseBody
    public ModelAndView deleteStudent(@ModelAttribute Student student) {

        ModelAndView modelAndView = new ModelAndView("student");

        Student student_ = studentService.findOneWithUserAndRoles(student.getId());

        if (student_ != null) {
            try {
                studentService.delete(student_);
                modelAndView.addObject("message", "Student successfully deleted");
            } catch (Exception e) {
                log.error("Could not delete student " +  student.getFirstName() + " " + student.getLastName(), e);
                modelAndView.addObject("error", "Error adding student");
            }
        } else
            modelAndView.addObject("error", "Student does not exist");

        return student(modelAndView);
    }

}
