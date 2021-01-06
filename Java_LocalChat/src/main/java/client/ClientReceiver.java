/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.Socket;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitorInputStream;
/**
 *
 * @author admin
 */
public class ClientReceiver implements Runnable{
    private DataInputStream _input = null;
    private DataOutputStream _output = null;
    private MainMenu _main = null;
    private Socket soc = null;
    private Thread runner;
    private boolean is_active = false;
    private String sender_name = null;
    ClientReceiver(Socket share_socket, MainMenu main){
        this._main = main;
        this.soc = share_socket;
        this.is_active = true;
        try{
            this._input = new DataInputStream(this.soc.getInputStream());
            this._output = new DataOutputStream(this.soc.getOutputStream());
        }
        catch(Exception e){
            _main.printLog("Receiving file error", e.getMessage());
        }
    }
    void start() {
        if (this.runner == null){
            this.runner = new Thread(this);
            this.runner.start();
        }
    }
    void stop(){
        this.is_active = false;
    }
    @Override
    public void run() {
         while (this.is_active){
            try {
                String msg = this._input.readUTF();
                ArrayList<String> tokens = new ArrayList<>(Arrays.asList(msg.split("`")));
                if(tokens.size() >= 4){
                    String order = tokens.get(0);
                    if (order.equals("SENDING_FILE")){
                        String file_name = tokens.get(1);
                        try{
                            int size = Integer.parseInt(tokens.get(2));
                        }
                        catch(NumberFormatException e1){
                            System.out.println("Unexpected signal received: " + msg);
                            break;
                        }
                        this.sender_name = tokens.get(3);
                        for(int i = 4; i <tokens.size(); ++i){
                            this.sender_name = this.sender_name + "`" + tokens.get(i);
                        }

                        String dir = this._main.default_download_folder + this.sender_name + "/" + file_name;
                        this._main.printLog("Downloading","Saving file to " + dir);

                        InputStream in = this.soc.getInputStream();
                        FileOutputStream file = new FileOutputStream(dir);

                        ProgressMonitorInputStream notification = new ProgressMonitorInputStream(this._main, "Your file is being downloaded", in);
                        BufferedInputStream buf = new BufferedInputStream(notification);
                        byte[] b = new byte[this._main.MAXSIZE];
                        int count, loaded = 0;
                        while(true){
                            count = buf.read(b);
                            if (count == -1) break;
                            file.write(b, 0, count);
                        }
                        file.flush();
                        file.close();
                        this.stop();
                        JOptionPane.showMessageDialog(this._main, "File downloaded and saved at\n" + dir );
                        System.out.println("Download completed!");
                       
                    }
                    
                }
                else System.out.println("Unexpected signal received: " + msg);
            }
            catch(Exception e){
                if(this.sender_name != null){
                    try{
                        DataOutputStream eDos = new DataOutputStream(this.soc.getOutputStream());
                        eDos.writeUTF("SEND_FILE_RESPONSE "+ this.sender_name + " Lost connection");
                        System.out.println(e.getMessage());
                        JOptionPane.showMessageDialog(this._main, e.getMessage(), "Lost connection to file sender!\nCanceling...", JOptionPane.ERROR_MESSAGE);
                        this.stop();
                        this.soc.close();
                    }
                   catch(Exception e2){
                        System.out.println(e2.getMessage());
                   }
                }
            }
        }
    }
    
}
