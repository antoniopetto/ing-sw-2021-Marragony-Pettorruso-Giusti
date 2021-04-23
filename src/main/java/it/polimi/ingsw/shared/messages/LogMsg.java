package it.polimi.ingsw.shared.messages;

public class LogMsg {

    private final String log;

    public LogMsg(String log){
        this.log = log;
    }

    public String getLogInfo(){ return log; }
}
