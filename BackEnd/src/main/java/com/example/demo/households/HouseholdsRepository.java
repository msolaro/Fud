package com.example.demo.households;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author msolaro
 * @author rlspick
 *
 */
@Repository
public interface HouseholdsRepository extends JpaRepository<Households, Long> {

	Households findByHousehold(String household);

	boolean existsHouseholdsByHousehold(String housename);

	boolean existsBySessionKey(String sessionkey);

	Households findBySessionKey(String sessionkey);

	boolean existsByHousehold(String household);
}
