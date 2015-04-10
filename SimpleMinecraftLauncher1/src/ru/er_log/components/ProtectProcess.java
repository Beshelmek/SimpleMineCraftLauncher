package ru.er_log.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;


/**
 *
 * @author GoodPC
 */

public class ProtectProcess {
                
    public static void CheckPid(){

            new Timer(5 * 1000, new ActionListener() {
                
                public void actionPerformed(ActionEvent e)
                { 
                    //что-то тут будет...
                }

            }).start();
    }

}
