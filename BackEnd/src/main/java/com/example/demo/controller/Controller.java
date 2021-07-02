package com.example.demo.controller;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.groceryList.GroceryList;
import com.example.demo.groceryList.GroceryListRepository;
import com.example.demo.households.Households;
import com.example.demo.households.HouseholdsRepository;
import com.example.demo.item.Item;
import com.example.demo.pantry.Pantry;
import com.example.demo.pantry.PantryRepository;
import com.example.demo.users.Users;
import com.example.demo.users.UsersRepository;
import com.example.demo.vo.createHouseholdVO;
import com.example.demo.vo.gListVO;
import com.example.demo.vo.loginVO;
import com.example.demo.vo.pantryVO;

@RestController
public class Controller {

	private final UsersRepository usersRepo;
	private final HouseholdsRepository houseRepo;
	private final GroceryListRepository groceryListRepo;
	private final PantryRepository pantryRepo;

	Controller(UsersRepository usersRepo, HouseholdsRepository houseRepo, GroceryListRepository groceryListRepo, PantryRepository pantryRepo) {
		this.usersRepo = usersRepo;
		this.houseRepo = houseRepo;
		this.groceryListRepo = groceryListRepo;
		this.pantryRepo = pantryRepo;
	
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(Map<String, Object> model) {
		return "welcome";
	}
	
/**
 * for testing
 * @param id
 * @return
 */
	@GetMapping("/users/{id}")
	Users one(@PathVariable Long id) {

		return usersRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}
	
	/**
	 * get mapping for specific users by ID
	 * 
	 * @param id
	 * @return User json?
	 */
	@GetMapping("/house")
	List<Households> houses() {

		return houseRepo.findAll();
	}
	
	@GetMapping("/glist")
	List<GroceryList> glists(){
		return groceryListRepo.findAll();
	}
	
	//should change this return type to loginVO
	@PostMapping("/registration")
	Users newUser(@RequestBody Users newUser) throws NoSuchAlgorithmException {
		Users t = new Users();
		t = newUser;
		String pass = t.getPassword();
		// store hashed passwords
		if(usersRepo.existsByUsername(newUser.getUsername()))
			return newUser;
		
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		BigInteger number = new BigInteger(1, md.digest(pass.getBytes(StandardCharsets.UTF_8)));
		StringBuilder hexString = new StringBuilder(number.toString(16));

		// Pad with leading zeros
		while (hexString.length() < 32) {
			hexString.insert(0, '0');
		}

		pass = hexString.toString();
		t.setPassword(pass);
		t.setHousehold("unassigned");
		t.setSessionKey(t.getUsername());
		//create pantry for user
		GroceryList g = new GroceryList();
		g.setGroceryListName(t.getUsername());
		groceryListRepo.save(g);
		
		Pantry p = new Pantry(t.getUsername());
		pantryRepo.save(p);
		return usersRepo.save(t);
	}

	/**
	 * 
	 * @param loginInfo -- username, password
	 * @return Sessionkey
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping(value = "/login", produces = "application/json")
	loginVO login(@RequestBody Users loginInfo) throws NoSuchAlgorithmException {
		String sessionKey = new String();
		loginVO ret = new loginVO();
		Users t = new Users();
		ret.setMessage("invalid");
		// Database stores hashed passwords so gotta hash the incoming password to check
		String pass = loginInfo.getPassword();
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		BigInteger number = new BigInteger(1, md.digest(pass.getBytes(StandardCharsets.UTF_8)));
		StringBuilder hexString = new StringBuilder(number.toString(16));

		// Pad with leading zeros
		while (hexString.length() < 32) {
			hexString.insert(0, '0');
		}

		if(usersRepo.existsByUsername(loginInfo.getUsername()))
		{
		pass = hexString.toString();
		t = usersRepo.findByUsername(loginInfo.getUsername());
				if (t.getPassword().compareTo(pass) == 0) {
					// generate session Key
					sessionKey += System.currentTimeMillis() + t.getPassword();
					MessageDigest md2 = MessageDigest.getInstance("SHA-256");
					BigInteger number2 = new BigInteger(1, md2.digest(sessionKey.getBytes(StandardCharsets.UTF_8)));
					StringBuilder hexString2 = new StringBuilder(number2.toString(16));

					// Pad with leading zeros
					while (hexString2.length() < 32) {
						hexString2.insert(0, '0');
					}	
					sessionKey = hexString2.toString();
					t.setSessionKey(sessionKey);
					ret.setToken(sessionKey);
					ret.setMessage("valid");
					
					//update/create? users grocerylist with new sessionkey
					//this might mean a new grocerylist every time should double check
					GroceryList g = groceryListRepo.findByGroceryListName(loginInfo.getUsername());
					g.setSessionKey(sessionKey);
					
					//update users pantry with new sessionkey.
					Pantry p = pantryRepo.findByPantryName(t.getUsername());
					p.setSessionKey(sessionKey);
					
					groceryListRepo.save(g);
					pantryRepo.save(p);
					usersRepo.save(t);
					return ret;
				}
				ret.setMessage("incorrect password");
				return ret;
			
		}
		ret.setMessage("User not found");
		ret.setToken("false");
		return ret;
	}

	/**
	 * mapping to replace a User
	 * 
	 * @param newUser object
	 * @param old     users id
	 * @return
	 */
	@PutMapping("/users/{id}") // try and do it all in the request body
	Users replaceUser(@RequestBody Users newUser, @PathVariable Long id) {

		return usersRepo.findById(id).map(user -> {
			user.setUsername(newUser.getUsername());
			user.setPassword(newUser.getPassword());
			user.setFirstName(newUser.getFirstName());
			user.setLastName(newUser.getLastName());
			user.setHousehold(newUser.getHousehold());
			return usersRepo.save(user);
		}).orElseGet(() -> {
			newUser.setId(id);
			return usersRepo.save(newUser);
		});
	}

	/**
	 * 
	 * @param loginInfo -- username, new password, sessionkey
	 * @return true / false denoting whether successful
	 */
	@PutMapping("/users/passwordChange")
	createHouseholdVO pChange(@RequestBody Users loginInfo) throws NoSuchAlgorithmException {
		createHouseholdVO ret = new createHouseholdVO();
		ret.setStatus(false);
		Users t = new Users();
		t = usersRepo.findByUsername(loginInfo.getUsername());
				if (t.getSessionKey().compareTo(loginInfo.getSessionKey()) == 0) {
					// found the correct user

					// store hashed passwords
					String pass = loginInfo.getPassword();
					MessageDigest md = MessageDigest.getInstance("SHA-256");
					BigInteger number = new BigInteger(1, md.digest(pass.getBytes(StandardCharsets.UTF_8)));
					StringBuilder hexString = new StringBuilder(number.toString(16));

					// Pad with leading zeros
					while (hexString.length() < 32) {
						hexString.insert(0, '0');
					}

					pass = hexString.toString();
					t.setPassword(pass);
					usersRepo.save(t);
					ret.setStatus(true);
					ret.setMessage("Successfully changed password");
					return ret;
				}
		ret.setMessage("Failed to change password");
		return ret;

	}

	/**
	 * 
	 * @param loginInfo -- username, sessionkey, 
	 * @return
	 */
	@PostMapping("/users/newHousehold")
	createHouseholdVO createHousehold(@RequestBody Users loginInfo) {

		createHouseholdVO ret = new createHouseholdVO();
		ret.setStatus(false);
		
		Users t = new Users();
		t = usersRepo.findByUsername(loginInfo.getUsername());
		String housename = loginInfo.getHousehold();
		// look for user matching sessionkey
		if (t.getSessionKey().compareTo(loginInfo.getSessionKey()) == 0) {
			//going to have to check a few things first.
			//1. if user is already in a household throw error
			if(t.getHousehold().compareTo("unassigned") != 0)
			{
				ret.setMessage("Already in a house");
				return ret;
			}
			//2. Check if household name already exists.)
			if(houseRepo.existsHouseholdsByHousehold(housename))
			{
				ret.setMessage("House name already exists");
				return ret;
			}
				
			Households h = new Households();
			h.setUsername(t.getUsername());
			h.setHousehold(housename);
			t.setHousehold(housename);
			usersRepo.save(t);
			houseRepo.save(h);
			ret.setStatus(true);
			ret.setMessage("Household Created Successfully");
			return ret;
		}
		ret.setMessage("Authentication Failed");
		return ret;

	}
	
	
	
	/**
	 * 
	 * @param loginInfo -- sessionkey, newhousehold, password
	 * @return true/false denoting whether or not user was found / household was
	 *         found
	 */
	@PostMapping("/users/joinHousehold")
	createHouseholdVO joinHousehold(@RequestBody Users loginInfo) {

		//need a pin and a sessionkey....
		//can use loginInfo and just call password pin yea yeah good idea
		String pin = loginInfo.getPassword();

		
		
		createHouseholdVO ret = new createHouseholdVO();
		ret.setStatus(false);
		Users u = new Users();
		
		//ok so that was to check if the user exists so that i can then check if sessionkeys are equal
			if (usersRepo.existsBySessionKey(loginInfo.getSessionKey())) {
				
				//find our user that exists based off session key
				u = usersRepo.findBySessionKey(loginInfo.getSessionKey());
				String housename = loginInfo.getHousehold();
				if(houseRepo.existsByHousehold(housename))
				{
					//household exists
					Households h = houseRepo.findByHousehold(housename);
					
					//check if the pin is correct
					if(pin.compareTo(h.getPin()) == 0)
					{
					//adds roomate id to household
					h.addRoomate(u.getID());
					u.setHousehold(housename);
					houseRepo.save(h);
					usersRepo.save(u);
					ret.setMessage("Succesfully joined household");
					ret.setStatus(true);
					return ret;
					}
					ret.setMessage("Invalid Pin");
					return ret;
				}
				//not super sure how to do this
				ret.setMessage("Household could not be joined because household does not exist");
				return ret;
			}

		ret.setMessage("Authentication Error");
		return ret;

	}
	
	/**
	 * 
	 * @param loginInfo -- sessionkey,
	 * @return true/false denoting whether or not user was found / household was
	 *         found
	 */
	@PutMapping("/users/leaveHousehold")
	createHouseholdVO leaveHousehold(@RequestBody Users loginInfo) {

		createHouseholdVO ret = new createHouseholdVO();
		ret.setStatus(false);
		Users u = new Users();
		// look for user matching sessionkey
		u = usersRepo.findBySessionKey(loginInfo.getSessionKey());
		String housename = u.getHousehold();
			if (u.getSessionKey().compareTo(loginInfo.getSessionKey()) == 0) {
					//household exists
					Households h = houseRepo.findByHousehold(housename);
					//adds roomate id to household
					h.removeRoomate(u.getID());
					u.setHousehold("unassigned");
					houseRepo.save(h);
					usersRepo.save(u);
					ret.setMessage("Succesfully left household");
					ret.setStatus(true);
					return ret;

			}

		ret.setMessage("Authentication Error");
		return ret;

	}
	
	@PostMapping("/hh/generatepin")
	createHouseholdVO generatePin(@RequestBody Users loginInfo) {
	//first have to check if user is in a household
		Users current = loginInfo;
		createHouseholdVO ret = new createHouseholdVO();
		
		
		//check if a user exists with sessionKey
		if(usersRepo.existsBySessionKey(current.getSessionKey()))
		{
			current = usersRepo.findBySessionKey(loginInfo.getSessionKey());
			//now check if a household with sessionkey exists
			Households h = houseRepo.findByHousehold(current.getHousehold());
			ret.setMessage(h.newPin());
			houseRepo.save(h);
			ret.setStatus(true);
		}
		else
		{
			ret.setMessage("User not found");
			ret.setStatus(false);
		}
		return ret;
				
	}

	//-------------------------end of households----------------------------------
	
	 // ------------------------ grocery list-----------------------------------
	
	/**
	 * Put mapping for updating a groceryList
	 * @param Username, sessionkey, food?
	 * @param GroceryList
	 */
	@PutMapping("/groceryList/addItem")
	createHouseholdVO addFood(@RequestBody GroceryList food)
	{
		//return object
		createHouseholdVO ret = new createHouseholdVO();
		ret.setStatus(false);
		//get sessionkey sent to us
		String sessionkey = food.getSessionKey();
		//check if glist with sessionKey exists
		if(groceryListRepo.existsBySessionKey(sessionkey))
		{
			GroceryList g = groceryListRepo.findBySessionKey(sessionkey);
			g.addItem(food.getFood());
			groceryListRepo.save(g);
			ret.setStatus(true);
			ret.setMessage("successfully added food");
			return ret;
		}
			
		
		ret.setMessage("User not found");
		return ret;
		
	}
	
	/**
	 * 
	 * @param Users info -- sessionKey
	 * @return
	 */
	@PostMapping("/groceryList/getItems")
	gListVO getGList(@RequestBody Users info)
	{
		gListVO ret = new gListVO();
		ret.setStatus(false);
		String sessionKey = info.getSessionKey();
		if(groceryListRepo.existsBySessionKey(sessionKey))
		{
			GroceryList g = groceryListRepo.findBySessionKey(sessionKey);
			ret.setItems(g.getgList_items());
			ret.setStatus(true);
			ret.setMessage("Success");
			return ret;
		}
		ret.setMessage("Failed authentication");
		return ret;
		
	}
	
	@PostMapping("/groceryList/deleteItem")
	createHouseholdVO deleteItem(@RequestBody GroceryList food) {

		// return object
		createHouseholdVO ret = new createHouseholdVO();
		ret.setStatus(false);
		// get sessionkey sent to us
		String sessionkey = food.getSessionKey();
		// check if glist with sessionKey exists
		if (groceryListRepo.existsBySessionKey(sessionkey)) {
			GroceryList g = groceryListRepo.findBySessionKey(sessionkey);
			if(g.removeItem(food.getFood()))
			{
				groceryListRepo.save(g);
				ret.setStatus(true);
				ret.setMessage("successfully removed food");
				return ret;
			}
			
			ret.setStatus(false);
			ret.setMessage("Specified food did not exist");
			return ret;
		}

		ret.setMessage("User not found");
		return ret;

	}
	//----------------------------------------end of groceryList---------------------

	/**
	 * Mapping for delete requests by user ID
	 * 
	 * @param id
	 */
	@DeleteMapping("/users/{id}")
	void deleteUser(@PathVariable Long id) {
		usersRepo.deleteById(id);
	}
	
	//----------------------pantry controllers----------------------
	
	@PostMapping("/pantry/add")
	createHouseholdVO pantryAdd(@RequestBody Item in) {
		// return object
		createHouseholdVO ret = new createHouseholdVO();
		ret.setStatus(false);
		// get sessionkey sent to us
		String sessionkey = in.getSessionKey();
		// check if pantry with sessionKey exists
		if (pantryRepo.existsBySessionKey(sessionkey)) {
			//find users pantry
			Pantry p = pantryRepo.findBySessionKey(sessionkey);
			Item t = in;
			
			if(!p.addFood(t))
			{
			// item was added succesfully
				pantryRepo.save(p);
				//save users pantry
				ret.setStatus(true);
				ret.setMessage("Food updated");
				return ret;
			}
			else
			{
				ret.setMessage("Food Added");
				pantryRepo.save(p);
				ret.setStatus(true);
				return ret;
			}
			
		}

		ret.setMessage("User not found");
		return ret;

	}
	
	@PostMapping("/pantry/delete")
	createHouseholdVO pantrydelete(@RequestBody Item in) {

		// return object
		createHouseholdVO ret = new createHouseholdVO();
		ret.setStatus(false);
		// get sessionkey sent to us
		String sessionkey = in.getSessionKey();
		// check if pantry with sessionKey exists
		if (pantryRepo.existsBySessionKey(sessionkey)) {
			Pantry p = pantryRepo.findBySessionKey(sessionkey);
			Item t = in;
			
			//check if item exists in pantry
			if(!p.contains(in))
			{
				ret.setStatus(false);
				ret.setMessage("Item does not exist in pantry");
				return ret;
			}
			else {
				//might have to check quantity
			p.deleteFood(in.getName());
			pantryRepo.save(p);
			ret.setStatus(true);
			ret.setMessage("Food deleted");
			return ret;
			}
		}

		ret.setMessage("User not found");
		return ret;

	}
	
	@PostMapping("/pantry/get")
	pantryVO pantryGet(@RequestBody Item in) {

		List<Item> foods= new ArrayList<Item>();
		// return object
		pantryVO ret = new pantryVO();
		ret.setStatus(false);
		
		// get sessionkey sent to us
		String sessionkey = in.getSessionKey();
		
		// check if pantry with sessionKey exists
		if (pantryRepo.existsBySessionKey(sessionkey)) {
			Pantry p = pantryRepo.findBySessionKey(sessionkey);
			
			//pantry exists set p = our pantry
			foods = p.getAll();
			ret.setItems(foods);
			ret.setStatus(true);
			ret.setMessage("Found pantry");
			return ret;
			}
		

		ret.setMessage("User not found");
		return ret;

	}
	
	//-----------------------end of pantry------------------------------------
	
	@PostMapping("/getInfo")
	Users getInformation(@RequestBody Users in) {

		Users ret = new Users();
		String sessionkey = in.getSessionKey();
		//ret.setStatus(false);
		//if sessionkey exists
		if(usersRepo.existsBySessionKey(sessionkey))
		{
			Users current = usersRepo.findBySessionKey(in.getSessionKey());
			String name = current.getFirstName();
			ret = current;
			ret.setPassword("");
			ret.setSessionKey("");
			return ret;
		}
		return ret;

	}
	
	
	//-------------------shared household pantry---------------
	
	
	@PostMapping("/hhpantry/add")
	createHouseholdVO hhpantryAdd(@RequestBody Item in) {
		// return object
		createHouseholdVO ret = new createHouseholdVO();
		ret.setStatus(false);
		// get sessionkey sent to us
		if(usersRepo.existsBySessionKey(in.getSessionKey()))
		{
			//household in question is the users household
			String household = usersRepo.findBySessionKey(in.getSessionKey()).getHousehold();
			// check if house with housename from user exists
			if (houseRepo.existsByHousehold(household)) {
				//find users household
				Households h = houseRepo.findByHousehold(household);
				Item t = in;
				
				if(!h.addFood(t))
				{
				// item was added succesfully
					houseRepo.save(h);
					//save users pantry
					ret.setStatus(true);
					ret.setMessage("Food updated");
					return ret;
				}
				else
				{
					ret.setMessage("Food Added");
					houseRepo.save(h);
					ret.setStatus(true);
					return ret;
				}
				
			}
		}

		ret.setMessage("User not found");
		return ret;

	}
	
	@PostMapping("/hhpantry/delete")
	createHouseholdVO hhpantrydelete(@RequestBody Item in) {

		// return object
		createHouseholdVO ret = new createHouseholdVO();
		ret.setStatus(false);
		// get sessionkey sent to us
		String sessionkey = in.getSessionKey();
		
		//check for a user with that sessionkey
		if(usersRepo.existsBySessionKey(sessionkey))
		{
			Users current = usersRepo.findBySessionKey(sessionkey);
			//get the users current household
			
			// check if pantry with sessionKey exists
			if (houseRepo.existsByHousehold(current.getHousehold())) {
				Households h = houseRepo.findByHousehold(current.getHousehold());
				
				Item t = in;
				
				//check if item exists in pantry
				if(!h.contains(in))
				{
					ret.setStatus(false);
					ret.setMessage("Item does not exist in household pantry");
					return ret;
				}
				else {
					//might have to check quantity
				h.deleteFood(in.getName());
				houseRepo.save(h);
				ret.setStatus(true);
				ret.setMessage("Food deleted");
				return ret;
				}
			}
			//user not found
			ret.setMessage("User not found");
			return ret;
		}
		ret.setMessage("User not found");
		return ret;

	}
	
	@PostMapping("/hhpantry/get")
	pantryVO hhpantryGet(@RequestBody Item in) {

		List<Item> foods= new ArrayList<Item>();
		// return object
		pantryVO ret = new pantryVO();
		ret.setStatus(false);
		
		// get sessionkey sent to us
		String sessionkey = in.getSessionKey();
		if(usersRepo.existsBySessionKey(sessionkey))
		{
		//checks if there is a user with that exists with that session key
			
			//now we need to check if that users household exists
			Users current = usersRepo.findBySessionKey(sessionkey);
			if (houseRepo.existsByHousehold(current.getHousehold())) {
				// we have now established the household exists

				Households h = houseRepo.findByHousehold(current.getHousehold());

				// pantry exists set p = our pantry
				foods = h.getAll();
				ret.setItems(foods);
				ret.setStatus(true);
				ret.setMessage("Found household pantry");
				return ret;

			}
		}
	
		ret.setMessage("User not found");
		return ret;

	}
	

}
