package pl.edu.agh.iobber.android.conversation;

import org.jivesoftware.smackx.muc.SubjectUpdatedListener;

public class AndroidSubjectUpdatedListener implements SubjectUpdatedListener {
    @Override
    public void subjectUpdated(String s, String s2) {
        //TODO
        //ta klase mozna byloby wykorzystywac najprawdpodobniej do odbierania informacji ile osob przeczytalo
        //wiadomosc. Kazzdy kto przeczytal wiadomosc klika w nia i poprzez to zmienia podmiot na swoj nick
        //wszyscy odbieraja ta zmiane i wiedza ze ta osoba przeczytala ostatnia wiadomosc
    }
}
