package pl.edu.agh.iobber.core;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.muc.SubjectUpdatedListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import pl.edu.agh.iobber.core.exceptions.CannotChangeSubjectOfTheRoomException;
import pl.edu.agh.iobber.core.exceptions.CannotConfigurePrivateRoomException;
import pl.edu.agh.iobber.core.exceptions.CannotCreateMultiUSerChatException;
import pl.edu.agh.iobber.core.exceptions.CannotGetRoomInfoFromMultiUserChat;
import pl.edu.agh.iobber.core.exceptions.CannotGrantAdminException;
import pl.edu.agh.iobber.core.exceptions.CannotGrantModeratorException;
import pl.edu.agh.iobber.core.exceptions.CannotGrantVoiceException;
import pl.edu.agh.iobber.core.exceptions.CannotJoinToTheMultiUserChatException;
import pl.edu.agh.iobber.core.exceptions.CannotRevokeAdminException;
import pl.edu.agh.iobber.core.exceptions.CannotRevokeModeratorException;
import pl.edu.agh.iobber.core.exceptions.CannotRevokeVoiceException;
import pl.edu.agh.iobber.core.exceptions.CannotSendConfigurationFormException;
import pl.edu.agh.iobber.core.exceptions.CannotSendMessageToMultiUserChatException;

import static java.lang.String.format;

//any informations and tips -> http://www.igniterealtime.org/builds/smack/dailybuilds/documentation/extensions/muc.html

public class MultiConversation implements MsgListener{

    public static final String ROOM_TYPE_INSTANT = "Instant room";
    public static final String ROOM_TYPE_RESERVED = "Reserved room";

    private List<MsgListener> listeners = new LinkedList<MsgListener>();
    private MultiUserChat muc;
    private MultiUserBaseManager multiUserBaseManager;
    private Logger logger = Logger.getLogger(MultiConversation.class.getSimpleName());
    private String typeOfRoom;
    private String nameOfTheRoom;
    private String nickname;
    private XMPPConnection xmppConnection;
    private Form form;
    private String ownerOfPrivateRoom;

    public MultiConversation(XMPPConnection conn, String nameOfTheRoom, String nickname){
        logger.info("MultiConversation constructor induced");
        this.nickname = nickname;
        this.xmppConnection = conn;
        this.nameOfTheRoom = nameOfTheRoom;
    }

    public void setBaseManager(MultiUserBaseManager multiUserBaseManager){
        logger.info("New base managener is added");
        this.multiUserBaseManager = multiUserBaseManager;
    }

    public void setOwnerOfReservedRoom(String name){
        ownerOfPrivateRoom = name;
    }

    public void addMessageListener(MsgListener msgListener) {
        logger.info(format("msg listener %s added", msgListener));
        listeners.add(msgListener);
    }

    public void sendMessage(String message) throws CannotSendMessageToMultiUserChatException {
        try {
            muc.sendMessage(message);
        } catch (XMPPException e) {
            logger.info("cannot send message to multiusechat " + message + " " + e.toString());
            throw new CannotSendMessageToMultiUserChatException(e);
        }
    }

    public void setTypeOfRoom(String type) throws CannotConfigurePrivateRoomException {
        typeOfRoom = type;
        if(type.equals(ROOM_TYPE_INSTANT)){
            form = new Form(Form.TYPE_SUBMIT);
            form.setAnswer("muc#roomconfig_changesubject", true);
        }else{
            Form form = null;
            try {
                form = muc.getConfigurationForm();
            } catch (XMPPException e) {
                logger.info("Cannot configurePrivateRoom " + e.toString());
                throw new CannotConfigurePrivateRoomException(e);
            }
            Form submitForm = form.createAnswerForm();
            for (Iterator fields = form.getFields(); fields.hasNext();) {
                FormField field = (FormField) fields.next();
                if (!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null) {
                    // Sets the default value as the answer
                    submitForm.setDefaultAnswer(field.getVariable());
                }
            }
            List owners = new ArrayList();
            owners.add(ownerOfPrivateRoom);
            submitForm.setAnswer("muc#roomconfig_roomowners", owners);
            submitForm.setAnswer("muc#roomconfig_changesubject", true);
        }
        logger.info("Type of multi user char set");
    }

