/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sound;

/**
 *
 * @author admin
 */
public class notification_sound {
    public static void play_sound(){
        java.awt.Toolkit.getDefaultToolkit().beep();
        try{
            Thread.sleep(500);
        }
        catch (InterruptedException e){
            System.out.println(e);
        }
    }
}
