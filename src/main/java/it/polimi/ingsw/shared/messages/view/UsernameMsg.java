package it.polimi.ingsw.shared.messages.view;

public class UsernameMsg {

    private String username;

    public UsernameMsg(String username) {
        this.username = username;
    }

    public String getUsername(){ return username; }
}
