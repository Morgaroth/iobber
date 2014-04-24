package pl.edu.agh.iobber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Map;
import java.util.logging.Logger;

import pl.edu.agh.iobber.core.LoggedUser;
import pl.edu.agh.iobber.core.XMPPManagerApplication;
import pl.edu.agh.iobber.core.exceptions.InternetNotFoundException;
import pl.edu.agh.iobber.core.exceptions.KurwaZapomnialemZaimplementowac;
import pl.edu.agh.iobber.core.exceptions.NotConnectedToTheServerException;
import pl.edu.agh.iobber.core.exceptions.ServerNotFoundException;
import pl.edu.agh.iobber.core.exceptions.UserNotExistsException;

import static java.lang.String.format;
import static pl.edu.agh.iobber.LoginActivity.LOGIN_REQUEST;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private static final String PREF = "SharedLogInPreferences";
    private Logger logger = Logger.getLogger(MainActivity.class.getSimpleName());
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private LoggedUser loggedUser;
    private Map<String, ConversationFragment> conversationsCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logIn();
    }

    private void logIn() {
        if (isLoggedUser()) {
            LoggedUser loggedUser = tryLogInUser();
            if (loggedUser != null) {
                setUpContent(loggedUser);
            } else {
                Toast.makeText(this, R.string.couldnt_log_in, Toast.LENGTH_LONG).show();
            }
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivityForResult(i, LOGIN_REQUEST);
        }
    }

    private LoggedUser tryLogInUser() {
        XMPPManagerApplication xm = (XMPPManagerApplication) getApplication();
        try {
            xm.connectToServer();
            xm.loginToServer();
            // TODO zwracanie obiektu który będzie prezentował zalogowanego użytkownika
            throw new KurwaZapomnialemZaimplementowac();
        } catch (InternetNotFoundException e) {
            Toast.makeText(this, R.string.Internet_not_found, Toast.LENGTH_LONG).show();
            logOut();
        } catch (ServerNotFoundException e) {
            Toast.makeText(this, R.string.Server_not_found, Toast.LENGTH_LONG).show();
            logOut();
        } catch (UserNotExistsException e) {
            Toast.makeText(this, R.string.User_not_exsist, Toast.LENGTH_LONG).show();
            logOut();
        } catch (NotConnectedToTheServerException e) {
            Toast.makeText(this, R.string.Server_not_connect, Toast.LENGTH_LONG).show();
            logOut();
        }
        // TODO zaimplementowac
        throw new KurwaZapomnialemZaimplementowac();
    }

    private void logOut() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF, 0);
        SharedPreferences.Editor shEditor = sharedPreferences.edit();
        shEditor.remove("LOGIN");
        shEditor.commit();
    }

    private void setUpContent(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                loggedUser.getActiveConversations());
    }

    private boolean isLoggedUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF, 0);
        return sharedPreferences.contains("LOGIN");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                //User loggedUser = (User) data.getSerializableExtra(USER);
                //logger.info(format("LoginActivity results OK with user %s", loggedUser));
                logger.info(format("LoginActivity results OK"));
                LoggedUser loggedUser = tryLogInUser();
                setUpContent(loggedUser);
            }
            if (resultCode == RESULT_CANCELED) {
                logger.info("Login activity results CANCELLED !");
                logIn();
                Toast.makeText(this, R.string.Login_cancelled, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mNavigationDrawerFragment != null) {
            if (!mNavigationDrawerFragment.isDrawerOpen()) {
                // Only show items in the action bar relevant to this screen
                // if the drawer is not showing. Otherwise, let the drawer
                // decide what to show in the action bar.
                getMenuInflater().inflate(R.menu.main, menu);
                restoreActionBar();
                return true;
            }
        }
        return super.onCreateOptionsMenu(menu);
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
    public void onNavigationDrawerItemSelected(int position, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, getConversationOrNew(title))
                .commit();

    }

    private ConversationFragment getConversationOrNew(String title) {
        if (!conversationsCache.containsKey(title)) {
            conversationsCache.put(title, ConversationFragment.newInstance(loggedUser.getConversation(title).getChat()));
        }
        return conversationsCache.get(title);
    }
}