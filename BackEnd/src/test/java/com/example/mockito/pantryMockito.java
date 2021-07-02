package com.example.mockito;

import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.demo.households.*;
import com.example.demo.item.Item;
import com.example.demo.pantry.Pantry;
import com.example.demo.pantry.PantryRepository;
import com.example.demo.users.Users;
import com.example.demo.users.UsersRepository;

/**
 * 
 * @author msolaro
 *
 */
public class pantryMockito {

	@InjectMocks
	Pantry pan = new Pantry();

	@Mock
	UsersRepository usersRepo;

	@Mock
	PantryRepository pRepo;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateUser() {

		Users user1 = new Users();
		user1.setFirstName("Max");
		user1.setLastName("Solaro");
		List<Users> list = new ArrayList<>();
		list.add(user1);
		when(usersRepo.findAll()).thenReturn(list);
		assertEquals(1, usersRepo.findAll().size());

	}

	@Test
	public void pantryAdd() {

		Users user1 = new Users();
		user1.setFirstName("Max");
		user1.setLastName("Solaro");
		user1.setUsername("msolaro");
		user1.setSessionKey("testing");

		// create the pantry
		Pantry p = new Pantry(user1.getSessionKey());

		// create a food item
		Item food = new Item();
		food.setName("Toast");
		food.setCategory("Carbs");
		food.setExpirationDate("1977");
		food.setQuantity(14);

		// add the food
		p.addFood(food);

		// save pantry
		pRepo.save(p);

		// test the user is correct
		when(usersRepo.findByUsername(user1.getUsername())).thenReturn(user1);
		assertEquals("Max", user1.getFirstName());

		// test the pantry contains the correct food
		when(pRepo.findBySessionKey(user1.getSessionKey())).thenReturn(p);
		assertEquals(true, p.contains(food));

	}

	@Test
	public void pantryDelete() {

		Users user1 = new Users();
		user1.setFirstName("Max");
		user1.setLastName("Solaro");
		user1.setUsername("msolaro");
		user1.setSessionKey("testing");

		// create the pantry
		Pantry p = new Pantry(user1.getSessionKey());

		// create a food item
		Item food = new Item();
		food.setName("Toast");
		food.setCategory("Carbs");
		food.setExpirationDate("1977");
		food.setQuantity(14);

		// add the food
		p.addFood(food);

		// save pantry
		pRepo.save(p);

		// test the user is correct
		when(usersRepo.findByUsername(user1.getUsername())).thenReturn(user1);
		assertEquals("Max", user1.getFirstName());

		// test the pantry contains the correct food
		when(pRepo.findBySessionKey(user1.getSessionKey())).thenReturn(p);
		assertEquals(true, p.contains(food));

		// try removing the food
		p.deleteFood(food.getName());
		pRepo.save(p);

		// test if pantry deleated food
		when(pRepo.findBySessionKey(user1.getSessionKey())).thenReturn(p);
		assertEquals(false, p.contains(food));

	}

	@Test
	public void pantrySize() {

		Users user1 = new Users();
		user1.setFirstName("Max");
		user1.setLastName("Solaro");
		user1.setUsername("msolaro");
		user1.setSessionKey("testing");

		// create the pantry
		Pantry p = new Pantry(user1.getSessionKey());

		// create a food item
		Item food = new Item();
		food.setName("Toast");
		food.setCategory("Carbs");
		food.setExpirationDate("1977");
		food.setQuantity(14);

		// adding new food
		// create a food item
		Item food2 = new Item();
		food2.setName("Ice");
		food2.setCategory("water");
		food2.setExpirationDate("never");
		food2.setQuantity(7);

		// add the food
		p.addFood(food2);
		p.addFood(food);

		// save pantry
		pRepo.save(p);

		// check size of pantry
		when(pRepo.findBySessionKey(user1.getSessionKey())).thenReturn(p);
		assertEquals(2, p.getAll().size());
	}

	@Test
	public void twoPantries() {
		// two users
		Users user1 = new Users();
		user1.setFirstName("Max");
		user1.setLastName("Solaro");
		user1.setUsername("msolaro");
		user1.setSessionKey("testing");

		Users user2 = new Users();
		user2.setFirstName("Emily");
		user2.setLastName("Kinee");
		user2.setSessionKey("nottesting");

		// create the pantries
		Pantry p = new Pantry(user1.getSessionKey());
		Pantry p2 = new Pantry(user2.getSessionKey());

		// create a food item
		Item food = new Item();
		food.setName("Toast");
		food.setCategory("Carbs");
		food.setExpirationDate("1977");
		food.setQuantity(14);

		// adding new food
		// create a food item
		Item food2 = new Item();
		food2.setName("Ice");
		food2.setCategory("water");
		food2.setExpirationDate("never");
		food2.setQuantity(7);

		// add the food
		p.addFood(food);
		p2.addFood(food2);

		// save pantry
		pRepo.save(p);
		pRepo.save(p2);

		// check size of pantry 1
		when(pRepo.findBySessionKey(user1.getSessionKey())).thenReturn(p);
		assertEquals(1, p.getAll().size());

		// check size of pantry 2
		when(pRepo.findBySessionKey(user2.getSessionKey())).thenReturn(p2);
		assertEquals(1, p2.getAll().size());

		// test the pantry contains the correct food 2
		when(pRepo.findBySessionKey(user1.getSessionKey())).thenReturn(p);
		assertEquals(true, p.contains(food));

		// test the pantry contains the correct food 1
		when(pRepo.findBySessionKey(user2.getSessionKey())).thenReturn(p2);
		assertEquals(true, p2.contains(food2));

	}

}
