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
    //private List<String> name_list;
    //private List<ChildrenSocket> soc_list;
    private TreeMap <String, Socket> user_list;
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
            this.user_list = new TreeMap<String, Socket>();
            this.core = new ServerSocket(this.port);
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
