package it.polimi.ingsw.client;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.*;

public class ClientTest {

    private static final String LOCALIP = "192.168.1.176";

    public static void println(BufferedWriter writer, String string) throws IOException{
        writer.write(string);
        writer.newLine();
    }

    @Test
    public void clientOneTest(){
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        final InputStream inputStream;
        try {
            println(writer,"1");
            println(writer, LOCALIP);
            println(writer,"7777");
            println(writer,"aaa");
            println(writer, "4");
            writer.flush();
            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            System.setIn(inputStream);
            Client.main(null);
        }
        catch (IOException e){
            fail();
        }
    }

    @Test
    public void clientTwoTest(){
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        final InputStream inputStream;
        try {
            println(writer,"1");
            println(writer, LOCALIP);
            println(writer,"7777");
            println(writer,"bbb");
            println(writer, "4");
            writer.flush();
            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            System.setIn(inputStream);
            Client.main(null);
        }
        catch (IOException e){
            fail();
        }
    }

    @Test
    public void clientThreeTest(){
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        final InputStream inputStream;
        try {
            println(writer,"1");
            println(writer, LOCALIP);
            println(writer,"7777");
            println(writer,"ccc");
            println(writer, "4");
            writer.flush();
            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            System.setIn(inputStream);
            //Scanner scanner = new Scanner(System.in);
            Client.main(null);
        }
        catch (IOException e){
            fail();
        }
    }

    @Test
    public void clientFourTest(){
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        final InputStream inputStream;
        try {
            println(writer,"1");
            println(writer, LOCALIP);
            println(writer,"7777");
            println(writer,"ddd");
            println(writer, "4");
            writer.flush();
            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            System.setIn(inputStream);
            Client.main(null);
        }
        catch (IOException e){
            fail();
        }
    }

}