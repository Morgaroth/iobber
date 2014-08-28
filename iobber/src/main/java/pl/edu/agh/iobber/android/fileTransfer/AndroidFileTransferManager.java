package pl.edu.agh.iobber.android.fileTransfer;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import java.io.File;
import java.util.logging.Logger;

import pl.edu.agh.iobber.core.exceptions.CannotSendFileToUserException;

//in order to get some informations, pease visit this site -> https://www.igniterealtime.org/builds/smack/docs/latest/documentation/extensions/filetransfer.html

public class AndroidFileTransferManager {

    private Logger logger = Logger.getLogger(AndroidFileTransferManager.class.getSimpleName());
    private XMPPConnection xmppConnection;
    private FileTransferManager fileTransferManager;
    private String destination;
    private String filepath;
    private String comment;
    private OutgoingFileTransfer transfer;
    private AndroidFileTransferListener androidFileTransferListener;

    public AndroidFileTransferManager(XMPPConnection xmppConnection){
        logger.info("New AndroidFileTransferManager is created");
        comment = "";
        androidFileTransferListener = null;
        this.xmppConnection = xmppConnection;
        fileTransferManager = new FileTransferManager(xmppConnection);
    }

    public void setDestination(String destination){
        logger.info("Destination for sending file is set");
        this.destination = destination;
    }

    public void setFilepathToTransferedFile(String filepath){
        logger.info("Filepath to the file is set");
        this.filepath = filepath;
    }

    public void setCommentToTransferedFile(String comment){
        logger.info("Comment for transfered file is set");
        this.comment = comment;
    }

    public void sendFile() throws CannotSendFileToUserException {
        transfer = fileTransferManager.createOutgoingFileTransfer(destination);
        try {
            transfer.sendFile(new File(filepath), comment);
            logger.info("The file is going to be send");
        } catch (XMPPException e) {
            logger.info("Cannot send the file to specified user " + destination + " " + filepath + " " + e.toString());
            throw new CannotSendFileToUserException(e);
        }
    }

    public void setFileTransferListener(AndroidFileTransferListener fileTransferListener){
        fileTransferManager.addFileTransferListener(fileTransferListener);
        androidFileTransferListener = fileTransferListener;
    }

    public FileTransfer.Status getStatusOfIncomingIleTransfer(){
        return androidFileTransferListener.getStatus();
    }

    public double getProgressOfIncomingIleTransfer(){
        return androidFileTransferListener.getProgress();
    }

    public boolean isDoneOfIncomingIleTransfer(){
        return androidFileTransferListener.isDone();
    }

    public FileTransfer.Error getErrorOfIncomingIleTransfer(){
        return androidFileTransferListener.getError();
    }

    public FileTransfer.Status getStatusOfOutgoingIleTransfer(){
        return transfer.getStatus();
    }

    public double getProgressOfOutgoingIleTransfer(){
        return transfer.getProgress();
    }

    public boolean isDoneOfOutgoingIleTransfer(){
        return transfer.isDone();
    }

    public FileTransfer.Error getErrorOfOutgoingIleTransfer(){
        return transfer.getError();
    }
}
