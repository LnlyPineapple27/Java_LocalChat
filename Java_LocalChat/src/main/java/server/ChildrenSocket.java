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

public class ChildrenSocket implements Runnable {
    private Socket soc;
    private ServerCore parent_core;
    StringTokenizer tokens;
    DataInputStream fin;
    private String name;
    private Thread runner;
    String getName(){
        return this.name;
    }
    public ChildrenSocket(ServerCore parent, Socket socket) throws Exception{
        this.soc = socket;
        this.parent_core = parent;
        
        try{
            this.fin = new DataInputStream(this.soc.getInputStream());
        }
        catch(Exception e){
            throw e;
        }
        
        //InputStream input = this.soc.getInputStream();
        //BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        

        //sendUserList();

        //String userName = reader.readLine().trim();
        //if (!parent_core.appendUser(userName)){
            // use_name already exist
            
       // }
    }
    @Override
    public void run() {
        try{
            while(true){
                String message = fin.readUTF();
                tokens = new StringTokenizer(message);
                String request = tokens.nextToken();
                switch(request){
                    case "REQUEST_JOIN":
                        String _name = "";
                        while(tokens.hasMoreTokens()){
                            _name += tokens.nextToken();
                            _name += " ";
                        }
                        this.name = _name.trim();
                        try{
                            DataOutputStream r_output = new DataOutputStream(this.soc.getOutputStream());
                            if (this.parent_core.appendUser(this.name, this.soc)){
                                System.out.println("New user added: " + this.name);
                                r_output.writeUTF("REQUEST_FEEDBACK`ACCEPTED");
                            }
                            else{
                                r_output.writeUTF("REQUEST_FEEDBACK`UNACCEPTED");
                            }
                        }
                        catch(Exception e){
                            this.parent_core.pass_msg("Could not send feedback to user " + this.name, e.getMessage());
                        }
                        break;
                    case "REQUEST_DISCONNECT":
                        String delete_name = "";
                        while(tokens.hasMoreTokens()){
                            delete_name += tokens.nextToken();
                            delete_name += " ";
                        }
                        delete_name = delete_name.trim();
                        if (this.parent_core.deleteUser(delete_name))
                            System.out.println("User-" + delete_name + " was removed from this server!");
                        else System.out.println("{Error]: User-" + delete_name + " doesn't exist in server! Maybe sth went wrong :<");
                        break;
                        
                    case "REQUEST_CHAT_TO":
                        String sender = tokens.nextToken(), 
                                receiver = tokens.nextToken(),
                                content = "";
                        while(tokens.hasMoreTokens()){
                            content += tokens.nextToken();
                            content += " ";
                        }
                        Socket r_soc = this.parent_core.get_client_socket(receiver);
                        this.parent_core.pass_msg("NEW_MESSAGE", "From " + sender + " to " + receiver + ": " + content);
                        try {
                            DataOutputStream r_output = new DataOutputStream(r_soc.getOutputStream());
                            r_output.writeUTF("NEW_MESSAGE`" + sender + "`" + content);
                         
                            
                        } catch (IOException e) {
                            this.parent_core.pass_msg("IOException", "Could not pass message to user: " + receiver);
                        }
                        break;
                    case "REQUEST_CHAT_ALL":
                        String _sender = tokens.nextToken(), 
                                _content = "";
                        while(tokens.hasMoreTokens()){
                            _content += tokens.nextToken();
                            _content += " ";
                        }
                        
                        this.parent_core.pass_msg("NEW_MESSAGE_ALL", "From " + _sender + " to all: " + _content);
                        Set<String> list = this.parent_core.get_user_list();
                        if (list != null){
                            for(String _receiver : list){
                                Socket _r_soc = this.parent_core.get_client_socket(_receiver);
                                if (!_receiver.equals(_sender)){
                                    try {
                                        DataOutputStream r_output = new DataOutputStream(_r_soc.getOutputStream());
                                        r_output.writeUTF("NEW_MESSAGE_ALL`" + _sender + "`" + _content);
                                        
                                    } catch (IOException e) {
                                        this.parent_core.pass_msg("IOException", "Could not pass message to user: " + _receiver);
                                    }
                                }
                            }
                        }
                        break;
                    default:
                        this.parent_core.pass_msg("Unknown order received", message);
                        break;
                }
            }
        }
        catch(Exception e){
            this.parent_core.pass_msg(this.name + "-ChildrenSocket", e.getMessage()+ " - Cause: " + e.getCause());
            if (this.parent_core.deleteUser(this.name))
                System.out.println("User-" + this.name + " was removed from this server!");
            else System.out.println("{Error]: User-" + this.name + " doesn't exist in server! Maybe sth went wrong :<");
                        
        }
    }
    void start() {
        if (this.runner == null){
            this.runner = new Thread(this);
            this.runner.start();
        }
    }
   
}
