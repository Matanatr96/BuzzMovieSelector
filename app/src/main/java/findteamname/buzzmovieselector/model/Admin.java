package findteamname.buzzmovieselector.model;

/**
 * Created by jherndon on 3/13/16.
 */
public class Admin extends User {
    /**
     * Default admin is just a userId and a password
     * System automatically adds "_admin" to the end
     * @param userID
     * @param password
     */
    public Admin(int userID, String name, String password) {
        super(userID, name, password, null, false);
    }
    public void ban(User user) {
        if (!(user instanceof Admin)) {
            user.setBanned(true);
        }
    }
    public void unBan(User user) {
        if (!(user instanceof Admin)) {
            user.setBanned(false);
        }
    }
}
