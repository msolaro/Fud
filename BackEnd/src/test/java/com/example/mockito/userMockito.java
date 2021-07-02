package com.example.mockito;

import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.demo.households.*;
import com.example.demo.users.Users;
import com.example.demo.users.UsersRepository;

/**
 * 
 * @author rlspick
 *
 */
public class userMockito {

	@InjectMocks
	Households household = new Households();

	@Mock
	UsersRepository usersRepo;

	@Mock
	HouseholdsRepository householdRepo;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateUser() {

		Users user1 = new Users();
		user1.setFirstName("Riley");
		user1.setLastName("Spick");

		List<Users> list = new ArrayList<>();
		list.add(user1);

		when(usersRepo.findAll()).thenReturn(list);

		assertEquals(1, usersRepo.findAll().size());

	}

	@Test
	public void testCreateMultipleUser() {

		Users user1 = new Users();
		user1.setFirstName("Riley");
		user1.setLastName("Spick");

		Users user2 = new Users();
		user2.setFirstName("Brian");
		user2.setLastName("Hendrickson");

		Users user3 = new Users();
		user3.setFirstName("Donna");
		user3.setLastName("Young");

		List<Users> list = new ArrayList<>();
		list.add(user1);
		list.add(user2);
		list.add(user3);

		when(usersRepo.findAll()).thenReturn(list);

		assertEquals(3, usersRepo.findAll().size());

	}

	@Test
	public void checkUserExistsInDB() {

		Users user1 = new Users();
		user1.setFirstName("Riley");
		user1.setLastName("Spick");
		user1.setUsername("rspick");

		when(usersRepo.existsByUsername(user1.getUsername())).thenReturn(true);

		assertEquals(true, usersRepo.existsByUsername(user1.getUsername()));
	}
	
	@Test
	public void resetUserPassword() {
		
		Users user1 = new Users();
		user1.setFirstName("Riley");
		user1.setLastName("Spick");
		user1.setUsername("rspick");
		user1.setPassword("password1");
		
		when(usersRepo.findByUsername("rlspick")).thenReturn(user1);
		user1.setPassword("newPass");
		
		assertEquals("newPass", usersRepo.findByUsername("rlspick").getPassword());
		
	}
	
	@Test 
	public void resetUserUsername() {

		Users user1 = new Users();
		user1.setFirstName("Riley");
		user1.setLastName("Spick");
		user1.setUsername("rspick");
		
		when(usersRepo.findBySessionKey(user1.getSessionKey())).thenReturn(user1);
		user1.setUsername("rlspick");
		
		assertEquals("rlspick", usersRepo.findBySessionKey(user1.getSessionKey()).getUsername());
	}
	
	@Test
	public void resetUserFirstName() {
		
		Users user1 = new Users();
		user1.setFirstName("Riley");
		user1.setLastName("Spick");
		user1.setUsername("rspick");
		
		when(usersRepo.findBySessionKey(user1.getSessionKey())).thenReturn(user1);
		user1.setFirstName("Bert");
		
		assertEquals("Bert", usersRepo.findBySessionKey(user1.getSessionKey()).getFirstName());
	}
	
	@Test
	public void resetUserLastName() {
		
		Users user1 = new Users();
		user1.setFirstName("Riley");
		user1.setLastName("Spick");
		user1.setUsername("rspick");
		
		when(usersRepo.findBySessionKey(user1.getSessionKey())).thenReturn(user1);
		user1.setLastName("Mason");
		
		assertEquals("Mason", usersRepo.findBySessionKey(user1.getSessionKey()).getLastName());
	}
}
