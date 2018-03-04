package findteamname.buzzmovieselector.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import findteamname.buzzmovieselector.R;
import findteamname.buzzmovieselector.model.Admin;
import findteamname.buzzmovieselector.model.Major;
import findteamname.buzzmovieselector.model.Movie;
import findteamname.buzzmovieselector.model.MovieManager;
import findteamname.buzzmovieselector.model.Rating;
import findteamname.buzzmovieselector.model.RatingManager;
import findteamname.buzzmovieselector.model.User;
import findteamname.buzzmovieselector.model.UserManagementFacade;
import findteamname.buzzmovieselector.model.UserManager;
import findteamname.buzzmovieselector.model.AuthentificationFacade;
import findteamname.buzzmovieselector.model.UserManagerSingleton;
import findteamname.buzzmovieselector.database.DBQuery;


public class OpeningScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);

        UserManager um = UserManagerSingleton.getUserManager();

        DBQuery dbq = new DBQuery();
        Log.d("OpenActivity", "db initialized");
        List<List<String>> userInfo = dbq.getUsersTable();
        Log.d("All ratings from table", dbq.getRatingsTable().toString());
        List<List<String>> ratings = dbq.getRatingsTable();
        List<List<String>> movies = dbq.getMoviesTable();
        if (userInfo == null) {
            CharSequence text = getString(R.string.openingFailedToConnect);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } else {
            for (List<String> user : userInfo) {
                Major m;
                try {
                    m = Major.valueOf(user.get(3));
                } catch (Exception e) {
                    m = null;
                }
                User u;
                if (user.get(5).equals("0")) {
                    u = new User(
                            Integer.parseInt(user.get(0)),
                            user.get(1),
                            user.get(2),
                            m,
                            user.get(4).equals("1") // isBanned
                    );
                } else {
                    u = new Admin(
                            Integer.parseInt(user.get(0)),
                            user.get(1),
                            user.get(2)
                    );
                }

                um.addUser(u);
            }
        }
        if (movies == null) {
            CharSequence text = getString(R.string.openingFailedToConnect);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } else {
            MovieManager movieManager = new MovieManager();
            for (List<String> movie: movies) {
                String id = movie.get(0);
                String title = movie.get(1);
                String genre = movie.get(2);
                movieManager.addMovie(new Movie(id, title, genre));
            }
        }
        if (ratings == null) {
            CharSequence text = getString(R.string.openingFailedToConnect);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } else {
            RatingManager rm = new RatingManager();
            for (List<String> rating: ratings) {
                int userId = Integer.parseInt(rating.get(0));
                String movieId = rating.get(1);
                float R = Float.parseFloat(rating.get(2));

                rm.initializeRating(userId, movieId, R);
                //Log.d("LOOKHERE", rm.ratings.toString());
            }
        }
    }

    /**
     * Attempts to log in based on the information provided
     *
     * @param v The view that called this method (the Login button)
     */
    public void onOpeningScreenLoginButtonPressed(View v) {
        AuthentificationFacade authenticator = UserManagerSingleton.getUserManager();

        EditText nameBox = (EditText) findViewById(R.id.input_email);
        EditText passBox = (EditText) findViewById(R.id.input_password);

        String id = nameBox.getText().toString();
        String pass = passBox.getText().toString();

        boolean loginSuccessful = authenticator.handleLoginRequest(id, pass);

        CharSequence text = (loginSuccessful)
                ? ("Login successful!")
                : "Login failed!";

        Toast t;

        if (loginSuccessful) {
            UserManagerSingleton.setUser(UserManagerSingleton.getUserManager().findUserByID(id));
            nameBox.setText("");
            passBox.setText("");
            Log.d("ISUSERBANNED:", ""+UserManagerSingleton.getUser().isBanned());
            if (UserManagerSingleton.getUser().isBanned()) {
                t = Toast.makeText(getApplicationContext(), "You've been banned", Toast.LENGTH_SHORT);
                t.show();
            }

            //nice and simple
            else if (UserManagerSingleton.getUser() instanceof Admin) {
                startActivity(new Intent(this, AdminMainActivity.class));
                t = Toast.makeText(getApplicationContext(), "Welcome, admin", Toast.LENGTH_SHORT);
                t.show();

            } else {
                t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                t.show();
                startActivity(new Intent(this, MainActivity.class));
            }
        } else {
            t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            t.show();

        }
    }

    /**
     * Takes the user to the registration screen.
     *
     * @param v The view that called this method (the REGISTER button)
     */
    public void onRegisterButtonPressed(View v) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }
}
