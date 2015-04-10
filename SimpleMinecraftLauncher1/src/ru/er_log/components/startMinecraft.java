/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.er_log.components;

import java.io.File;
import javax.swing.JOptionPane;
import static ru.er_log.components.Frame.frame;
import static ru.er_log.components.SystemInfo.SystemInfo;
import static ru.er_log.components.SystemInfo.SystemUnix;

/**
 *
 * @author GoodPC
 */
public class startMinecraft {
    
    public static void startMinecraft(){
        File Applet = new File("C:\\API.cfg");
        File Applet2 = new File("C:\\api.cfg");
        
        if(SystemInfo()){
            Frame.report("Mac");
        }else{
            if(SystemUnix()){
                Frame.report("Unix");
            }else{
                Frame.report("Windows");
                //CheckJavaHomePach();
            }
        }
        
        if (Applet.exists()){
            JOptionPane.showMessageDialog(frame, "Ошибка безопасности Java \n Найден сторонний rt.jar.", "SimpleMinecraft.Ru | Launcher", JOptionPane.ERROR_MESSAGE); 
            Runtime.getRuntime().exit(1);  
        }
        else if (Applet2.exists()){
            JOptionPane.showMessageDialog(frame, "Ошибка безопасности Java \n Найден сторонний rt.jar.", "SimpleMinecraft.Ru | Launcher", JOptionPane.ERROR_MESSAGE); 
            Runtime.getRuntime().exit(1);
        }
    }
    public static void appletMinecraft(){
        File Applet3 = new File("C:\\API.cfg");
        File Applet4 = new File("C:\\api.cfg");
        File Applet5 = new File("C:\\dump\\");
        File Applet6 = new File("C:\\conf.mbv");
        File Applet7 = new File ("C:\\mbv.jar");

        if (Applet3.exists()){
            JOptionPane.showMessageDialog(frame, "Ошибка безопасности Java \n Найден сторонний rt.jar.", "SimpleMinecraft.Ru | Launcher", JOptionPane.ERROR_MESSAGE); 
            Runtime.getRuntime().exit(1);  
        }
        else if (Applet4.exists()){
            JOptionPane.showMessageDialog(frame, "Ошибка безопасности Java \n Найден сторонний rt.jar.", "SimpleMinecraft.Ru | Launcher", JOptionPane.ERROR_MESSAGE); 
            Runtime.getRuntime().exit(1);
        }
        else if (Applet5.exists()){
            JOptionPane.showMessageDialog(frame, "Ошибка безопасности.", "SimpleMinecraft.Ru | Launcher", JOptionPane.ERROR_MESSAGE); 
            Runtime.getRuntime().exit(1);
        }
        else if (Applet6.exists()){
            JOptionPane.showMessageDialog(frame, "Ошибка безопасности.", "SimpleMinecraft.Ru | Launcher", JOptionPane.ERROR_MESSAGE);
            Runtime.getRuntime().exit(1);  
        }
        else if (Applet7.exists()){
            JOptionPane.showMessageDialog(frame, "Ошибка безопасности.", "SimpleMinecraft.Ru | Launcher", JOptionPane.ERROR_MESSAGE);
            Runtime.getRuntime().exit(1);
        }
        
    }

}

