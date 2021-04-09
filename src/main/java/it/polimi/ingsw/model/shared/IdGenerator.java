package it.polimi.ingsw.model.shared;

public class IdGenerator {

    private static int last = 0;

    private IdGenerator(){ }

    public static int getNext(){
        last ++;
        return last;
    }
}
