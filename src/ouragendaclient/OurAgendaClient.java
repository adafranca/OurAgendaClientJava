package ouragendaclient;

import java.util.Scanner;

/**
 *
 * @author fbeneditovm
 */
public class OurAgendaClient{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MessageExchanger mEx = new MessageExchanger(10000);
        mEx.start();
    }
        
}
