package com.boymask.rysolvia.database.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select u from User u where u.userid = ?1")
	User findByUserId(String userid);
}