    public void createRoom() throws CannotCreateMultiUSerChatException, CannotSendConfigurationFormException {
        muc = new MultiUserChat(xmppConnection, nameOfTheRoom);
        try {
            muc.create(nickname);
        } catch (XMPPException e) {
            logger.info("Cannot create private room " + e.toString());
            throw new CannotCreateMultiUSerChatException(e);
        }
        try {
            muc.sendConfigurationForm(form);
        } catch (XMPPException e) {
            logger.info("Cannot send configuration form " + e.toString());
            throw new CannotSendConfigurationFormException(e);
        }
    }

    public MultiUserChat getMultiUserChat(){
        return muc;
    }

    public void joinToRoom(String name) throws CannotJoinToTheMultiUserChatException {
        muc = new MultiUserChat(xmppConnection, nameOfTheRoom);
        try {
            muc.join(name);
        } catch (XMPPException e) {
            logger.info("Cannot join to the multi user chat " + e.toString());
            throw new CannotJoinToTheMultiUserChatException(e);
        }
    }

    public void joinToRoom(String name, String password) throws CannotJoinToTheMultiUserChatException {
        muc = new MultiUserChat(xmppConnection, nameOfTheRoom);
        try {
            muc.join(name, password);
        } catch (XMPPException e) {
            logger.info("Cannot join to the multi user chat " + e.toString());
            throw new CannotJoinToTheMultiUserChatException(e);
        }
    }

    public void addInvitationRejectionListener(InvitationRejectionListener invitationRejectionListener){
        muc.addInvitationRejectionListener(invitationRejectionListener);
    }

    public boolean invite(String name, String reason){
        boolean supports = MultiUserChat.isServiceEnabled(xmppConnection, name);
        if(supports) {
            muc.invite(name, reason);
        }
        return supports;
    }

    public void addSubjectUpdatedListener(SubjectUpdatedListener subjectUpdatedListener){
        muc.addSubjectUpdatedListener(subjectUpdatedListener);
    }

    public void changeSubject(String newSubject) throws CannotChangeSubjectOfTheRoomException {
        try {
            muc.changeSubject(newSubject);
        } catch (XMPPException e) {
            logger.info("Cannot change the subject of the room " + e.toString());
            throw new CannotChangeSubjectOfTheRoomException(e);
        }
    }

    public RoomInfo getInfoAboutRoom(String nameOfRoom) throws CannotGetRoomInfoFromMultiUserChat {
        try {
            return MultiUserChat.getRoomInfo(xmppConnection, nameOfRoom);
        } catch (XMPPException e) {
            logger.info("Cannot get RoomInfo " + e.toString());
            throw new CannotGetRoomInfoFromMultiUserChat(e);
        }
    }

    public void grantAdmin(String name) throws CannotGrantAdminException {
        try {
            muc.grantAdmin(name);
        } catch (XMPPException e) {
            logger.info("cannot grant admin for " + name + " " + e.toString());
            throw new CannotGrantAdminException(e);
        }
    }

    public void revokeAdmin(String name) throws CannotRevokeAdminException {
        try {
            muc.revokeAdmin(name);
        } catch (XMPPException e) {
            logger.info("cannot revoke admin for " + name + " " + e.toString());
            throw new CannotRevokeAdminException(e);
        }
    }

    public void grantVoice(String name) throws CannotGrantVoiceException {
        try {
            muc.grantVoice(name);
        } catch (XMPPException e) {
            logger.info("cannot grant voice for " + name + " " + e.toString());
            throw new CannotGrantVoiceException(e);
        }
    }

    public void revokeVoice(String name) throws CannotRevokeVoiceException {
        try {
            muc.revokeVoice(name);
        } catch (XMPPException e) {
            logger.info("cannot revoke voice for " + name + " " + e.toString());
            throw new CannotRevokeVoiceException(e);
        }
    }

    public void grantModerator(String name) throws CannotGrantModeratorException {
        try {
            muc.grantModerator(name);
        } catch (XMPPException e) {
            logger.info("cannot grant moderator for " + name + " " + e.toString());
            throw new CannotGrantModeratorException(e);
        }
    }

    public void revokeModerator(String name) throws CannotRevokeModeratorException {
        try {
            muc.revokeModerator(name);
        } catch (XMPPException e) {
            logger.info("cannot revoke moderator for " + name + " " + e.toString());
            throw new CannotRevokeModeratorException(e);
        }
    }

    @Override
    public void onMessage(SimpleMessage message) {
        //TODO
    }
}
