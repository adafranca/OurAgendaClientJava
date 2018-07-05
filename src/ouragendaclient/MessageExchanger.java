package ouragendaclient;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author fbeneditovm
 */
public class MessageExchanger implements MessageHandler{
    private static TCPClient client;
    private static boolean connected = false;
    private static Queue<String> messages = new LinkedList<String>();
    private static ArrayList<String> notification = new ArrayList();
    
    public MessageExchanger(int port){
        
        if(connected == false){
            client = new TCPClient(port, this);
            connected = true;
        }
        
    }
    
    public String getMessage(){
        
        String msg;
        
        if(!messages.isEmpty()){
            msg = messages.peek();
            messages.remove();
        }else{
            msg = "";
        }
        
        return msg;
    }
    
    public String getAnswer(String answerwait){
        String messageAnswer;
        String message = "";
        String[] section;
        
        do{
             messageAnswer = getMessage();
             
             if(!messageAnswer.equals("")){
                section = messageAnswer.split("&");
                message = section[0];
                System.out.println(message);
            }
      
        }while(!message.equals(answerwait));
        
        return messageAnswer;
    }
    
    public ArrayList<String> getNotification(){
        
        this.SendMessage("GET_NOTIFICATIONS");
        String messageAnswer;
        String[] section = null;
        String message = "";
        int qtdnotification = 0;
        ArrayList<String> msg_notification = new ArrayList();
        
        do{
             messageAnswer = this.getMessage();
            
             if(!messageAnswer.equals("")){
                section = messageAnswer.split("&");
                message = section[0];
                System.out.println(message);
            }
           
        }while(!message.equals("GET_NOTIFICATIONS_FB"));
        
        if(section[1].substring(8).equals("SUCCESS")){
            
            qtdnotification = Integer.parseInt(section[2].substring(10));
        
            for(int i=0;i<qtdnotification;i++){
                
                do{
                    messageAnswer = this.getMessage();
            
                    if(!messageAnswer.equals("")){
                       section = messageAnswer.split("&");
                       message = section[0];
                    }
                    System.out.println(messageAnswer);
                }while(!message.equals("NOTIFICATION"));
                
                
                msg_notification.add(messageAnswer);
            }
            
        }
        
        return msg_notification;
    }
    
    public void SendMessage(String message){
        client.sendMessage(message);
    }
    
    public boolean checkNotification(){
        if(notification.size() > 0){
            notification.clear();
            return true;
        }
        
        return false;
    }
    

    @Override
    public void handle(String message) {
        System.out.println(message);
        if(message.equals("CHECK_NOTIFICATIONS"))
            notification.add(message);
        else
            messages.add(message);
    }
    
}
