package com.mcit.AdmissionSystem.repository;

import com.mcit.AdmissionSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByUserName(String userName);
}
