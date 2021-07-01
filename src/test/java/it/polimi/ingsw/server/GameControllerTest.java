package it.polimi.ingsw.server;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

public class GameControllerTest {

    @Test
    public void serializationTest(){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            GameController gameController = new GameController(Set.of("aaa", "bbb", "ccc", "ddd"));
            gameController.setVirtualView(Mockito.mock(VirtualView.class));
            oos.writeObject(gameController);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            GameController readObject = (GameController) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            fail();
        }
    }
}