package it.polimi.ingsw.messages.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

public class NPlayerRequestMsg implements ViewMsg {

    @Override
    public void changeView(View view, ServerHandler handler) {
        boolean valid=false;
        while(!valid)
        {
            try{
                int number = view.getNumber();
                NPlayerMsg msg = new NPlayerMsg(number);
                handler.writeObject(msg);
                valid=true;
            }catch (Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Try again");
            }
        }
    }
}
