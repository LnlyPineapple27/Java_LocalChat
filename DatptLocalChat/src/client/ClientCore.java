/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Phan Tan Dat
 */
public class ClientCore implements Runnable{
    private String _name, _ip;
    private int _port;
    private Socket soc;
    private boolean is_active = false;
    private Thread runner = null;
    DataOutputStream output = null;
    DataInputStream input = null;
    //StringTokenizer tokens = null;
    private MainMenu _main = null;
    private ClientReceiver Filereceiver = null;
    private ClientSender FileSender = null;
    boolean getStatus(){
        return this.is_active;
    }
    ClientCore(MainMenu main, String user_name, String host_ip, int port) throws Exception{
        this._main = main;
        this._name = user_name;
        this._ip = host_ip;
        this._port = port;
        try {
            this.soc = new Socket(this._ip, this._port);
            
            System.out.println("SOC: " + soc);
            this.output = new DataOutputStream(this.soc.getOutputStream());
            this.output.writeUTF("REQUEST_JOIN " + this._name);
            this._main.setTitle(this._name);
            System.out.println("Connected to the chat server " + this._ip + " on port " + Integer.toString(this._port));
            this.input = new DataInputStream(this.soc.getInputStream());
            this.is_active = true;
            this.start();
 
        } catch (UnknownHostException ex) {
            throw new Exception("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            throw new Exception("I/O Error: " + ex.getMessage());
        }
        
    }

    void sendMessage(String receiver, String content) throws Exception{
        if (this.output != null){
            try {
                this.output.writeUTF("REQUEST_CHAT_TO " + this._name + " " + receiver + " " + content);
                this._main.printLog("Message to <" + receiver + ">", "'"+ content +"' was sent");
            } catch (Exception e) {
                throw e;
            }
        }
    }
    void sendMessageAll(String content) throws Exception{
        if (this.output != null){
            try {
                this.output.writeUTF("REQUEST_CHAT_ALL " + this._name + " " + content);
                
                this._main.printLog("Message to <ALL>", "'"+ content +"' was sent");
            } catch (Exception e) {
                throw e;
            }
        }
    }
    public void stop_now(boolean is_quit) throws Exception {
        try {
            this.terminate(is_quit);
            if (this.soc != null)
                this.soc.close();
        } 
        catch (Exception e) {
            throw e;
        }
    }
    public void terminate(boolean is_quit) throws IOException {
        if(this.output != null){
            if (is_quit) this.output.writeUTF("REQUEST_DISCONNECT " + this._name);
            this.output.close();
            this.output = null;
        }
        if(this.input != null){
            this.input.close();
            this.input = null;
        }
        this.is_active = false;
    }
    @Override
    public void run() {
        while (this.is_active){
            try {
                String msg = input.readUTF();
                ArrayList<String> tokens = new ArrayList<>(Arrays.asList(msg.split("`")));
                if (tokens.size()>1){
                    String order = tokens.get(0);
                    switch(order){
                        case "REQUEST_FEEDBACK":
                            String res = tokens.get(1);
                            if ("ACCEPTED".equals(res)){
                                this._main.printLog("Server response", "Connection accepted");

                            //System.out.println("feedback!!");
                            }
                            else if("UNACCEPTED".equals(res)){
                                this._main.printLog("Server response", "Connection unaccepted - username used");
                                JOptionPane.showMessageDialog(this._main, "Your username is used!\nPlease input another one :)", "Server refuse", JOptionPane.INFORMATION_MESSAGE);
                                this._main.closeConnection(false);
                            }
                            break;
                        case "NEW_MESSAGE":
                            String sender = tokens.get(1);
                            String content = tokens.get(2);
                            if (tokens.size() > 2){
                                for(int i = 3; i < tokens.size(); ++i){
                                    content = content + "`" + tokens.get(i);
                                }
                            }
                            this._main.printLog(sender + "'s message to YOU", content);
                            break;
                        case "NEW_MESSAGE_ALL":
                            String _sender = tokens.get(1);
                            String _content = tokens.get(2);
                            if (tokens.size() > 2){
                                for(int i = 3; i < tokens.size(); ++i){
                                    _content = _content + "`" + tokens.get(i);
                                }
                            }
                            this._main.printLog(_sender + "'s message to ALL", _content);
                            break;
                        case "USER_LIST":
                            Set<String> list = new HashSet<String>();
                            for(int i = 1; i < tokens.size(); ++i){
                                list.add(tokens.get(i));
                            }
                            this._main.updateOnlineList(list);
                            break;
                        case "REQUEST_SEND_FILE":
                            String Fsender = tokens.get(1);
                            int confirm = JOptionPane.showConfirmDialog(this._main,"'" + Fsender + "' want to send you a file!\nAccept?", "File message request", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION){
                                try {
                                    Socket _share = new Socket(this._ip, this._port);
                                    DataOutputStream Out = new DataOutputStream(_share.getOutputStream());
                                    Out.writeUTF("RECEIVING_OPENED "+ this._name);
                                    
                                    // Pick a place to save file
                                    String folder;
                                    JFileChooser chooser = new JFileChooser();
                                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                                    int open = chooser.showDialog(this._main, "Set location to save file");
                                    if(open == chooser.APPROVE_OPTION)
                                        folder = chooser.getSelectedFile().toString();
                                    else 
                                        folder = null;
                                    
                                    this.Filereceiver = new ClientReceiver(_share, this._main, folder);
                                    if (this.output != null){
                                         try {
                                            this.output.writeUTF("RECEIVE_FILE_RESPONSE ACCEPTED " + this._name + " " + Fsender);
                                            this._main.printLog("Accepting file from " + Fsender, " please wait for the download process to complete!");
                                        } catch (Exception e) {
                                            this._main.printLog("STH WENT WRONG", e.getMessage());
                                        }
                                    }
                                } catch (IOException e) {
                                    System.out.println("IOException: "+e.getMessage());
                                }
                            } 
                            else {
                                if (this.output != null){
                                    try {
                                        this.output.writeUTF("RECEIVE_FILE_RESPONSE UNACCEPTED " + this._name + " " + Fsender);
                                        this._main.printLog("Refused file from " + Fsender, "...");
                                    } catch (Exception e) {
                                        this._main.printLog("STH WENT WRONG", e.getMessage());
                                    }
                                       
                                }
                            }
                            break;
                        default:
                            this._main.printLog("Unknown order", msg);
                            break;
                    }
                } else{
                    if (tokens.get(0).equals("USER_LIST")){//empty user list received
                        this._main.updateOnlineList(null);
                    }
                    else
                        this._main.printLog("Unknown order", msg);
                }
            }
            catch(ArrayIndexOutOfBoundsException e){
                
                System.out.println(e.getMessage());
            }
            catch(IOException e){
                this._main.printLog("Connection interupted", "");
                this._main.printLog("Closing connection", "...");
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(this._main, "It seems thay you have been kicked off the server or the server have been shutdown :)", "Connection interupted", JOptionPane.INFORMATION_MESSAGE);
                this._main.closeConnection(false);
            }
        }
    }
    void start() {
        if (this.runner == null){
            this.runner = new Thread(this);
            this.runner.start();
        }
    }
    
}
