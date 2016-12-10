package ouragendaclient;

import java.util.Scanner;

/**
 *
 * @author fbeneditovm
 */
public class MessageExchanger implements MessageHandler{
    private TCPClient client;
    private Scanner scanner;
    
    public MessageExchanger(int port){
        client = new TCPClient(port, this);
        scanner = new Scanner(System.in);
    }
    
    public void start(){
        String buffer;
        System.out.println("Start sending your messages:");
        while(true){
            buffer = scanner.nextLine();
            client.sendMessage(buffer);
            if(buffer.equalsIgnoreCase("exit"))
                break;
        }
    }

    @Override
    public void handle(String message) {
        System.out.println(message);
    }
    
}
