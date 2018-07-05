/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouragendaclient;

import java.util.ArrayList;

/**
 *
 * @author Ada França
 */
public class ModifySubstring {
    
    public ArrayList getEvents(String message){
        //Get the arguments from the message string
        String[] section = message.split("&");
        String[] data = section[4].substring(17).split(" ");
        ArrayList event = new ArrayList();
        event.add(section[1].substring(8)); // Numero
        event.add(section[2].substring(4)); // ID
        event.add(section[3].substring(6)); // Nome
        event.add(data[0]); // data 
        event.add(section[5].substring(15)); // Tempo_Final
        event.add(section[6].substring(7)); // local
        event.add(section[7].substring(6)); // Descricao
        event.add(section[9].substring(10));
        return event;
    }    
}
