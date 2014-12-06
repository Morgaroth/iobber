package pl.edu.agh.iobber.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.logging.Logger;

import pl.edu.agh.iobber.R;

import static java.lang.String.format;

public class LoginActivity extends ActionBarActivity implements LoginFragment.OnFragmentInteractionListener {
    public static final int LOGIN_REQUEST = 1;
    public static final String USER = "USER";
    private Logger logger = Logger.getLogger(LoginActivity.class.getSimpleName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logger.info(format("onCreate"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                logger.info("Login fragment clicked settings action button");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void userLogged(String user) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(USER, user);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
