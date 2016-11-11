/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */


import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas 
					implements FacePamphletConstants {
	
	/*
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the display
	 */
	public FacePamphletCanvas() {
		
		// Initialize GLabels //
		appMessage = new GLabel("");
		profileName = new GLabel("");

	}

	
	/* 
	 * This method displays a message string near the bottom of the 
	 * canvas.  Every time this method is called, the previously 
	 * displayed message (if any) is replaced by the new message text 
	 * passed in.
	 */
	public void showMessage(String msg) {
		// Displays message at the bottom of the canvas
		appMessage.setLabel(msg);
		appMessage.setFont(MESSAGE_FONT);
		add(appMessage, (getWidth() - appMessage.getWidth()) /2, getHeight() - BOTTOM_MESSAGE_MARGIN);
	}
	
	
	/* 
	 * This method displays the given profile on the canvas.  The 
	 * canvas is first cleared of all existing items (including 
	 * messages displayed near the bottom of the screen) and then the 
	 * given profile is displayed.  The profile display includes the 
	 * name of the user from the profile, the corresponding image 
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		// Clear the canvas //
		removeAll(); 
		
		if (profile != null) {
			
			// Add name to profile //
			addName(profile.getName());
			
			// Profile image //
			addProfileImage(profile);
						
			// Status //
			addStatus(profile);
			
			// Friends //
			addFriendHeader();
			addFriends(profile);
			
		}	
	}
	
	/* add a GLabel to canvas with the profile name */
	private void addName(String name) {
		profileName.setLabel(name);
		profileName.setFont(PROFILE_NAME_FONT);
		profileName.setColor(Color.BLUE);
		add(profileName, getLeftColumnX(), getNameY(profileName));
	}
	
	 
	/* this method checks to see if there is an image filepath provided
	 * if so, it scales the image and calls addImage method to add it to the canvas
	 */
	private void addProfileImage(FacePamphletProfile prof) {
		if (prof.getImage() != null) {
			profileImage = prof.getImage();
			profileImage.setSize(IMAGE_WIDTH, IMAGE_HEIGHT); // resizes GImage to set bounds
			addImage(profileImage);
		} else {
			emptyImage = new GRect(IMAGE_WIDTH, IMAGE_HEIGHT);
			addImage(emptyImage);
		}
	}
	
	/* This method adds a GObject at the coordinates specified for the profile image */
	private void addImage(GObject obj) {
		add(obj, getLeftColumnX(), getImageY());
	}
	
	/* This method sets and adds the profileStatus GLabel */
	private void addStatus(FacePamphletProfile prof) {
		String status = prof.getStatus();
		if (status != null && status.length() > 0) {
			profileStatus = new GLabel(status);
		} else {
			profileStatus = new GLabel("No current status");
		}
		// add status to canvas //
		profileStatus.setFont(PROFILE_STATUS_FONT);
		add(profileStatus, getLeftColumnX(), getStatusY());	
	}
	
	/*
	 * This method adds the header "Friends:" to the canvas
	 */
	private void addFriendHeader() {
		friendHeader = new GLabel("Friends:");
		friendHeader.setFont(PROFILE_FRIEND_LABEL_FONT);
		add(friendHeader, getRightColumnX(), getFriendHeaderY(friendHeader));
	}
	
	/*
	 * This method iterates through FacePamphletProfile prof's list of friends
	 * to create a GLabel for each friend, to be displayed beneath the friendHeader
	 */
	
	private void addFriends(FacePamphletProfile prof) {
		Iterator<String> it = prof.getFriends(); 
		int i = 0;
		while (it.hasNext()) {
			addFriendsListToCanvas(it.next(), i);
			i++;
		}
	}
	
	/*
	 * This method adds a GLabel to the canvas for each friend of the current profile
	 * displays each profile name, stacked vertically below the friendHeader
	 */
	
	private void addFriendsListToCanvas(String str, int i) {
		GLabel friendsName = new GLabel(str);
		friendsName.setFont(PROFILE_FRIEND_FONT);
		add(friendsName, getRightColumnX(), getFriendsListY(friendsName, i));
	}
	
	
	/* GObject coordinate methods */
	
	private double getLeftColumnX() {
		return LEFT_MARGIN;
	}
	
	private double getNameY(GLabel label) {
		return TOP_MARGIN + label.getAscent();
	}
	
	private double getImageY() {
		return getNameY(profileName) + IMAGE_MARGIN;
	}
	
	private double getStatusY() {
		return getImageY() + IMAGE_HEIGHT + IMAGE_MARGIN + STATUS_MARGIN;
	}
	
	private double getRightColumnX() {
		return APPLICATION_WIDTH / 2.0;
	}
	
	private double getFriendHeaderY(GLabel label) {
		return getImageY() - label.getAscent();
	}
	
	private double getFriendsListY(GLabel label, int i) {
		return getFriendHeaderY(friendHeader) + + friendHeader.getHeight() + label.getHeight() * i;
	}
	
	// Private instance variables //
	
	GImage profileImage;
	GRect emptyImage;
	GLabel profileName;
	GLabel appMessage;
	GLabel profileStatus;
	GCompound profile;
	GLabel friendHeader;
}
