package findteamname.buzzmovieselector.model;

import java.util.Collection;
import java.util.HashMap;
import findteamname.buzzmovieselector.database.DBQuery;

/**
 * Manages the users of the app using a backing HashMap.
 */
public class UserManager implements AuthentificationFacade, UserManagementFacade {

    private static HashMap<String, User> users = new HashMap<>();

    /**
     * protected constructor to prevent instantiation, except in subclass
     */
    protected UserManager() {

    }

    @Override
    public void addUser(User user) {
        users.put(user.getUserName(), user);
    }

    @Override
    public boolean removeUser(User user) {
        return users.remove(user.getUserName()) != null;
    }

    @Override
    public User findUserByID(String id) {
        return users.get(id);
    }

    @Override
    public boolean handleLoginRequest(String id, String password) {
//        String DBPass;
//        DBQuery query = new DBQuery();
//        DBPass = query.getPassByUsername(id);
//        if (DBPass == null) {
//            return false;
//        }
//        query.getUsersTable();
//        return DBPass.equals(password);
        User user = findUserByID(id);
        return user != null && user.validate(password);
    }

    /**
     * @return An ArrayList of all the registered users
     */
    public static Collection<User> getUsers() {
        return users.values();
    }

}
