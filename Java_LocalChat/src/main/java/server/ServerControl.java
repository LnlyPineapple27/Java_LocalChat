/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.Set;
import java.util.Vector;
import javax.swing.JOptionPane;
import sound.notification_sound;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
/**
 *
 * @author admin
 */
// This class will provide GUI for server management
public class ServerControl extends javax.swing.JFrame {

    /**
     * Creates new form ServerControll
     */
    private boolean start_button_status;
    private boolean has_sound;
    private boolean has_date;
    private boolean has_time;
    private boolean auto_ratify;
    private notification_sound _player;
    DateTimeFormatter _day;
    DateTimeFormatter _time;
    ServerCore core;
    public ServerControl() {
        
        //System.out.println("FUCK");
        _player = new notification_sound();
        start_button_status = has_sound = has_date = has_time = auto_ratify = true;
        _day = DateTimeFormatter.ofPattern("dd/MM/yyyy");  
        _time = DateTimeFormatter.ofPattern("HH:mm:ss");
        initComponents();
        /*ArrayList<String> list = new ArrayList<String>(){{
            add("Dat1");
            add("datdeptrai");
            for(int i = 0; i < 30; ++i){
                add("datdeptrai" + i);
            }
        }};
        updateOnlineList(list);*/
        this.setVisible(true);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        TabsTree = new javax.swing.JTabbedPane();
        OptionTab = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        serverbutton = new javax.swing.JButton();
        PortField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        LogoLabel = new javax.swing.JLabel();
        EventTab = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        ClearButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        soundCheckBox = new javax.swing.JCheckBox();
        HasDateCheckBox = new javax.swing.JCheckBox();
        HasTimeCheckBox = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        LogText = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server Manager");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        OptionTab.setLayout(new java.awt.BorderLayout(1, 1));

        jPanel3.setLayout(new java.awt.GridBagLayout());

        serverbutton.setText("Start Server");
        serverbutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                serverbuttonMousePressed(evt);
            }
        });
        serverbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverbuttonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(serverbutton, gridBagConstraints);

        PortField.setText("1881");
        PortField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PortFieldKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(PortField, gridBagConstraints);

        jLabel1.setText("Port");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel3.add(jLabel1, gridBagConstraints);

        OptionTab.add(jPanel3, java.awt.BorderLayout.LINE_END);

        try{
            //javax.swing.ImageIcon icon = new javax.swing.ImageIcon("hcmus_logo.png");
            LogoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/server/hcmus_logo.png"))); // NOI18N
        }
        catch(Exception e){
            LogoLabel.setText("Logo image is inaccessible!");
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LogoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LogoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
        );

        OptionTab.add(jPanel4, java.awt.BorderLayout.CENTER);

        TabsTree.addTab("Options", OptionTab);

        EventTab.setLayout(new java.awt.BorderLayout(1, 1));

        jPanel1.setLayout(new java.awt.BorderLayout());

        ClearButton.setText("Clear Log");
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });
        jPanel1.add(ClearButton, java.awt.BorderLayout.LINE_START);

        jPanel2.setLayout(new java.awt.GridLayout(2, 2));

        soundCheckBox.setSelected(true);
        soundCheckBox.setText("Notification sound");
        soundCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soundCheckBoxActionPerformed(evt);
            }
        });
        jPanel2.add(soundCheckBox);

        HasDateCheckBox.setSelected(true);
        HasDateCheckBox.setText("Show date in log");
        HasDateCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HasDateCheckBoxActionPerformed(evt);
            }
        });
        jPanel2.add(HasDateCheckBox);

        HasTimeCheckBox.setSelected(true);
        HasTimeCheckBox.setText("Show time in log");
        HasTimeCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HasTimeCheckBoxActionPerformed(evt);
            }
        });
        jPanel2.add(HasTimeCheckBox);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        EventTab.add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jPanel5.setLayout(new java.awt.BorderLayout());

        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Online User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        userTable.setColumnSelectionAllowed(true);
        userTable.getTableHeader().setReorderingAllowed(false);
        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                userTableMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(userTable);
        userTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (userTable.getColumnModel().getColumnCount() > 0) {
            userTable.getColumnModel().getColumn(0).setResizable(false);
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 123, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 426, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel6, java.awt.BorderLayout.LINE_START);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jLabel2.setLabelFor(LogText);
        jLabel2.setText("Server Log");
        jPanel7.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        LogText.setEditable(false);
        LogText.setColumns(20);
        LogText.setRows(5);
        jScrollPane2.setViewportView(LogText);

        jPanel7.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        EventTab.add(jPanel5, java.awt.BorderLayout.CENTER);

        TabsTree.addTab("Events Log", EventTab);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabsTree, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabsTree, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    void updateOnlineList(Set<String> user_list){
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) userTable.getModel();
        model.setRowCount(0);//Clear table
        if (user_list != null){
            for(String i : user_list){
                Vector row = new Vector();
                row.add(i);
                model.addRow(row);
            }
        }
        userTable.revalidate();//Show changes
    }
    private void serverbuttonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_serverbuttonMousePressed
        // TODO add your handling code here:
        
        
        if (this.start_button_status){ // Start server
            String _port = PortField.getText().trim();
            if (_port.length() > 0){
                int port;
                try {
                    port = Integer.parseInt(_port);
                }
                catch(NumberFormatException e){
                   JOptionPane.showMessageDialog(this, "Port should only be numbers", "Invalid input", JOptionPane.ERROR_MESSAGE);
                   return;
                }

                
                System.out.println("Starting server on port " + port);                
                printLog("Starting server", "on port " + Integer.toString(port));
                
                try{
                    this.core = new ServerCore(this, port);
                    this.core.start();
                    
                }
                catch (Exception e){
                    if (has_sound) _player.play_sound();
                    this.printLog("STH_WENT_WRONG", e.getMessage());
                    return;
                }
                this.printLog("SERVER", "is running");
                serverbutton.setText("Stop Server");
                this.PortField.setEditable(false);
            }
            else{
                if (has_sound) _player.play_sound();
                JOptionPane.showMessageDialog(this, "Please input a port number", "Empty input", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        else{ //Stop server
            if (has_sound) _player.play_sound();
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to terminate the current server process?", "Warning!!!", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION){
                System.out.println("Server stopped");
                printLog("Stopping server", "goodbye");
                try{
                    this.core.stop();
                }
                catch (Exception e){
                    this.printLog("STH_WENT_WRONG", e.getMessage());
                    return;
                }
                this.printLog("SERVER", "stopped");
                serverbutton.setText("Start Server");
                this.PortField.setEditable(true);
            }
            else return;
        }
        
        start_button_status = !start_button_status;
    }//GEN-LAST:event_serverbuttonMousePressed

    private void soundCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundCheckBoxActionPerformed
        // TODO add your handling code here:        
        if(soundCheckBox.isSelected())
            has_sound = true;
        else
            has_sound = false;
        if (has_sound) _player.play_sound();
    }//GEN-LAST:event_soundCheckBoxActionPerformed

    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
        // TODO add your handling code here:
        LogText.selectAll();
        LogText.replaceSelection("");
        if (this.has_sound) _player.play_sound();
    }//GEN-LAST:event_ClearButtonActionPerformed

    private void userTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userTableMousePressed
        // TODO add your handling code here:
        javax.swing.JTable source = (javax.swing.JTable)evt.getSource();
        int row = source.rowAtPoint( evt.getPoint() );
        int column = source.columnAtPoint( evt.getPoint() );
        String s = source.getModel().getValueAt(row, column)+"";
        if (has_sound) _player.play_sound();
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to remove this user: " + s, "USER REMOVAL", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION){
            if(this.core.deleteUser(s))
                System.out.println("User-" + s + " was removed from this server!");
            else System.out.println("{Error]: User-" + s + " doesn't exist in server! Maybe sth went wrong :<");
        }
            
    }//GEN-LAST:event_userTableMousePressed

    private void HasTimeCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HasTimeCheckBoxActionPerformed
        // TODO add your handling code here:
        if (HasTimeCheckBox.isSelected())
            has_time = true;
        else 
            has_time = false;
        if (has_sound) _player.play_sound();
    }//GEN-LAST:event_HasTimeCheckBoxActionPerformed

    private void HasDateCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HasDateCheckBoxActionPerformed
        // TODO add your handling code here:
        if (HasDateCheckBox.isSelected())
            has_date = true;
        else
            has_date = false;
        if (has_sound) _player.play_sound();
    }//GEN-LAST:event_HasDateCheckBoxActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        if(!this.start_button_status){
            System.out.println("Server stopped");
            this.printLog("Stopping server", "goodbye");
            try{
                this.core.stop();
            }
            catch (Exception e){
                this.printLog("STH_WENT_WRONG", e.getMessage());
                return;
            }
            this.printLog("SERVER", "stopped");
            JOptionPane.showMessageDialog(null, "Server shutdown", "Goodbye", JOptionPane.INFORMATION_MESSAGE);

        }
    }//GEN-LAST:event_formWindowClosing

    private void PortFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PortFieldKeyPressed
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if(Character.isLetterOrDigit(c)|| Character.isISOControl(c)){
            PortField.setEditable(true);
        }
        else PortField.setEditable(false);
    }//GEN-LAST:event_PortFieldKeyPressed

    private void serverbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serverbuttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_serverbuttonActionPerformed
    
    void printLog(String type, String msg){
        String t = "", d = "";
        LocalDateTime now = LocalDateTime.now();
        if (has_date)
            d = _day.format(now);
            if (!has_time)
                d += "-";
            else
                d += " ";
        
        if (has_time)
            t = _time.format(now) + "-";
        
        if (has_sound) _player.play_sound();
        this.LogText.append(d + t + "[" + type + "]: " + msg + "\r\n");
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerControl().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ClearButton;
    private javax.swing.JPanel EventTab;
    private javax.swing.JCheckBox HasDateCheckBox;
    private javax.swing.JCheckBox HasTimeCheckBox;
    private javax.swing.JTextArea LogText;
    private javax.swing.JLabel LogoLabel;
    private javax.swing.JPanel OptionTab;
    private javax.swing.JTextField PortField;
    private javax.swing.JTabbedPane TabsTree;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton serverbutton;
    private javax.swing.JCheckBox soundCheckBox;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
