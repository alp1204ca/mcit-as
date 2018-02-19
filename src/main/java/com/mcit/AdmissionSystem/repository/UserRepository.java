package com.mcit.AdmissionSystem.repository;

import com.mcit.AdmissionSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByUserName(String userName);
}
