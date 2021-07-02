package com.example.demo.pantry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author msolaro
 * @author rlspick
 *
 */
@Repository
public interface PantryRepository extends JpaRepository<Pantry, Long> {
	Pantry findByPantryName(String pantryName);

	Pantry existsPantrysByPantryName(String pantryName);

	boolean existsBySessionKey(String sessionkey);

	Pantry findBySessionKey(String sessionkey);
}
