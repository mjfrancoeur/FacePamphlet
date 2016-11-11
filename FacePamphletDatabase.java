/*
 * File: FacePamphletDatabase.java
 * -------------------------------
 * This class keeps track of the profiles of all users in the
 * FacePamphlet application.  Note that profile names are case
 * sensitive, so that "ALICE" and "alice" are NOT the same name.
 */

import java.util.*;

public class FacePamphletDatabase implements FacePamphletConstants {

	/* 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the database.
	 */
	public FacePamphletDatabase() {
		
	}
	
	
	/* 
	 * This method adds the given profile to the database.  If the 
	 * name associated with the profile is the same as an existing 
	 * name in the database, the existing profile is replaced by 
	 * the new profile passed in.
	 */
	public void addProfile(FacePamphletProfile profile) {
		friendsDatabase.put(profile.getName(), profile);
	}

	
	/* 
	 * This method returns the profile associated with the given name 
	 * in the database.  If there is no profile in the database with 
	 * the given name, the method returns null.
	 */
	public FacePamphletProfile getProfile(String name) {
		if (friendsDatabase.containsKey(name)) {
			return friendsDatabase.get(name);
		}
		return null;
	}
	
	
	/* 
	 * This method removes the profile associated with the given name
	 * from the database.  It also updates the list of friends of all
	 * other profiles in the database to make sure that this name is
	 * removed from the list of friends of any other profile.
	 * 
	 * If there is no profile in the database with the given name, then
	 * the database is unchanged after calling this method.
	 */
	public void deleteProfile(String name) {
		friendsDatabase.remove(name);
		
		// Iterates through database, removing name when it is among other profile's list of friends //
		for (String friend : friendsDatabase.keySet()) {
			FacePamphletProfile currentProfile = friendsDatabase.get(friend);
			
			if (isFriendsWith(currentProfile, name)) {
				currentProfile.removeFriend(name);
			}
		}
	}
	
	/*
	 * This method returns true if FacePamphletProfile prof is friends with the FacePamphletProfile
	 * with a name of String name. 
	 */
	private Boolean isFriendsWith(FacePamphletProfile prof, String name) {
		Iterator<String> it = prof.getFriends();
		while (it.hasNext()) {
			if (it.next().equals(name)) {
				return true;
			}
		}
		return false;
	}

	
	/* 
	 * This method returns true if there is a profile in the database 
	 * that has the given name.  It returns false otherwise.
	 */
	public boolean containsProfile(String name) {
		if (friendsDatabase.containsKey(name)) {
			return true;
		}
		return false;
	}
	
	/* private instance variables */
	HashMap<String, FacePamphletProfile> friendsDatabase = new HashMap<String, FacePamphletProfile>(); 
}
