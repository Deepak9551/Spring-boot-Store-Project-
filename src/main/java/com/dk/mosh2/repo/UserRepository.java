package com.dk.mosh2.repo;


import com.dk.mosh2.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

	 boolean existsByEmail(String email);

	 Optional<Users> findByEmail(String email);

}
