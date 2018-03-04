package findteamname.buzzmovieselector.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;

import findteamname.buzzmovieselector.R;
import findteamname.buzzmovieselector.model.Major;
import findteamname.buzzmovieselector.model.User;
import findteamname.buzzmovieselector.model.UserManager;
import findteamname.buzzmovieselector.model.UserManagerSingleton;
import findteamname.buzzmovieselector.database.DBQuery;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner majorSelector = (Spinner) findViewById(R.id.registrationMajorSelector);
        ArrayAdapter<Major> adapter = new ArrayAdapter<Major>(this,
                android.R.layout.simple_spinner_dropdown_item, Major.values());
        majorSelector.setAdapter(adapter);
    }


    /**
     * Attempts to register the user when they click on register after filling out the required
     * fields.
     *
     * @param v The view that called this function (the REGISTER button)
     *
     * @return True if the user was succesfully resgistered, false otherwise
     */
    public boolean onRegisterButtonClicked(View v) {
        EditText userIDText      = (EditText) findViewById(R.id.registrationNameText);
        EditText passText        = (EditText) findViewById(R.id.registrationPassText);
        EditText reEnterPassText = (EditText) findViewById(R.id.registrationReEnterPassText);

        Spinner majorSelector = (Spinner) findViewById(R.id.registrationMajorSelector);

        if (userIDText.getText().toString().isEmpty()
                || passText.getText().toString().isEmpty()
                || reEnterPassText.getText().toString().isEmpty()) {
            CharSequence text = getString(R.string.enter_all_fields);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

            return false;
        }

        String userName          = userIDText.getText().toString();
        String password        = passText.getText().toString();
        String reEnterPassword = reEnterPassText.getText().toString();

        if (!password.equals(reEnterPassword)) {
            CharSequence text = getString(R.string.registration_passwords_do_not_match);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            return false;
        }

        UserManager um = UserManagerSingleton.getUserManager();
        DBQuery query = new DBQuery();

        if (um.findUserByID(userName) != null) {
            CharSequence text = getString(R.string.registration_username_taken);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            return false;
        }

        int userID = query.addUser(userName, password, ((Major) majorSelector.getSelectedItem()).toString(), false);
        um.addUser(new User(userID, userName, password, (Major) majorSelector.getSelectedItem(), false));

        Log.d("Registration Activity", "UserID = " + userID);

        finish();

        return true;
    }

    /**
     * Starts the admin register acitivity
      * @param v
     */
    public void onAdminRegisterClicked(View v) {
        startActivity(new Intent(this, AdminRegistrationActivity.class));
    }

    /**
     * Cancels the login request (does not add a new user) and takes the user back to the opening
     * screen.
     *
     * @param v The view that called this method (the CANCEL button)
     */
    public void cancelRequest(View v) {
        finish();
    }
}
