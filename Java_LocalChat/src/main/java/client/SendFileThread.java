/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
import java.io.*;
import java.net.Socket;
import java.util.*;
import javax.swing.JOptionPane;
/**
 *
 * @author admin
 */
class SendFileThread implements Runnable{
    private SendFileUI main = null;
    private Socket soc = null;
    private Thread runner;
    DataOutputStream output = null;
    DataInputStream input = null;
    String host, name, receiver = "unknown", file_dir = "";
    
    int port;
    public SendFileThread(SendFileUI _main, String path){
        this.main = _main;
        this.host = _main.getHost();
        this.port = _main.getPort();
        this.name = _main.getName();
        this.receiver = _main.getReceiver();
        this.file_dir = path;
    }
    public void connect() throws Exception{
        try{
            this.soc = new Socket(this.host, this.port);
            this.output = new DataOutputStream(this.soc.getOutputStream());
            this.input = new DataInputStream(this.soc.getInputStream());
            //Tell request server to send file
            this.output.writeUTF("REQUEST_SEND_FILE " + this.name + " " + this.receiver);
            start();
        }
        catch(Exception e){
            throw e;
        }
    }

    private void start() {
        if (this.runner == null){
            this.runner = new Thread(this);
            this.runner.start();
        }
    }
    private void stop(){
        try {
            this.soc.close();
        } catch (IOException e) {
            System.out.println("[error while closing socket]: "+e.getMessage());
        }
        this.main.dispose();
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()){
                String msg = input.readUTF();
                ArrayList<String> tokens = new ArrayList<>(Arrays.asList(msg.split("`")));
                
                //String data = this.main.input.readUTF();  // Đọc nội dung của dữ liệu được nhận từ Server
                //st = new StringTokenizer(data);
                String order = tokens.get(0);  //  Lấy chữ đầu tiên từ dữ liệu
                switch(order){
                    case "SEND_REQUEST_FEEDBACK":
                        String res = tokens.get(1);
                            if("UNACCEPTED".equals(res)){
                                //this._main.printLog("sendfile response", "Connection unaccepted - username used");
                                JOptionPane.showMessageDialog(this.main, "Can not send file right now!\n Please try again later", "Server refuse", JOptionPane.ERROR_MESSAGE);
                                this.main.printMsg("Can not send file right now", "Send file canceled");  
                                this.stop();
                                this.main.dispose();
                            }
                            else if("NO_RECEIVER".equals(res)){
                                JOptionPane.showMessageDialog(this.main, "Maybe receiver has disconnected!", "Receiver not found", JOptionPane.ERROR_MESSAGE);
                                this.main.printMsg("Receiver not found", "Send file canceled");      
                                this.stop();
                                this.main.dispose();
                            }
                            else if("ACCEPTED".equals(res)){
                                System.out.println("send file connection accepted");
                                this.main.printMsg("Server connected", "Prepare to send file");
                            }
                        break;
                    case "RECEIVE_FILE_RESPONSE":
                        String res2 = tokens.get(1);
                            if("ACCEPTED".equals(res2)){
                                try{
                                     //Thread.sleep(2000);
                                    new ClientSender(this.soc, this.file_dir, this.name, this.receiver, this.main);
                                }
                                catch(Exception e){
                                    System.out.println("Exception occured: " + e.getMessage());
                                    
                                    JOptionPane.showMessageDialog(this.main, "Exception occured:\n" + e.getMessage(), "Can not send file", JOptionPane.WARNING_MESSAGE);
                                    System.out.println("Can not send file right now");
                                    this.stop();
                                    this.main.dispose();
                                }
                            }
                            else if("UNACCEPTED".equals(res2)){
                                JOptionPane.showMessageDialog(this.main, "Receiver refused to get file", "File not sent", JOptionPane.INFORMATION_MESSAGE);
                                this.stop();
                                //this.main.dispose();
                            }

                    case "SEND_FILE_RESPONSE":
                        String __msg = tokens.get(1);
                        for(int i = 2; i < tokens.size(); ++i)
                            __msg = __msg + "`" + tokens.get(i);

                        JOptionPane.showMessageDialog(this.main, __msg, "Error on receiver side", JOptionPane.ERROR_MESSAGE);
                        this.stop();
                        //this.main.dispose();
                        break;
                    default:
                        this.main.printMsg("Unknown order received", msg);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
