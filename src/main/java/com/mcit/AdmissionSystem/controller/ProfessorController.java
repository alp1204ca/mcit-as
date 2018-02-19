package com.mcit.AdmissionSystem.controller;

import com.mcit.AdmissionSystem.model.Professor;
import com.mcit.AdmissionSystem.model.Role;
import com.mcit.AdmissionSystem.model.User;
import com.mcit.AdmissionSystem.service.MailService;
import com.mcit.AdmissionSystem.service.ProfessorService;
import com.mcit.AdmissionSystem.service.RoleService;
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
public class ProfessorController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private ProfessorService professorService;

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

    @GetMapping("/professor")
    @ResponseBody
    public ModelAndView professor( ModelAndView modelAndView ) {
        log.info("/professor called");

        if (modelAndView==null)
            modelAndView = new ModelAndView("professor");

        try {

            List<Professor> professorList = professorService.findAll();
            modelAndView.addObject("professors", professorList);
        } catch (Exception e) {
            log.error("Error retrieving professors",e);
            modelAndView.addObject("error", "Error retrieving professors");
        }

        return modelAndView;
    }

    @PostMapping("/professor/new")
    @ResponseBody
    public ModelAndView newProfessor(@ModelAttribute Professor professor) {

        ModelAndView modelAndView = new ModelAndView("professor");

        if (professor != null && professor.getFirstName() != null && professor.getLastName() != null &&
                professor.getUser() != null && professor.getUser().getUserName() != null && professor.getUser().getEmail() != null) {

            try {

                User user = userService.findByUserName(professor.getUser().getUserName());
                if (user != null) {
                    log.error("Could not add professor " + professor.getFirstName() + " "
                            + professor.getLastName() + ". Username already in use: "
                            + professor.getUser().getUserName());
                    modelAndView.addObject("error", "Error adding professor - Username already in use");
                } else {
                    String password = RandomStringUtils.random(10, true, true);
                    String passwordEncrypted = passwordEncoder.encode(password);
                    Role adminRole = roleService.findByCode("ROLE_PROFESSOR");
                    Set<Role> roles = new HashSet<>();
                    roles.add(adminRole);
                    user = new User(professor.getUser().getUserName(), passwordEncrypted, professor.getUser().getEmail(), roles);
                    professor.setUser(user);
                    professorService.add(professor);

                    Map<String, Object> params = new HashMap<>();
                    params.put("name", professor.getFirstName() + " " + professor.getLastName());
                    params.put("username", professor.getUser().getUserName());
                    params.put("password", password);
                    params.put("url", url);

                    mailService.send(mailFrom, user.getEmail(), "Welcome to MCIT professor", "new_account_professor.html", params);

                    modelAndView.addObject("message", "Professor successfully added");
                }
            } catch (Exception e) {
                log.error("Could not add professor " + professor.getFirstName() + " " + professor.getLastName(), e);
                modelAndView.addObject("error", "Error adding professor");
           }

        } else
            modelAndView.addObject("error", "Invalid professor data");

        return professor(modelAndView);
    }

    @PostMapping("/professor/edit")
    @ResponseBody
    public ModelAndView editProfessor(@ModelAttribute Professor professor) {

        ModelAndView modelAndView = new ModelAndView("professor");

        if (professor != null && professor.getFirstName() != null && professor.getLastName() != null) {

            Professor professor_ = professorService.findById(professor.getId());

            if (professor_ != null) {
                try {
                    professorService.update(professor);
                    modelAndView.addObject("message", "Professor successfully updated");
                } catch (Exception e) {
                    log.error("Could not update professor " +  professor.getFirstName() + " " + professor.getLastName(), e);
                    modelAndView.addObject("error", "Error updating professor");
                }
            } else
                modelAndView.addObject("error", "Professor does not exist");
        } else
            modelAndView.addObject("error", "Invalid professor data");

        return professor(modelAndView);
    }

    @PostMapping("/professor/delete")
    @ResponseBody
    public ModelAndView deleteProfessor(@ModelAttribute Professor professor) {

        ModelAndView modelAndView = new ModelAndView("professor");

        if (professor != null && professor.getId() != null) {

            Professor professor_ = professorService.findOneWithUserAndRoles(professor.getId());

            if (professor_ != null) {
                try {
                    professorService.delete(professor_);
                    modelAndView.addObject("message", "Professor successfully deleted");
                } catch (Exception e) {
                    log.error("Could not delete professor " +  professor.getFirstName() + " " + professor.getLastName(), e);
                    modelAndView.addObject("error", "Error deleting professor");
                }
            } else
                modelAndView.addObject("error", "Professor does not exist");
        } else
            modelAndView.addObject("error", "Invalid professor");

        return professor(modelAndView);
    }

}
