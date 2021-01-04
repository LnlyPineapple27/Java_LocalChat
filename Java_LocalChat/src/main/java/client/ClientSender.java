/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.Socket;
import java.io.*;
/**
 *
 * @author admin
 */
public class ClientSender implements Runnable{
    private DataInputStream _input = null;
    private DataOutputStream _output = null;
    private MainMenu _main = null;
    private Socket soc = null;
    private Thread runner;
    ClientSender(Socket share_socket, MainMenu main){
        this._main = main;
        this.soc = share_socket;
    }
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    void start() {
        if (this.runner == null){
            this.runner = new Thread(this);
            this.runner.start();
        }
    }
}
