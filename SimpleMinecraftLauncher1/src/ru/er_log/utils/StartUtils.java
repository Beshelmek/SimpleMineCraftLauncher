/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.er_log.utils;

import java.io.File;
import javax.swing.JOptionPane;
import static ru.er_log.components.Frame.frame;

/**
 *
 * @author GoodPC
 */
public class StartUtils {
    
    public static void CheckLauncherPach(){
        File launcher = new File(System.getenv("APPDATA") + "\\.simplemc\\Launcher.jar");
        
        if (!launcher.exists()){
            frame.report("Guard: Невозможно запустить процесс проверки!");
            Runtime.getRuntime().exit(1);
        } else {
            frame.report("Guard: Запущен процесс проверки!");
        }
    }
}
