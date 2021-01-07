/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author admin
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class ServerCore implements Runnable{
    private ServerSocket core = null;
    private ServerControl control_ui = null;
    private boolean is_active;
    private int port;
    private Thread runner = null;
    //public Vector sharing_client_name = null;
    //public Vector sharing_client_socket = null;
    private TreeMap <String, Socket> user_list, sharing_client, receiving_client;
    /*
    public void add_sharing_client_name(String name){
        if(this.sharing_client_name != null)
            this.sharing_client_name.add(name);
    }
    public void add_sharing_client_socket(Socket s){
        if(this.sharing_client_socket != null)
            this.sharing_client_socket.add(s);
    }
    
    public Socket get_sharing_client_socket(String name){
        for(int i = 0; i < this.sharing_client_name.size(); ++i){
            if(this.sharing_client_name.elementAt(i).equals(name)){
                return (Socket) this.sharing_client_socket.elementAt(i);
            }
        }
        return null;
    }
    public void remove_sharing_client(String name){
        for(int i = 0; i < this.sharing_client_name.size(); ++i){
            if(this.sharing_client_name.elementAt(i).equals(name)){
                try {
                    Socket remove_soc = get_sharing_client_socket(name);
                    if(remove_soc != null)
                        remove_soc.close();
                    
                    System.out.println("Removing sharer " + name);
                    this.sharing_client_socket.removeElementAt(i);
                    this.sharing_client_name.removeElementAt(i);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Couldnt remove sharer " + name);
                }
                break;
            }
        }
    }*/
    Set<String> get_user_list(){
        if (this.user_list.isEmpty())
            return null;
        return this.user_list.keySet();
    }
    Socket get_client_socket(String name){
        return this.user_list.get(name);
    }
    boolean getStatus(){
        return this.is_active;
    }
    int getPort(){
        return this.port;
    }
    private void sendUserListAll(){
        Set<String> list = this.get_user_list();
        for(String i : list){
            this.sendUserList(list, i);
        }
    }
    private void sendUserList(Set<String> list, String iname){
        Socket isoc = this.user_list.get(iname);
        if (isoc == null) return;
        String list_msg = "";
        if (list == null || list.isEmpty()) return;
        else if(list.size() > 1){
            int count = 1;
            for(String i : list){
                if (!i.equals(iname)){
                    list_msg += i;
                    if (count < list.size())
                        list_msg += "`";
                    count++;
                }

            }
        }
        try{
            DataOutputStream r_output = new DataOutputStream(isoc.getOutputStream());
            r_output.writeUTF("USER_LIST`" + list_msg);
        }
        catch(Exception e){
            this.pass_msg("Could not send users list to user " + iname, e.getMessage());
        }
    }
    Socket get_sharing_client(String name){
        return this.sharing_client.get(name);
    }
    boolean append_sharing_client(String new_name, Socket new_soc){
        if (this.sharing_client.containsKey(new_name))/*|| new_soc == null*/
            return false; 
        sharing_client.put(new_name, new_soc);
        return true;
    }
    
    boolean delete_sharing_client(String name){
        if (this.sharing_client.containsKey(name)){
            //Send msg to user to notify the removal
            Socket soc = this.sharing_client.get(name);
            if (soc != null){
                try{
                    soc.close();
                }
                catch (IOException e){
                    this.pass_msg("ServerCloseException", e.getMessage());
                }
            }
            this.sharing_client.remove(name);
            return true;
        }
        return false;
    }
    
    Socket get_receiving_client(String name){
        return this.receiving_client.get(name);
    }
    boolean append_receiving_client(String new_name, Socket new_soc){
        if (this.receiving_client.containsKey(new_name))/*|| new_soc == null*/
            return false; 
        receiving_client.put(new_name, new_soc);
        return true;
    }
    
    boolean delete_receiving_client(String name){
        if (this.receiving_client.containsKey(name)){
            //Send msg to user to notify the removal
            Socket soc = this.receiving_client.get(name);
            if (soc != null){
                try{
                    soc.close();
                }
                catch (IOException e){
                    this.pass_msg("ServerCloseException", e.getMessage());
                }
            }
            this.receiving_client.remove(name);
            return true;
        }
        return false;
    }
    boolean appendUser(String new_name, Socket new_soc){
        if (this.user_list.containsKey(new_name))/*|| new_soc == null*/
            return false;       
        user_list.put(new_name, new_soc);
        this.pass_msg("NEW_USER_JOINED", new_name);
        this.control_ui.updateOnlineList(this.get_user_list());
        this.sendUserListAll();
        return true;
    }
    boolean deleteUser(String name){
        if (this.user_list.containsKey(name)){
            //Send msg to user to notify the removal
            Socket soc = this.user_list.get(name);
            if (soc != null){
                try{
                    soc.close();
                }
                catch (IOException e){
                    this.pass_msg("ServerCloseException", e.getMessage());
                }
            }
            this.user_list.remove(name);

            this.pass_msg("USER_REMOVED", name);
            this.control_ui.updateOnlineList(this.get_user_list());
            this.sendUserListAll();
            return true;
        }
        return false;
    }
    public void pass_msg(String type, String msg){
        this.control_ui.printLog(type, msg);
    }
    public ServerCore(ServerControl ui, int port) throws Exception {
        try{
            this.port = port;
            this.control_ui = ui;
            this.is_active = true;
            this.sharing_client = new TreeMap<String, Socket>();
            this.receiving_client = new TreeMap<String, Socket>();
            this.user_list = new TreeMap<String, Socket>();
            this.core = new ServerSocket(this.port);
            //this.sharing_client_name = new Vector();
            //this.sharing_client_socket = new Vector();
        }
        catch(Exception e){
            throw e;
        }
        //this.appendUser("Datpt", null);
    }
    
    public void stop() throws Exception {
        try {
            this.terminate();
            if (this.core != null)
                this.core.close();
            if(this.runner != null)
                this.runner.join();
            this.user_list.clear();
            this.control_ui.updateOnlineList(this.get_user_list());
        } 
        catch (Exception e) {
            throw e;
        }
    }
    public void terminate() {
        this.is_active = false;
    }

    @Override
    public void run() {
        while (this.is_active){
            try{
                Socket soc = this.core.accept();
                System.out.println("SOC: " + soc);
                ChildrenSocket new_user = new ChildrenSocket(this, soc);
                new_user.start();
            }
            catch (Exception e){
                this.pass_msg("ServerThreadException", e.getMessage());
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
