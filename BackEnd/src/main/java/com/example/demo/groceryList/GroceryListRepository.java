package com.example.demo.groceryList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author msolaro
 * @author rlspick
 *
 */
@Repository
public interface GroceryListRepository extends JpaRepository<GroceryList, Long> {

	boolean existsBySessionKey(String sessionkey);

	GroceryList findBySessionKey(String sessionkey);

	GroceryList findByGroceryListName(String username);
}
