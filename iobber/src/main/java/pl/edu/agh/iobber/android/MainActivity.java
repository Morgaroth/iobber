package pl.edu.agh.iobber.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.android.ChatManagerListener.AndroidChatManagerListenerCore;
import pl.edu.agh.iobber.android.baseMessages.AndroidBaseManagerMessages;
import pl.edu.agh.iobber.android.baseMessages.DatabaseHelperMessages;
import pl.edu.agh.iobber.android.baseUsers.AndroidBaseManager;
import pl.edu.agh.iobber.android.baseUsers.DatabaseHelper;
import pl.edu.agh.iobber.android.contacts.ContactsFragment;
import pl.edu.agh.iobber.android.conversation.ConversationFragment;
import pl.edu.agh.iobber.android.finding.FindingFragment;
import pl.edu.agh.iobber.android.finding.FindingResultsFragment;
import pl.edu.agh.iobber.android.navigation.NavigationDrawerFragment;
import pl.edu.agh.iobber.android.AndroidRosterListener;
import pl.edu.agh.iobber.core.BaseManagerMessages;
import pl.edu.agh.iobber.core.BaseManagerMessagesConfiguration;
import pl.edu.agh.iobber.core.Contact;
import pl.edu.agh.iobber.core.Conversation;
import pl.edu.agh.iobber.core.LoggedUser;
import pl.edu.agh.iobber.core.SimpleMessage;
import pl.edu.agh.iobber.core.XMPPManager;

