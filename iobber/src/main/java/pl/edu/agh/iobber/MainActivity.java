package pl.edu.agh.iobber;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.String.format;
import static pl.edu.agh.iobber.LoginActivity.LOGIN_REQUEST;
import static pl.edu.agh.iobber.LoginActivity.USER;


import java.util.logging.Logger;

import pl.edu.agh.iobber.core.User;
import pl.edu.agh.iobber.core.XMPPManagerApplication;
import pl.edu.agh.iobber.core.exceptions.InternetNotFoundException;
import pl.edu.agh.iobber.core.exceptions.NotConnectedToTheServerException;
import pl.edu.agh.iobber.core.exceptions.ServerNotFoundException;
import pl.edu.agh.iobber.core.exceptions.UserNotExistsException;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private Logger logger = Logger.getLogger(MainActivity.class.getSimpleName());
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private static final String PREF = "SharedLogInPreferences";
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logIn();
    }

    private void logIn(){
        if (isLoggedUser()) {
            getLoggedUser();
            setUpContent();
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivityForResult(i, LOGIN_REQUEST);
        }
    }

    /**
     * zwraca cos
     * @return
     */
    private void getLoggedUser() {
        XMPPManagerApplication xm = (XMPPManagerApplication)getApplication();
        try {
            xm.connectToServer();
            xm.loginToServer();
        } catch (InternetNotFoundException e) {
            Toast.makeText(this, new String(getResources().getString(R.string.Internet_not_found)), Toast.LENGTH_LONG).show();
            logOut();
        } catch (ServerNotFoundException e) {
            Toast.makeText(this, new String(getResources().getString(R.string.Server_not_found)), Toast.LENGTH_LONG).show();
            logOut();
        }catch (UserNotExistsException e) {
            Toast.makeText(this, new String(getResources().getString(R.string.User_not_exsist)), Toast.LENGTH_LONG).show();
            logOut();
        } catch (NotConnectedToTheServerException e) {
            Toast.makeText(this, new String(getResources().getString(R.string.Server_not_connect)), Toast.LENGTH_LONG).show();
            logOut();
        }
    }

    private void logOut(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREF, 0);
        SharedPreferences.Editor shEditor = sharedPreferences.edit();
        shEditor.remove("LOGIN");
        shEditor.commit();
    }

    private void setUpContent() {
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
                getLoggedUser();
                setUpContent();
            }
            if (resultCode == RESULT_CANCELED) {
                logger.info("Login activity results CANCELLED !");
                logIn();
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
                logOut();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
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
                    });
            return rootView;
        }

        private void clearEditText() {
            // TODO wyczyszczenie okienka do pisania
        }

        private void sendMessage(CharSequence text) {
            // TODO logika od wysyłania wiadomośći
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    private static boolean isActionSend(int actionId, KeyEvent event) {
        return actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_SEND ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
    }

}
