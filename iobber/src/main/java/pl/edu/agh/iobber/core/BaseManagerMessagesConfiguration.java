package pl.edu.agh.iobber.core;

/**
 * Created by HOUSE on 2014-05-20.
 */
public class BaseManagerMessagesConfiguration {

    public final static String NUMBER_OF_DAYS = "NUMBER_OF_DAYS";

    private int numberOfDays;

    public BaseManagerMessagesConfiguration(int numberOfDaysToSaveHistory){
        this.numberOfDays = numberOfDaysToSaveHistory;
    }

    public int getNumberOfDaysToSaveHistory(){
        return numberOfDays;
    }

}
