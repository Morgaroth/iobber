package pl.edu.agh.iobber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import pl.edu.agh.iobber.core.Conversation;
import pl.edu.agh.iobber.core.LoggedUser;
import pl.edu.agh.iobber.core.XMPPManager;

import static java.lang.String.format;
import static pl.edu.agh.iobber.LoginActivity.LOGIN_REQUEST;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ContactListFragment.OnFragmentInteractionListener {


    public static final String LOGGED_USER = "LOGGED_USER";
    private static final String PREF = "SharedLogInPreferences";
    private Logger logger = Logger.getLogger(MainActivity.class.getSimpleName());
    private NavigationDrawerFragment navigationDrawerFragment;
    private CharSequence mTitle;
    private LoggedUser loggedUser;
    private Map<String, ConversationFragment> conversationsCache = new HashMap<String, ConversationFragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoggedUser user;
        if (savedInstanceState != null && (user = getLoggedUserOrNull(savedInstanceState)) != null) {
            logger.info(format("user %s recognized from bundle", user.getID()));
            loggedUser = user;
            setUpContent();
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivityForResult(i, LOGIN_REQUEST);
        }
    }

    private LoggedUser getLoggedUserOrNull(Bundle savedInstanceState) {
        boolean isLogged = savedInstanceState.containsKey(LOGGED_USER);
        if (isLogged) {
            String userID = savedInstanceState.getString(LOGGED_USER);
            return loadUserFromID(userID);
        }
        return null;
    }

    private void setUpContent() {
        setContentView(R.layout.activity_main);

        navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                loggedUser.getActiveConversations());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                String userID = data.getStringExtra(LoginActivity.USER);
                loggedUser = loadUserFromID(userID);
                setUpContent();
                logger.info(format("LoginActivity results OK"));
                Toast.makeText(this, "Hurra", Toast.LENGTH_LONG).show();
            }
            if (resultCode == RESULT_CANCELED) {
                logger.info("Login activity results CANCELLED !");
                // TODO przejscie do jakiegoś pierwszego ekranu
                Toast.makeText(this, R.string.Login_cancelled, Toast.LENGTH_LONG).show();
            }
        }
    }

    private LoggedUser loadUserFromID(String userID) {
        // TODO sprawdzać, czy na pewno udało się wczytać, czy istnieje itd
        return XMPPManager.getLoggedUser(userID);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
        if (loggedUser != null) {
            logger.info(format("main activity save user %s in bundle", loggedUser.getID()));
            outState.putString(LOGGED_USER, loggedUser.getID());
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (navigationDrawerFragment != null) {
            if (!navigationDrawerFragment.isDrawerOpen()) {
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                logger.info("clicked settings action button");
                return true;
            case R.id.action_example:
                logger.info("clicked example action button");
                return true;
            case R.id.action_new_conversation:
                logger.info("clicked new conversation action button");
                startNewConversation();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startNewConversation() {
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        new AlertDialog.Builder(this)
                .setView(input)
                .setTitle("z kim?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String oponent = input.getText().toString();
                        logger.info("user typed oponent " + oponent);
                        Conversation conversation = loggedUser.startConversation(oponent);
                        saveConversation(conversation);
                        loadConversation(conversation.getName());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void saveConversation(Conversation conversation) {
        navigationDrawerFragment.addConversationToList(conversation);
    }

    @Override
    public void onNavigationDrawerItemSelected(String title) {
        loadConversation(title);
    }

    private void loadConversation(String title) {
        ConversationFragment conversation = getConversationOrNew(title);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, conversation)
                .commit();
    }

    private ConversationFragment getConversationOrNew(String title) {
        if (!conversationsCache.containsKey(title)) {
            conversationsCache.put(title, ConversationFragment.newInstance(loggedUser.getConversation(title).getChat()));
        }
        return conversationsCache.get(title);
    }

    @Override
    public void onFragmentInteraction(int id) {
        logger.info("user typed contact on position " + id);
    }
}
