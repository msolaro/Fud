package com.example.demo.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author msolaro
 * @author rlspick
 *
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

	Users findByUsername(String Username);

	boolean existsBySessionKey(String sessionkey);

	Users findBySessionKey(String sessionkey);

	boolean existsByUsername(String username);
}
