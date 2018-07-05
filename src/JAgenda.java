
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import ouragendaclient.MessageExchanger;
import ouragendaclient.ModifySubstring;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ada França
 */
public class JAgenda extends javax.swing.JFrame {

    /**
     * Creates new form JAgenda
     */
    String userId;
    DefaultTableModel modelnotificacoes = new DefaultTableModel();
    
    Thread thread = new Thread(new Runnable() { public void run(){
                                try {
                                    while(true){
                                        Thread.sleep(1000*20);
                                        getNotifications();
                                    }    
                                }catch(Exception Ex){
                                    
                                }
                            }
                    });
    
    public JAgenda(String userName, String userId) {
        
        initComponents();
        this.userId = userId;
        tUsuario.setText(userName);
        thread.start();
        getEvents();
        String[] section = jCalendar.getDate().toString().split(" ");
        String day = section[2];
        String year = section[5];
        String month = section[1];
        getEventsUsersDay(day, month, year);
    }
    
    private void getUsers(){
        
        MessageExchanger me = new MessageExchanger(10000);
        ModifySubstring m = new ModifySubstring();
        String messageAnswer;
        ArrayList users = new ArrayList();
        String[] section;
        String message = "";
        int qtdusers = 0;
       
        me.SendMessage("SHOW_USERS");
        
        messageAnswer = me.getAnswer("SHOW_USERS_FB");
        
        section = messageAnswer.split("&");
        
        if(section[1].substring(8).equals("SUCCESS")){
            
            qtdusers = Integer.parseInt(section[2].substring(9));
            
            for(int i=0;i<qtdusers;i++){
                
                messageAnswer = me.getAnswer("USER");
                section = messageAnswer.split("&");
                
            }    
        }
    }
    
    public String getEventByID(String id){
       DefaultTableModel model = (DefaultTableModel) tEvents.getModel();
       String name_event = ""; 
       for(int i=0;i<model.getRowCount();i++){
           if(id.equals(model.getValueAt(i, 0))){
               name_event = String.valueOf(model.getValueAt(i,1));
           }
        }
       return name_event;
    }   
    
    public void getNotifications(){
        MessageExchanger me = new MessageExchanger(10000);
        String message;
        JFrame jframe = new JFrame();
        
        if(me.checkNotification()){
            ArrayList<String> notificacoes;

                notificacoes = me.getNotification();
                 int i = 0;
                 while(i < notificacoes.size()){
                     String[] section = notificacoes.get(i).split("&");
                     System.out.println(section[2].substring(6));
                     switch(section[2].substring(6)){
                         case "NEW_INVITATION":
                                int resposta;
                                resposta = JOptionPane.showConfirmDialog(null, "Você foi convidado para participar do evento '" + getEventByID(section[3].substring(10)) + "'. Gostaria de participar?");
                                if(resposta == JOptionPane.YES_OPTION){
                                    me.SendMessage("ANSWER_INVITATION&-accept=TRUE&-event_id=" + section[3].substring(10));
                                }else{
                                    me.SendMessage("ANSWER_INVITATION&-accept=FALSE&-event_id=" + section[3].substring(10));
                                }
                                getEvents();
                                break;
                         case "INVITE_ANSWER":
                               if(section[5].substring(7).equals("true")){
                                    message = "O convidado " + section[4].substring(7) + " aceitou seu convite para o evento '" + getEventByID(section[3].substring(10)) + "'";
                                    
                               }
                               else{
                                   message = "O convidado " + section[4].substring(7) + "não aceitou seu convite para o evento " + section[2].substring(10);
                               } 
                               JOptionPane.showMessageDialog(jframe,message);
                               createNewNotification(message);
                               getEvents();
                               break;
                         case "EVENT_DELETED":
                               message = "O evento " + section[3].substring(6) + " foi deletado.";
                               JOptionPane.showMessageDialog(jframe,message);
                               createNewNotification(message);
                               getEvents();
                               break;
                         case "EVENT_UPDATED":
                               JOptionPane.showMessageDialog(jframe,section[0]);
                               break;
                     }
                     
                  i++;   
                 }
            }
        }    
    
