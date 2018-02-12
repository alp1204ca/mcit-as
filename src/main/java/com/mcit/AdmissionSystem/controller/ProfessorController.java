package com.mcit.AdmissionSystem.controller;

import com.mcit.AdmissionSystem.model.Professor;
import com.mcit.AdmissionSystem.service.ProfessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ProfessorController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private ProfessorService professorService;

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

        if (professor != null && professor.getFirstName() != null && professor.getLastName() != null) {

            try {
                professorService.add(professor);
                modelAndView.addObject("message", "Professor successfully added");
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

            Professor professor_ = professorService.findById(professor.getId());

            if (professor_ != null) {
                try {
                    professorService.deleteById(professor.getId());
                    modelAndView.addObject("message", "Professor successfully deleted");
                } catch (Exception e) {
                    log.error("Could not delete professor " +  professor.getFirstName() + " " + professor.getLastName(), e);
                    modelAndView.addObject("error", "Error adding professor");
                }
            } else
                modelAndView.addObject("error", "Professor does not exist");
        } else
            modelAndView.addObject("error", "Invalid professor");

        return professor(modelAndView);
    }

}
