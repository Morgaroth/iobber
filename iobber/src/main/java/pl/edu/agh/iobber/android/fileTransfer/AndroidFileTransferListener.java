package pl.edu.agh.iobber.android.fileTransfer;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import java.io.File;

//TODO ten if powinien byc sterowany z zewnatrz i w zaleznosci od woli uzytkownika albo pobieramy plik albo nie

public class AndroidFileTransferListener implements FileTransferListener {

    private IncomingFileTransfer transfer;

    @Override
    public void fileTransferRequest(FileTransferRequest fileTransferRequest) {
        if(true) {
            transfer = fileTransferRequest.accept();
            try {
                transfer.recieveFile(new File(fileTransferRequest.getFileName()));
            } catch (XMPPException e) {

            }
        } else {
            // Reject it
            fileTransferRequest.reject();
        }
    }

    public FileTransfer.Status getStatus(){
        return transfer.getStatus();
    }

    public double getProgress(){
        return transfer.getProgress();
    }

    public boolean isDone(){
        return transfer.isDone();
    }

    public FileTransfer.Error getError(){
        return transfer.getError();
    }
}