    public void createNewNotification(String message){
        modelnotificacoes = (DefaultTableModel) tNotificacoes.getModel();
        modelnotificacoes.addRow(new Object[]{message});
        
    }
    
    public void getEvents(){
        
        DefaultTableModel model = (DefaultTableModel) tEvents.getModel();
        MessageExchanger me = new MessageExchanger(10000);
        ModifySubstring m = new ModifySubstring();
        String messageAnswer;
        ArrayList events = new ArrayList();
        String[] section;
        int i=0,qtdEvents=0;
        
        while (model.getRowCount() > 0)  // limpa a tabela toda vez 
              model.removeRow(0);
        
        me.SendMessage("SHOW_EVENTS&-date=ALL");
        messageAnswer = me.getMessage();
        
        messageAnswer = me.getAnswer("SHOW_EVENTS_FB");
        
        section = messageAnswer.split("&");
       
        if(section[1].substring(8).equals("SUCCESS") && Integer.parseInt(section[2].substring(10)) > 0){
           qtdEvents = Integer.parseInt(section[2].substring(10));
           for(int j=0;j<qtdEvents;j++){
               messageAnswer = me.getAnswer("EVENT");
               events = m.getEvents(messageAnswer);
               model.addRow(new Object[] { events.get(1),events.get(2),events.get(5),events.get(3),events.get(4) ,events.get(4), events.get(6),events.get(7)});
           }
        }
        
    }
    
