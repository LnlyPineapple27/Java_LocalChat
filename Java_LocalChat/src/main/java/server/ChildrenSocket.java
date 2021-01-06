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
    private String name = null, file_sender = null, file_receiver = null;
    private Thread runner;
    private int BUFFER_SIZE = 100;
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
                    case "REQUEST_SEND_FILE":
                        this.file_sender = tokens.nextToken();
                        String receiver1 = tokens.nextToken();
                        try{
                            DataOutputStream r_output = new DataOutputStream(this.soc.getOutputStream());
                            if (this.parent_core.append_sharing_client(this.file_sender, this.soc)){
                                System.out.println("New file_sender added: " + this.name);
                                Socket rr_soc = this.parent_core.get_client_socket(receiver1);
                                if (rr_soc != null){
                                    r_output.writeUTF("SEND_REQUEST_FEEDBACK`ACCEPTED");
                                   try {
                                        DataOutputStream rr_output = new DataOutputStream(rr_soc.getOutputStream());
                                        rr_output.writeUTF("REQUEST_SEND_FILE`" + this.file_sender);
                                        
                                    } catch (IOException e) {
                                        this.parent_core.pass_msg("IOException", "Could not pass message to user: " + receiver1);
                                    }
                                }
                                else 
                                    r_output.writeUTF("SEND_REQUEST_FEEDBACK`NO_RECEIVER");
                                
                            }
                            else{
                                r_output.writeUTF("SEND_REQUEST_FEEDBACK`UNACCEPTED");
                            }
                        }
                        catch(Exception e){
                            this.parent_core.pass_msg("Could not send feedback to file sender " + this.name, e.getMessage());
                        }
                        break;
                    case "RECEIVE_FILE_RESPONSE":
                        String res = tokens.nextToken(),
                                __receiver = tokens.nextToken(),
                                __sender = tokens.nextToken();
                        
                        Socket soc1 = this.parent_core.get_sharing_client(__sender);
                        
                            if (soc1 != null){
                                try {
                                        DataOutputStream refuse_output = new DataOutputStream(soc1.getOutputStream());
                                        if ("ACCEPTED".equals(res))     
                                            refuse_output.writeUTF("RECEIVE_FILE_RESPONSE`ACCEPTED");
                                        else if("UNACCEPTED".equals(res)) {
                                            this.parent_core.delete_sharing_client(__sender);
                                            refuse_output.writeUTF("RECEIVE_FILE_RESPONSE`UNACCEPTED");
                                        }
                                } catch (IOException e) {
                                    this.parent_core.pass_msg("IOException", "Could not pass message to sender: " + __sender);
                                }
                            }
                        
                        break;
                    case "RECEIVING_OPENED":
                        this.file_receiver = tokens.nextToken();
                        if (this.parent_core.append_receiving_client(this.name, this.soc)){
                            System.out.println("New file_receiver added: " + this.name);
                            this.parent_core.pass_msg(this.file_receiver, "is waiting for file to send");
                        }
                        break;
                    default:
                        this.parent_core.pass_msg("Unknown order received", message);
                        break;
                    case "SENDING_FILE":
                        //main.appendMessage("CMD_SENDFILE : Client đang gửi một file...");
                        /*
                         Format: CMD_SENDFILE [Filename] [Size] [Recipient] [Consignee]  from: Sender Format
                         Format: CMD_SENDFILE [Filename] [Size] [Consignee] to Receiver Format
                         */
                        
                        String sender3 = tokens.nextToken();
                        String receiver3 = tokens.nextToken();
                        String file_name3 = tokens.nextToken();
                        String filesize3 = tokens.nextToken();
                        //main.appendMessage("CMD_SENDFILE : Từ: " + consignee);
                        //main.appendMessage("CMD_SENDFILE : Đến: " + sendto);
                        this.parent_core.pass_msg("Sending file", "From " + sender3);
                        this.parent_core.pass_msg("Sending file", "To " + receiver3);
                        /**
                         * Nhận client Socket *
                         */
                        //main.appendMessage("CMD_SENDFILE : sẵn sàng cho các kết nối..");
                        Socket filesock = this.parent_core.get_receiving_client(receiver3); /* Consignee Socket  */
                        /*   Now Check if the consignee socket was exists.   */

                        if (filesock != null) { /* Exists   */

                            try {
                                DataOutputStream file_writer = new DataOutputStream(filesock.getOutputStream());
                                file_writer.writeUTF("SENDING_FILE`" + file_name3 + "`" + filesize3 + "`" + sender3);
                              
                                InputStream input3 = this.soc.getInputStream();
                                OutputStream sendFile = filesock.getOutputStream();
                                byte[] buffer = new byte[BUFFER_SIZE];
                                int cnt;
                                while ((cnt = input3.read(buffer)) > 0) {
                                    sendFile.write(buffer, 0, cnt);
                                }
                                sendFile.flush();
                                sendFile.close();
                                this.parent_core.delete_receiving_client(receiver3);
                                this.parent_core.delete_sharing_client(sender3);
                                this.parent_core.pass_msg("Sending file", "Completed");
                            } catch (IOException e) {
                                this.parent_core.pass_msg("Send file exception", e.getMessage());
                            }
                        } else { 
                            this.parent_core.delete_sharing_client(sender3);
                            this.parent_core.pass_msg("Send file exception", "Client '" + receiver3 + "' not found");
                            DataOutputStream dos = new DataOutputStream(this.soc.getOutputStream());
                            dos.writeUTF("SEND_FILE_ERROR " + "Client '" + receiver3 + "' not found");
                        }
                        break;
                }
            }
        }
        catch(Exception e){
            this.parent_core.pass_msg(this.name + "-ChildrenSocket", e.getMessage()+ " - Cause: " + e.getCause());
            if (this.name!=null){
                if (this.parent_core.deleteUser(this.name))
                    System.out.println("User-" + this.name + " was removed from this server!");
                else System.out.println("{Error]: User-" + this.name + " doesn't exist in server! Maybe sth went wrong :<");
            }
            else if(this.file_sender != null){
                if(this.parent_core.delete_sharing_client(this.file_sender))
                    System.out.println("File sender: " + this.file_sender + " removed");
                else System.out.println("File sender: " + this.file_sender + " couldnt be removed");
            }
            else if(this.file_receiver != null){
                 if(this.parent_core.delete_receiving_client(this.file_receiver))
                    System.out.println("File receiver: " + this.file_receiver + " removed");
                else System.out.println("File receiver: " + this.file_receiver + " couldnt be removed");
            }
            else System.out.println(e.getMessage());
            
        }
    }
    void start() {
        if (this.runner == null){
            this.runner = new Thread(this);
            this.runner.start();
        }
    }
   
}
