package it.polimi.ingsw.shared.messages.command;

import java.io.Serializable;

public class UsernameMsg implements Serializable {

    private String username;

    public UsernameMsg(String username) {
        this.username = username;
    }

    public String getUsername(){ return username; }

    @Override
    public String toString() {
        return "UsernameMsg{" +
                "username='" + username + '\'' +
                '}';
    }
}
