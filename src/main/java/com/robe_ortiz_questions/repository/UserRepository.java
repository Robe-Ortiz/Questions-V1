package com.robe_ortiz_questions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.robe_ortiz_questions.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	    
}
