package pl.edu.agh.iobber.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.android.base.AndroidBaseManager;
import pl.edu.agh.iobber.android.base.DatabaseHelper;
import pl.edu.agh.iobber.android.contacts.ContactsFragment;
import pl.edu.agh.iobber.android.conversation.ConversationFragment;
import pl.edu.agh.iobber.android.navigation.NavigationDrawerFragment;
import pl.edu.agh.iobber.core.AndroidRosterListener;
import pl.edu.agh.iobber.core.Contact;
import pl.edu.agh.iobber.core.Conversation;
import pl.edu.agh.iobber.core.LoggedUser;
import pl.edu.agh.iobber.core.XMPPManager;

import static java.lang.String.format;
import static pl.edu.agh.iobber.android.LoginActivity.LOGIN_REQUEST;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ContactsFragment.InteractionListener {


    public static final String LOGGED_USER = "LOGGED_USER";
    private static final String PREF = "SharedLogInPreferences";
    private Logger logger = Logger.getLogger(MainActivity.class.getSimpleName());
    private NavigationDrawerFragment navigationDrawerFragment;
    private ContactsFragment contactsFragment;
    private LoggedUser loggedUser;
    private Map<String, ConversationFragment> conversationsCache = new HashMap<String, ConversationFragment>();
    private boolean contactsLoaded = false;
    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHelper();
        try {
            XMPPManager.addBaseManager(new AndroidBaseManager(databaseHelper.getDao()));
            XMPPManager.setRosterListener(new AndroidRosterListener());
        } catch (SQLException e) {
        }

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

    private void getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private void setUpContent() {
        setContentView(R.layout.activity_main);

        navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                loggedUser.getActiveConversations());

        restoreActionBar();

        loadContactsFragment();
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
                //  TODO Czy to kiedy kolwiek nastąpi?
                Toast.makeText(this, R.string.Login_cancelled, Toast.LENGTH_LONG).show();
            }
        }
    }

    private LoggedUser loadUserFromID(String userID) {
        // sprawdzać, czy na pewno udało się wczytać, czy istnieje itd
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (navigationDrawerFragment == null || !navigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
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
//            case R.id.action_new_conversation:
//                logger.info("clicked new conversation action button");
//                startNewConversation();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void startNewConversation() {
//        InputDialog(this, "z kim?", new InputDialogC.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i, EditText input) {
//                String oponent = input.getText().toString();
//                logger.info("user typed oponent " + oponent);
//                startConversationWith(oponent);
//            }
//        }).show();
//    }

    private void updateNavigationDrawer() {
        navigationDrawerFragment.updateConversationsList(new LinkedList<Conversation>(loggedUser.getActiveConversations()));
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
        contactsLoaded = false;
        logger.info(format("conversation %s loaded", conversation));
    }

    private void loadContactsFragment() {
        if (contactsFragment == null) {
            contactsFragment = ContactsFragment.newInstance();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, contactsFragment)
                .commitAllowingStateLoss();
        Handler handler = new Handler(Looper.getMainLooper());
        logger.info("logger setted");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                contactsFragment.setContactsList(loggedUser.getContacts());
            }
        }, 1);
        contactsLoaded = true;
        logger.info("contacts loaded");
    }


    private ConversationFragment getConversationOrNew(String title) {
        if (!conversationsCache.containsKey(title)) {
            conversationsCache.put(title, ConversationFragment.newInstance(loggedUser.getConversation(title)));
        }
        return conversationsCache.get(title);
    }

    @Override
    public void onContactClicked(Contact contact) {
        logger.info(format("user start conversation with %s", contact));
        startConversationWith(contact);
    }

    private void startConversationWith(Contact contact) {
        Conversation conversation = loggedUser.startConversation(contact);
        updateNavigationDrawer();
        loadConversation(conversation.getName());
    }

    @Override
    public void onBackPressed() {
        if (contactsLoaded) {
            super.onBackPressed();
        } else {
            loadContactsFragment();
        }
    }
}
