package pl.edu.agh.iobber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.logging.Logger;

import pl.edu.agh.iobber.core.User;
import pl.edu.agh.iobber.core.exceptions.KurwaZapomnialemZaimplementowac;

import static java.lang.String.format;
import static pl.edu.agh.iobber.LoginActivity.LOGIN_REQUEST;
import static pl.edu.agh.iobber.LoginActivity.USER;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private Logger logger = Logger.getLogger(MainActivity.class.getSimpleName());
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    private static boolean isActionSend(int actionId, KeyEvent event) {
        return actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_SEND ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logIn();
    }

    private void logIn() {
        if (isLoggedUser()) {
            setUpContent(getLoggedUser());
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivityForResult(i, LOGIN_REQUEST);
        }
    }

    private User getLoggedUser() {
        // TODO wymyślić w jaki sposób będzie przechowywany zalogowany użytkownik i tutaj go ładować, jego albo coś, to będzie wystarczać do interakcji
        return null;
    }

    private void setUpContent(User loggedUser) {
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private boolean isLoggedUser() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                User loggedUser = (User) data.getSerializableExtra(USER);
                logger.info(format("LoginActivity results OK with user %s", loggedUser));
                setUpContent(loggedUser);
            }
            if (resultCode == RESULT_CANCELED) {
                logger.info("Login activity results CANCELLED !");
                Toast.makeText(this, "login cancelled!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
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
                        Editable oponent = input.getText();
                        logger.info("user typed oponent " + oponent);
                        Conversation conversation = loggedUser.startConversation(oponent);
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
        throw new KurwaZapomnialemZaimplementowac();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));

        // TODO akcja na wciśnięcie entera podczas pisania

        ((EditText) rootView.findViewById(R.id.chatLine)).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        Logger.getLogger(MainActivity.class.getName()).info("used event=" + event);
                        if (isActionSend(actionId, event)) {
                            sendMessage(v.getText());
                            clearEditText();
                            return true;
                        }
                        return false;
                    }
                }
        );
        return rootView;
        }

    private void clearEditText() {
        // TODO wyczyszczenie okienka do pisania
        }

    private void sendMessage(CharSequence text) {
        // TODO logika od wysyłania wiadomośći
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