    public void getEventsUsersDay(String day,String month,String year){
        JFrame jframe = new JFrame();
        MessageExchanger me = new MessageExchanger(10000);
        String[] section;
        
        switch(month){
            case "Jan":
                month = "01";
                break;
            case "Feb":
                month = "02";
                break;
            case "Mar":
                month = "03";
                break;
            case "Apr":
                month = "04";
                break;
            case "May":
                month = "05";
                break;
            case "Jun":
                month = "06";
                break;
            case "Jul":
                month = "07";
                break;
            case "Aug":
                month = "08";
                break;
            case "Sep":
                month = "09";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;    
        }
        
        textDATA.setText(day + "/" + month + "/" + year);
        ModifySubstring m = new ModifySubstring();
        String messageAnswer;
        ArrayList<String> users = new ArrayList();
        String message = "";
        int qtdusers = 0;
       
        me.SendMessage("SHOW_USERS");
        
        messageAnswer = me.getAnswer("SHOW_USERS_FB");
        
        section = messageAnswer.split("&");
        
        if(section[1].substring(8).equals("SUCCESS")){
            
            qtdusers = Integer.parseInt(section[2].substring(9));
            
            for(int i=0;i<qtdusers;i++){
                messageAnswer = me.getAnswer("USER");
                section = messageAnswer.split("&");
                String user = section[3].substring(6);
                users.add(user);
            }
            
            for(int i=0;i<qtdusers;i++){
                
                me.SendMessage("GET_BUSY_INTERVALS&-guest=" + users.get(i) + "&-date=" + year + "-" + month + "-" + day);
                messageAnswer = me.getAnswer("GET_BUSY_INTERVALS_FB");
                
                if(!messageAnswer.equals("GET_BUSY_INTERVALS_FB&-status=<FAIL>")){
                    section = messageAnswer.split("&");
                    message += "Usuário " + users.get(i) + ": " + section[2].substring(8) + "\n";
                }
                else
                {
                    
                }
            }
        }
       textAgenda.setText(message);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tEvents = new javax.swing.JTable();
        btnNewEvent = new javax.swing.JButton();
        btnEditEvent = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jCalendar = new com.toedter.calendar.JCalendar();
        jScrollPane3 = new javax.swing.JScrollPane();
        textAgenda = new javax.swing.JTextArea();
        btnVisualizarAgenda = new javax.swing.JButton();
        textDATA = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tUserName = new javax.swing.JTextField();
        tUserPass = new javax.swing.JPasswordField();
        btnNewUser = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        tUsuario = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tNotificacoes = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(1200, 600));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(700, 600));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tEvents.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tEvents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nome", "Local", "Data", "Horario Inicial", "Horario Final", "Descrição", "Dono_ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tEvents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tEventsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tEvents);
        if (tEvents.getColumnModel().getColumnCount() > 0) {
            tEvents.getColumnModel().getColumn(0).setPreferredWidth(20);
            tEvents.getColumnModel().getColumn(1).setResizable(false);
            tEvents.getColumnModel().getColumn(2).setResizable(false);
            tEvents.getColumnModel().getColumn(3).setResizable(false);
            tEvents.getColumnModel().getColumn(4).setResizable(false);
            tEvents.getColumnModel().getColumn(5).setResizable(false);
            tEvents.getColumnModel().getColumn(6).setResizable(false);
            tEvents.getColumnModel().getColumn(7).setResizable(false);
        }

        btnNewEvent.setText("Novo Evento");
        btnNewEvent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewEventMouseClicked(evt);
            }
        });

        btnEditEvent.setText("Editar Evento");
        btnEditEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditEventActionPerformed(evt);
            }
        });

        btnExcluir.setText("Excluir Evento");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(btnNewEvent)
                .addGap(49, 49, 49)
                .addComponent(btnEditEvent)
                .addGap(51, 51, 51)
                .addComponent(btnExcluir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNewEvent)
                    .addComponent(btnEditEvent)
                    .addComponent(btnExcluir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Meus Eventos", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jCalendar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCalendarMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jCalendarMousePressed(evt);
            }
        });

        textAgenda.setColumns(20);
        textAgenda.setRows(5);
        jScrollPane3.setViewportView(textAgenda);

        btnVisualizarAgenda.setText("Visualizar");
        btnVisualizarAgenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVisualizarAgendaActionPerformed(evt);
            }
        });

        textDATA.setText("DATA");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnVisualizarAgenda)
                    .addComponent(jCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(textDATA)
                .addGap(157, 157, 157))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(textDATA)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addComponent(jCalendar, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnVisualizarAgenda)
                .addContainerGap(163, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Agenda", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Nome");

        jLabel2.setText("Senha");

        btnNewUser.setText("Criar usuário");
        btnNewUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewUserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(182, 182, 182)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tUserName))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnNewUser)
                            .addComponent(tUserPass, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(438, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tUserPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(btnNewUser)
                .addContainerGap(349, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Novo Usuário", jPanel5);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 76, 770, -1));

        btnSair.setText("Sair");
        btnSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSairMouseClicked(evt);
            }
        });
        jPanel1.add(btnSair, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 10, 147, -1));

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        tUsuario.setText("usuario");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(tUsuario)
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(tUsuario)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 20));

        tNotificacoes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Notificações"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tNotificacoes);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 90, 270, 480));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1056, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewEventMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewEventMouseClicked
        // TODO add your handling code here:
        ArrayList event = new ArrayList();
        JEvents jevents = new JEvents(this,event);
        jevents.setVisible(true);
        getNotifications();
    }//GEN-LAST:event_btnNewEventMouseClicked

    private void btnSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSairMouseClicked
        // TODO add your handling code here:
        MessageExchanger me = new MessageExchanger(10000);
        me.SendMessage("LOGOUT");
        
        String messageAnswer = me.getMessage();
        
        while(messageAnswer.equals("")){
            messageAnswer = me.getMessage();
            System.out.println("");
        }
        
        if(messageAnswer.equals("LOGOUT_FB&-status=SUCCESS")){
            JFrame jframe = new JFrame();
            JOptionPane.showMessageDialog(jframe,"Conexão finalizada com sucesso.");
            this.dispose();
            JLogin jlogin = new JLogin();
            jlogin.setVisible(true);
        }else{
            JFrame jframe = new JFrame();
            JOptionPane.showMessageDialog(jframe,"Conexão não pode ser finalizada.");
        }
        
        this.dispose();
    }//GEN-LAST:event_btnSairMouseClicked

    private void tEventsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tEventsMouseClicked
        // TODO add your handling code here:
        this.btnEditEvent.enable();
        
    }//GEN-LAST:event_tEventsMouseClicked

    private void btnEditEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditEventActionPerformed
        // TODO add your handling code here:
        
        if(userId.equals(String.valueOf(tEvents.getValueAt(tEvents.getSelectedRow(), 7)))){
            ArrayList<String> event = new ArrayList();
            event.add(String.valueOf(tEvents.getValueAt(tEvents.getSelectedRow(), 0))); // id_evento
            event.add(String.valueOf(tEvents.getValueAt(tEvents.getSelectedRow(), 1))); // nome_evento
            event.add(String.valueOf(tEvents.getValueAt(tEvents.getSelectedRow(), 2))); // local_evento
            event.add(String.valueOf(tEvents.getValueAt(tEvents.getSelectedRow(), 3))); // data_evento
            event.add(String.valueOf(tEvents.getValueAt(tEvents.getSelectedRow(), 4))); // horario_inicial
            event.add(String.valueOf(tEvents.getValueAt(tEvents.getSelectedRow(), 5))); // horario_final
            event.add(String.valueOf(tEvents.getValueAt(tEvents.getSelectedRow(), 6))); // descricao
        
            JEvents je = new JEvents(this,event);
            je.setVisible(true);
        }
        else{
            JFrame jframe = new JFrame();
            JOptionPane.showMessageDialog(jframe,"Você não pode editar o evento, pois não é criador(a).");
        }
    }//GEN-LAST:event_btnEditEventActionPerformed

    private void jCalendarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCalendarMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jCalendarMouseClicked

    private void jCalendarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCalendarMousePressed
        // TODO add your handling code here:
        JFrame jframe = new JFrame();
        JOptionPane.showMessageDialog(jframe,"Conexão não pode ser finalizada.");
    }//GEN-LAST:event_jCalendarMousePressed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // TODO add your handling code here:
        String id = String.valueOf(tEvents.getValueAt(tEvents.getSelectedRow(), 0));
        MessageExchanger me = new MessageExchanger(10000);
        String messageAnswer;
        
        me.SendMessage("DELETE_EVENT&-event_id=" + id);
        messageAnswer = me.getAnswer("DELETE_EVENT_FB");
        
        if(messageAnswer.equals("DELETE_EVENT_FB&-status=SUCCESS")){
            JFrame jframe = new JFrame();
            JOptionPane.showMessageDialog(jframe,"Evento deletado com sucesso");
            this.getEvents();
        }else{
            JFrame jframe = new JFrame();
            JOptionPane.showMessageDialog(jframe,"Evento não foi deletado com sucesso");
        }
        
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnVisualizarAgendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVisualizarAgendaActionPerformed
        // TODO add your handling code here:
        String[] section = jCalendar.getDate().toString().split(" ");
        String day = section[2];
        String year = section[5];
        String month = section[1];
        
        this.getEventsUsersDay(day, month, year);
    }//GEN-LAST:event_btnVisualizarAgendaActionPerformed

    private void btnNewUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewUserActionPerformed
        // TODO add your handling code here:
        String userName = tUserName.getText();
        String userPsswd = new String (tUserPass.getPassword());
        String message = "CREATE_USER&-u="+userName+"&-p="+userPsswd+"";
        
        MessageExchanger me = new MessageExchanger(10000);
        me.SendMessage(message);
        
        String messageAnswer = me.getMessage();
        
        while(messageAnswer.equals("")){
            messageAnswer = me.getMessage();
            System.out.println("");
        }
        
        if(messageAnswer.equals("CREATE_USER_FB&-status=SUCCESS")){
            JFrame jframe = new JFrame();
            JOptionPane.showMessageDialog(jframe,"Usuário criado com sucesso.");
        }else{
            JFrame jframe = new JFrame();
            JOptionPane.showMessageDialog(jframe,"Usuário não foi criado.");
        }
    }//GEN-LAST:event_btnNewUserActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditEvent;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnNewEvent;
    private javax.swing.JButton btnNewUser;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnVisualizarAgenda;
    private com.toedter.calendar.JCalendar jCalendar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tEvents;
    private javax.swing.JTable tNotificacoes;
    private javax.swing.JTextField tUserName;
    private javax.swing.JPasswordField tUserPass;
    private javax.swing.JLabel tUsuario;
    private javax.swing.JTextArea textAgenda;
    private javax.swing.JLabel textDATA;
    // End of variables declaration//GEN-END:variables
}
