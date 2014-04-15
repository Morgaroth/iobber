package pl.edu.agh.iobber;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Logger;

import pl.edu.agh.iobber.core.exceptions.YetAnotherException;
import pl.edu.agh.iobber.core.Credentials;
import pl.edu.agh.iobber.core.exceptions.ServerNotFoundException;
import pl.edu.agh.iobber.core.User;
import pl.edu.agh.iobber.core.exceptions.UserNotExistsException;
import pl.edu.agh.iobber.core.exceptions.WrongPasswordException;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.String.format;
import static pl.edu.agh.iobber.R.string.*;

public class LoginFragment extends Fragment {

    private Logger logger = Logger.getLogger(LoginFragment.class.getSimpleName());

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View inflate = inflater.inflate(R.layout.login_fragment, container, false);
        final Button loginButton = (Button) inflate.findViewById(R.id.login_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryLogin(inflate);
            }
        });
        ((EditText) inflate.findViewById(R.id.login_password_edit)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (isDoneAction(i, keyEvent)) {
                    closeKeyboard();
                    loginButton.performClick();
                }
                return false;
            }
        });
        return inflate;
    }


    private boolean isDoneAction(int actionId, KeyEvent event) {
        return actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_SEND ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
    }


    private void tryLogin(final View inflate) {
        EditText nickField = ((EditText) inflate.findViewById(R.id.login_login_edit));
        String nick = nickField.getText().toString();
        EditText passwordField = ((EditText) inflate.findViewById(R.id.login_password_edit));
        String password = passwordField.getText().toString();
        if (nick == null || nick.equals("")) {
            Toast.makeText(getActivity(), Nick_cannot_be_empty, LENGTH_SHORT).show();
            return;
        } else if (password == null || password.equals("")) {
            Toast.makeText(getActivity(), Password_cannot_be_empty, LENGTH_SHORT).show();
            return;
        }
        logger.info(format("try login with credentials \"%s\" and \"%s\"", nick, password));
        try {
            User loggedUser = User.login(new Credentials(nick, password));
            logger.info("logged user: " + loggedUser + ", starting new activity");
            // TODO start new activity with boundled data
        } catch (UserNotExistsException e) {
            nickField.clearComposingText();
            passwordField.clearComposingText();
            logger.info(format("user %s not exists", nick));
            Toast.makeText(getActivity(), User_not_exists, LENGTH_SHORT).show();
        } catch (ServerNotFoundException e) {
            logger.info("server not found");
            Toast.makeText(getActivity(), "server not exists", LENGTH_SHORT).show();
        } catch (WrongPasswordException e) {
            passwordField.clearComposingText();
            logger.info(format("wrong password(%s) for user %s", password, nick));
            Toast.makeText(getActivity(), Wrong_password, LENGTH_SHORT).show();
        } catch (YetAnotherException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }


    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

}
