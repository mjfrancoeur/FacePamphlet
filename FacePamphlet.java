/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program 
					implements FacePamphletConstants {

	/* For export as a JAR file */
	public static void main(String[] args) {
		new FacePamphlet().start(args);
	}
	
	
	/*
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		
		// Add FacePamphletCanvas //
		canvas = new FacePamphletCanvas();
		add(canvas);
	
		// Initialize database //
		db = new FacePamphletDatabase();
		
		// Initialize interactors //
		nameTextField = new JTextField(TEXT_FIELD_SIZE);
		addNameButton = new JButton("Add");
		deleteNameButton = new JButton("Delete");
		lookupNameButton = new JButton("Lookup");
		
		statusTextField = new JTextField(TEXT_FIELD_SIZE);
		
		changeStatusButton = new JButton("Change Status");
		
		pictureTextField = new JTextField(TEXT_FIELD_SIZE);
		
		changePictureButton = new JButton("Change Picture");
		
		friendTextField = new JTextField(TEXT_FIELD_SIZE);
		
		addFriendButton = new JButton("Add Friend");
		
	    // Add interactors //
		
		add(nameTextField, NORTH);
		add(addNameButton, NORTH);
		add(deleteNameButton, NORTH);
		add(lookupNameButton, NORTH);
		
		add(statusTextField, WEST);
		add(changeStatusButton, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		add(pictureTextField, WEST);
		add(changePictureButton, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		add(friendTextField, WEST);
		add(addFriendButton, WEST);
		
		// Add action listeners //
		addActionListeners();
		statusTextField.addActionListener(this); // add action listener for JTextField
		pictureTextField.addActionListener(this); // add action listener for JTextField
		friendTextField.addActionListener(this); // add action listener for JTextField
		
		// Test //
		canvas.showMessage("Hello, world!");
		canvas.displayProfile(currentProfile);
    }
    
  
    /*
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent e) {
    	
    	Object source = e.getSource();
		
    	// Confirms source and performs source JComponent action //
    	
    	if(checkInteractors(source, changeStatusButton, statusTextField)) {
			changeStatus();	
    	} else if (checkInteractors(source, changePictureButton, pictureTextField)) {
			changePicture();
		} else if (checkInteractors(source, addFriendButton, friendTextField)) {
			addFriend();
		} else if (checkNameInteractors(source, addNameButton)) {
			addProfile();
		} else if (checkNameInteractors(source, deleteNameButton)) {
			deleteProfile();
		} else if (checkNameInteractors(source, lookupNameButton)) {
			lookupProfile();
		}
    }
    
    /*
     * This method returns true if the source is JComponent button of JTextField tf   
     * AND if JTextField tf contains text.
     */   
    private boolean checkInteractors(Object source, JComponent button, JTextField tf) {
       	if((source == button) || (source == tf)) {
			if (checkForText(tf)) {
				return true;
			}
    	}
    	return false;
    }
    
   
    /* This method returns true if text has been entered into JTextField tf. */
    private boolean checkForText(JTextField tf) {
       	String text = tf.getText();
    	if (text != null && text.length() > 0) {
			return true;
		}
    	return false;
    }
    
    /*
     * This method returns true if source is one of the namefield buttons
     * and if text has been entered in nameTextField.
     */
    private boolean checkNameInteractors(Object source, JComponent button) {
    	// Returns true if source comes from one of the Name field buttons and if there is text in the nameTextField //
    	String name = nameTextField.getText();
    	if(source == button && (name != null && name.length() > 0)) {
    		return true;
    	}
    	return false;
    }
    
    /*
     * This method changes the profile's status to the String entered into statusTextField
     * and updates the profile to display the status.
     */
    private void changeStatus() {
    	String status = statusTextField.getText();
       	if (isProfileSelected()) {
       		currentProfile.setStatus(status);
       		canvas.displayProfile(currentProfile);
       	}
       	
    }
    
    /* 
     * This method uploads the filename provided in the pictureTextField and if valid,
     * displays as the new profile picture.
     */
    private void changePicture() {
    	String filename = pictureTextField.getText();
       	if (isProfileSelected()) {
       		addImage(filename);
       	}
    }
    
    /* Adds friend to profile's network. */
    private void addFriend() {
    	
    	String name = friendTextField.getText();    	
    	// If all conditions are valid, will add the profile named in friendTextField
    	// to the current profile's list of friends (and performs reciprocal friending)
    	// if successful, displays message and adds the new friend under the "Friends:" header on the canvas.
    	if (isAddFriendValid(name)) {
    		currentProfile.addFriend(name);
			db.getProfile(name).addFriend(currentProfile.getName());
			canvas.displayProfile(currentProfile);
			canvas.showMessage(name + " is now your friend. Release the dopamine!");
    	}
	}
    
    /**
     * This method returns true if a profile has been selected.
     */
    private Boolean isProfileSelected() {
		if (currentProfile == null) {
    		canvas.showMessage("You haven't picked a profile yet. Fool of a Took!");
    		return false;
    	}
		return true;
	}

    /*
     * This method attempts to live a normal life outside of the public eye
     * it also sets the current profile's image and updates the display
     * if the file name is invalid, it throws an error and displays an error message. 
     */
	private void addImage(String file) {
   		GImage image = null;
   		try {
   			image = new GImage(file);
   			currentProfile.setImage(image);
   			canvas.displayProfile(currentProfile);
   		} catch (ErrorException ex) {
   			// Executed if filename cannot be opened
   			canvas.showMessage("YOU SHALL NOT PASS! Just kidding, but tbh I couldn't find a file with that name.");
   		}
	}
	
	/*
	 * If all conditions are met, returns true, else returns false.
	 * Conditions:
	 * A profile has been selected
	 * The profile exists
	 * The friend being added also is a valid profile
	 */
    private boolean isAddFriendValid(String name) {
    	if (!isProfileSelected()) {
    		return false;
    	}
    	if (!db.containsProfile(name)) {
    		canvas.showMessage("That profile doesn't seem to exist...");
    		return false;
    	}
    	if (currentProfile.containsFriend(name)) {
    		canvas.showMessage("You've already friended " + name + ". Play it cool, man.");
    		return false;
    	}
    	return true;
    }
    
    /*
     * This method creates a new FacePamphletProfile
     * and sets it to the current profile.
     */
    private void addProfile() {
    	
    	// gets profile name from nameTextField //
    	String profName = nameTextField.getText();
    	
    	// if profile already exists
    	if (db.containsProfile(profName)) {
    		currentProfile = db.getProfile(profName);
    		canvas.showMessage("Added new profile");
    		canvas.showMessage(profName + "'s profile already exists. Plz hold while we redirect you to our anti-fraud hotline.");
    	} else {
    		FacePamphletProfile newProfile = new FacePamphletProfile(profName);
    		db.addProfile(newProfile);
    		currentProfile = newProfile;
    		canvas.displayProfile(currentProfile);
    		canvas.showMessage(profName + " now has a profile! Keep it secret, keep it safe.");
    	}
    }
    
    /*
     * This method deletes the profile that matches the string entered in NameTextField
     * and removes the profile from other profiles' friend list.
     */
    private void deleteProfile() {
    	String profName = nameTextField.getText();
    	currentProfile = null;
    	if (db.containsProfile(profName)) {
    		db.deleteProfile(profName);
    		canvas.displayProfile(currentProfile);
    		canvas.showMessage("profile deleted");
    	} else {
    		canvas.displayProfile(currentProfile);
    		canvas.showMessage("A profile with that name does not exist. A girl is no one.");
    	}
    }
    
    /*
     * This method re-assigns currentProfile to the FacePamphletProfile with
     * the name that corresponds to the string entered into NameTextField.
     */
    private void lookupProfile() {
    	String profName = nameTextField.getText();
    	if (db.containsProfile(profName)) {
    		canvas.showMessage("Lookup :" + db.getProfile(profName).toString());
    		currentProfile = db.getProfile(profName);
    		canvas.displayProfile(currentProfile);
    	} else {
    		currentProfile = null;
    		canvas.displayProfile(currentProfile);
    		canvas.showMessage("A profile with that name does not exist. A girl is no one.");
    	}
    }
    
    // Private instance variables //
    
    // Interactors //
    private JTextField nameTextField;
    private JButton addNameButton;
    private JButton deleteNameButton;
    private JButton lookupNameButton;
    
    private JTextField statusTextField;
    private JButton changeStatusButton;
    
    private JTextField pictureTextField;
    private JButton changePictureButton;
    
    private JTextField friendTextField;
    private JButton addFriendButton;
    
    // Canvas //
    private FacePamphletCanvas canvas;
    
    // Database //
    private FacePamphletDatabase db;
    
    // Current profile //
    private FacePamphletProfile currentProfile;
    
    
    
}
