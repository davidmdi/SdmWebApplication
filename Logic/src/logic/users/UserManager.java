package logic.users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
Adding and retrieving Logic.users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final Set<User> usersSet;

    public UserManager() {
        usersSet = new HashSet<>();
        usersSet.add(new User("moshe","customer"));
    }

    public synchronized void addUser(String username,String userType) {
        // if Logic == null -> create logic -> add user to users Array.
        // need to figure out
        usersSet.add(new User(username,userType));
    }

    public synchronized void removeUser(User username) {
        usersSet.remove(username);
    }

    public synchronized Set<User> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        for (User user:usersSet) {
            if(user.getName().equals(username))
                return true;
        }
        return false;
    }

    public User getUserByName(String userName){
        for (User user:usersSet) {
            if(user.getName().equals(userName))
                return user;
        }

        return null;
    }
}
