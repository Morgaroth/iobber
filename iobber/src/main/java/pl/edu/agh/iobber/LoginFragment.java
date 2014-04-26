package pl.edu.agh.iobber;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Logger;

import pl.edu.agh.iobber.core.User;

import static android.widget.Toast.LENGTH_SHORT;
import static pl.edu.agh.iobber.R.string.Nick_cannot_be_empty;
import static pl.edu.agh.iobber.R.string.Password_cannot_be_empty;
import static pl.edu.agh.iobber.R.string.Server_cannot_be_empty;

public class LoginFragment extends Fragment {

    private static final String PREF = "SharedLogInPreferences";
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

        EditText serverField = ((EditText) inflate.findViewById(R.id.login_server_edit));
        String server = serverField.getText().toString();

        if (nick.equals("m")) {
            nick = "mjaje@student.agh.edu.pl";
            password = "Funatyha";
        } else if (nick.equals("a")) {
            nick = "klusek@student.agh.edu.pl";
            password = "fotidep";
        }

        CheckBox sASLAuth = (CheckBox) inflate.findViewById(R.id.SASLAuth);
        boolean sASLAuthChecked = sASLAuth.isChecked();

        if (nick == null || nick.equals("")) {
            Toast.makeText(getActivity(), Nick_cannot_be_empty, LENGTH_SHORT).show();
            return;
        } else if (password == null || password.equals("")) {
            Toast.makeText(getActivity(), Password_cannot_be_empty, LENGTH_SHORT).show();
            return;
        } else if (server == null || server.equals("")) {
            Toast.makeText(getActivity(), Server_cannot_be_empty, LENGTH_SHORT).show();
            return;
        }
        User user = new User();
        user.setValue("LOGIN", nick);
        user.setValue("PASSWORD", password);
        user.setValue("PORT", String.valueOf(5222));
        user.setValue("SERVER", server);
        user.setValue("SASLAUTH", sASLAuth.isChecked() ? "YES" : "NO");

        mListener.userLogged(user);
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

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    public interface OnFragmentInteractionListener {
        public void userLogged(User user);
    }

}
