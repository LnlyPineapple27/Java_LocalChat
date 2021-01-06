/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.Socket;
import java.io.*;
import javax.swing.JOptionPane;
/**
 *
 * @author admin
 */
public class ClientSender implements Runnable{
    private DataOutputStream output = null;
    private SendFileUI main = null;
    private Socket soc = null;
    private Thread runner;
    //private boolean is_active = false;
    private String dir = "", sender = "", receiver = "";
    private int BUFFER_SIZE = 100;
    ClientSender(Socket socket, String path, String _sender, String _receiver, SendFileUI _main) throws Exception{
        this.main = _main;
        this.soc = socket;
        this.dir = path;
        this.receiver = _receiver;
        this.sender = _sender;
        try{
            this.output = new DataOutputStream(this.soc.getOutputStream());
            this.start();
            this.main.setTitle("Sending file...");
        }
        catch(Exception e){
            throw e;
        }
    }
    @Override
    public void run() {
        try {
            //form.disableGUI(true);
            System.out.println("Sending File");
            output = new DataOutputStream(this.soc.getOutputStream());
            /** Write filename, recipient, username  **/
            File filename = new File(this.dir);
            /*File parentDir = filename.getParentFile();
            if(! parentDir.exists())
                parentDir.mkdirs();
            */
            int len = (int) filename.length();
            int filesize = (int)Math.ceil(len / BUFFER_SIZE);
            String clean_filename = filename.getName();
            output.writeUTF("SENDING_FILE "  + this.sender + " " + this.receiver + " " + clean_filename.replace(" ", "_") +" "+ filesize);
            //System.out.println("Từ: "+ sender);
            //System.out.println("Đến: "+ receiver);
            /** Create an stream **/
            OutputStream file_writer = this.soc.getOutputStream();
            // Read file
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));

            byte[] buffer = new byte[BUFFER_SIZE];
            int count, loaded = 0;
            while((count = bis.read(buffer)) > 0){
                loaded += count;
                int percent = (loaded / filesize);
               
                if((percent%10) == 0)
                    this.main.setTitle("Process: " + Integer.toString(percent));
                output.write(buffer, 0, count);
            }
            /* Cập nhật AttachmentForm GUI */
            this.main.setTitle("Completed");
                
            //form.updateAttachment(false); //  Cập nhật Attachment 
            JOptionPane.showMessageDialog(this.main, "File was sent", "Completed", JOptionPane.INFORMATION_MESSAGE);
            this.main.dispose();
            output.flush();
            output.close();
            System.out.println("File đã được gửi..!");
        } catch (IOException e) {
            System.out.println("[SendFile]: "+ e.getMessage());
        }
    }
    
    
    void start() {
        if (this.runner == null){
            this.runner = new Thread(this);
            this.runner.start();
        }
    }
}
