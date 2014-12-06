package pl.edu.agh.iobber.android.fileTransfer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Logger;

import pl.edu.agh.iobber.R;
import pl.edu.agh.iobber.core.Conversation;
import pl.edu.agh.iobber.core.exceptions.CannotSendFileToUserException;

import static java.lang.String.format;
import static java.lang.Thread.sleep;

public class FileTransferFragment extends Fragment {

    public static final int ACTIVITY_CHOOSE_FILE = 10;
    private final Conversation delegate;
    private Logger logger = Logger.getLogger(FileTransferFragment.class.getSimpleName());
    private Activity activity = null;
    private TextView chosenFileView;

    private FileTransferFragment(Conversation delegate) {
        this.delegate = delegate;
    }

    public static FileTransferFragment newInstance(Conversation delegate) {
        return new FileTransferFragment(delegate);
    }

    @Override
    public String toString() {
        return "FileTransferFragment{}";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logger.info("Creating File Transfer Fragment");
        View rootView = inflater.inflate(R.layout.filetransfer_fragment_layout, container, false);
        chosenFileView = (TextView) rootView.findViewById(R.id.filetransfer_layout_filepath_view);
        Button chooseFileBtn = (Button) rootView.findViewById(R.id.filetransfer_layout_choose_file_button);
        chooseFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity != null) {
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("file/*");
                    Intent intent = Intent.createChooser(chooseFile, "Choose a file");
                    activity.startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
                }
            }
        });
        return rootView;
    }

    public void fileChosen(int requestCode, int resultCode, Intent data) {
        logger.info("FileTransferFragment received result from choosing file");
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String filePath = uri.getPath();
            chosenFileView.setText(filePath);
            try {
                delegate.sendFile(filePath);
            } catch (CannotSendFileToUserException e) {
                logger.severe("Cannot send file because " + e.getMessage());
                Toast.makeText(activity, "Cannot send file because " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

}
