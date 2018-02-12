package com.mcit.AdmissionSystem.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="as_professor")
public class Professor implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name="SEQ_GEN", sequenceName="SEQ_AS_PROFESSOR", allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}