package pl.edu.agh.iobber.core;

public interface MessageListener {

    void process(BaseManagerMessages baseManagerMessages, SimpleMessage simpleMessage);

}
