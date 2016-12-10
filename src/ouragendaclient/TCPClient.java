package ouragendaclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fbeneditovm
 */
public class TCPClient extends Thread{
    private Socket mySocket; //The client socket
    MessageHandler messageHandler;
    private DataInputStream in;
    private DataOutputStream out;
    
    TCPClient(int serverPort, MessageHandler handler){
        try {
            mySocket = new Socket("localhost", serverPort);
            messageHandler = handler;
            in = new DataInputStream(mySocket.getInputStream());
            out = new DataOutputStream(mySocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.start();
    }
    @Override
    public void run(){
        String buffer;
        int port;
        try{
            while(!mySocket.isClosed()){
                buffer = in.readUTF();
                if(buffer.equalsIgnoreCase("exit"))
                    break;
                messageHandler.handle(buffer);
            }
        }catch(IOException e){System.out.println("IO: "+e.getMessage());}
    }
    public void sendMessage(String message){
        try {
            out.writeUTF(message);
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
