package it.polimi.ingsw.messages.view;

import com.sun.javafx.iio.ios.IosDescriptor;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;
import java.io.Serializable;

public interface ViewMsg extends Serializable {
    void changeView(View view, ServerHandler server) throws IOException;
}
