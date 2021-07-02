package com.example.fud.Models;

/**
 * This class is a model for the user
 */
public class UserModel {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    /**
     * This is a constructor for the user model
     * @param firstName User's first name
     * @param lastName User's last name
     * @param email User's email
     * @param password User's password
     */
    public UserModel(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the first name of the user
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the email of the user.
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password of the user.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