import static java.lang.String.format;
import static pl.edu.agh.iobber.android.LoginActivity.LOGIN_REQUEST;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        ContactsFragment.InteractionListener, FindingFragment.OnResultListener,
        FindingResultsFragment.OnResultLister {

    public static final String LOGGED_USER = "LOGGED_USER";
    public static final String SHARED_PREFERENCES = "MESSAGES_PREFS";
    private static final String PREF = "SharedLogInPreferences";
    private Logger logger = Logger.getLogger(MainActivity.class.getSimpleName());
    private NavigationDrawerFragment navigationDrawerFragment;
    private ContactsFragment contactsFragment;
    private LoggedUser loggedUser;
    private Map<String, ConversationFragment> conversationsCache = new HashMap<String, ConversationFragment>();
    private boolean contactsLoaded = false;
    private DatabaseHelper databaseHelper = null;
    private DatabaseHelperMessages databaseHelperMessages = null;
    private FindingFragment findingFragment;
    private FindingResultsFragment findingResultsFragment;
    private Stack<Fragment> fragmentsStack = new Stack<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.settings, true);
        getHelperMessage();
        getHelper();

        try {
            BaseManagerMessages baseManagerMessages = new AndroidBaseManagerMessages(databaseHelperMessages.getSimpleMessageDao(), getSharedPreferences(SHARED_PREFERENCES, 0));
            baseManagerMessages.setBaseManagerMessagesConfiguration(new BaseManagerMessagesConfiguration(30));
            XMPPManager.addBaseManagerMessage(baseManagerMessages);
            XMPPManager.addBaseManager(new AndroidBaseManager(databaseHelper.getUserDao()));
        } catch (SQLException e) {
            logger.info(e.toString());
        }
        XMPPManager.setRosterListener(new AndroidRosterListener());
        XMPPManager.setChatManagerListener(new AndroidChatManagerListenerCore(this));
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

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelper(this);
        }
        return databaseHelper;
    }

    private DatabaseHelperMessages getHelperMessage() {
        if (databaseHelperMessages == null) {
            databaseHelperMessages = DatabaseHelperMessages.getHelper(this);
        }
        return databaseHelperMessages;
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
            databaseHelper.close();
            databaseHelper = null;
        }

        if (databaseHelperMessages != null) {
            databaseHelperMessages.close();
            databaseHelperMessages = null;
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
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            case R.id.action_find:
                logger.info("clicked fin action button");
                loadFragment(getOrCreateFindingFragment());
                return true;
            case R.id.action_show_contacts:
                loadContactsFragment();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private FindingFragment getOrCreateFindingFragment() {
        if (findingFragment == null) {
            findingFragment = new FindingFragment();
        }
        return findingFragment;
    }

    private void updateNavigationDrawer() {
        navigationDrawerFragment.updateConversationsList(new LinkedList<Conversation>(loggedUser.getActiveConversations()));
    }

    @Override
    public void onNavigationDrawerItemSelected(String title) {
        loadConversation(title);
    }

    private void loadFragmentLoss(Fragment fragment) {
        loadFragmentLoss(fragment, true);
    }

    private void loadFragmentLoss(Fragment fragment, boolean queue) {
        prepareLoadingFragment(fragment, queue).commitAllowingStateLoss();
    }

    private void loadFragment(Fragment fragment) {
        loadFragment(fragment, true);
    }

    private void loadFragment(Fragment fragment, boolean queue) {
        prepareLoadingFragment(fragment, queue).commit();
    }

    private FragmentTransaction prepareLoadingFragment(Fragment fragment, boolean queue) {
        if (queue) {
            fragmentsStack.push(fragment);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.beginTransaction()
                .replace(R.id.container, fragment);
    }

    private void loadConversation(String title) {
        ConversationFragment conversation = getConversationOrNew(title);
        loadFragment(conversation);
        contactsLoaded = false;
        logger.info(format("conversation %s loaded", conversation));
    }

    private void loadContactsFragment() {
        if (contactsFragment == null) {
            contactsFragment = ContactsFragment.newInstance();
        }
        loadFragmentLoss(contactsFragment);
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
        return getConversationOrNew(title, false);
    }

    private ConversationFragment getConversationOrNew(String title, boolean recreate) {
        if ((!conversationsCache.containsKey(title)) || recreate) {
            conversationsCache.put(title, ConversationFragment.newInstance(loggedUser.getOrCreateConversation(title)));
        }
        return conversationsCache.get(title);
    }

    private ConversationFragment getConversationOrNew(String title, SimpleMessage messages) {
        return getConversationOrNew(title, messages, false);
    }

    private ConversationFragment getConversationOrNew(String title, SimpleMessage messages, boolean recreate) {
        if ((!conversationsCache.containsKey(title)) || recreate) {
            conversationsCache.put(title, ConversationFragment.newInstance(loggedUser.getOrCreateConversation(title), messages));
        }
        return conversationsCache.get(title);
    }

    @Override
    public void onContactClicked(Contact contact) {
        logger.info(format("user start conversation with %s", contact));
        startConversationWith(contact);
    }

    @Override
    public void onContactLongClick(Contact item) {
        FindingFragment fragment = getOrCreateFindingFragment();
        fragment.setContact(item);
        logger.severe(format("finding for %s", item.getXMPPIdentifier()));
        loadFragment(fragment);
    }

    public void joinToConversatonWith(Chat chat) {
        final Conversation conversation = loggedUser.joinToConversation(chat);
        logger.info("Joined to conversation");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateNavigationDrawer();
                loadConversation(conversation.getName());
            }
        });
    }

    private void startConversationWith(Contact contact) {
        Conversation conversation = loggedUser.startConversation(contact);
        updateNavigationDrawer();
        loadConversation(conversation.getName());
    }

    @Override
    public void onBackPressed() {
        if (fragmentsStack.size() > 1) {
            fragmentsStack.pop();
            logger.info(fragmentsStack.toString());
            loadFragment(fragmentsStack.peek(), false);
        } else {
            logger.info("stack has only one position");
            loadFragment(fragmentsStack.peek(), false);
        }
    }

    @Override
    public void onResult(String author, List<SimpleMessage> messages) {
        FindingResultsFragment fragment = getOrCreateFindingResultsFragment();
        fragment.setUp(messages, author);
        loadFragment(fragment);
    }

    private FindingResultsFragment getOrCreateFindingResultsFragment() {
        if (findingResultsFragment == null) {
            findingResultsFragment = new FindingResultsFragment();
        }
        return findingResultsFragment;
    }

    @Override
    public void onFoundMessageSelected(SimpleMessage msg, String author) {
        logger.info("scroll to msg " + msg);
        ConversationFragment conversationFrag = getConversationOrNew(author, msg, true);
        loadFragment(conversationFrag);
        Toast.makeText(this, "Scroll to " + msg.getBody(), Toast.LENGTH_LONG).show();
    }
}
