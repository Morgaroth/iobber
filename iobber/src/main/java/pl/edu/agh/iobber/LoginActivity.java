package pl.edu.agh.iobber;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.logging.Logger;

import pl.edu.agh.iobber.core.User;

import static java.lang.String.format;

/**
 * Ala ma ktoa
 */
public class LoginActivity extends ActionBarActivity implements LoginFragment.OnFragmentInteractionListener {
    private Logger logger = Logger.getLogger(LoginActivity.class.getSimpleName());

    private LoginFragment loginFragment;

    public static final int LOGIN_REQUEST = 1;
    public static final String USER = "USER_DATA";

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void userLogged(User user) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(USER, user);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
