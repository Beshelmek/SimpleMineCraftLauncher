package ru.er_log.components;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;
import static ru.er_log.Settings.testvoid;
//import static ru.er_log.Settings.use_warning_message;
import static ru.er_log.components.Launcher.checkLauncher;
import ru.er_log.utils.GuardUtils;
import static ru.er_log.utils.StartUtils.CheckLauncherPach;

/**
 *
 * @author GoodPC
 */
public class Guard {
    
    private static Component frame;
    
    public static int guard = 1;
    
    public static void StartGuard() throws IOException, Exception{

        if(testvoid == false){
            checkLauncher();
            CheckLauncherPach();
            CheckFirstHashFile();
            CheckSecondHashFile();
            CheckStarterJava();
        }else{
            return;
        }
    }
    
    //проверка первого hash файла
    public static void CheckFirstHashFile(){

        File hashlog = new File(System.getProperty("java.home") + "\\bin\\hash.log");
        
        if (hashlog.exists()){
           hashlog.delete();
           Frame.report("Guard: Проверка #1 выполнена успешно!");
           guard = 1;
        }else{
            Frame.report("Guard: Проверка завершена со сбоем! Код ошибки: #1");
            
            guard = 2;
            Runtime.getRuntime().exit(1);
        }
    }
    
    
    //Проверка второго hash файла
    public static void CheckSecondHashFile(){
   
        File javalog = new File(System.getProperty("java.home") + "\\bin\\java.log");
        
        if (javalog.exists()){
           javalog.delete();
           Frame.report("Guard: Проверка #2 выполнена успешно!");
           guard = 1;
        }else{
            Frame.report("Guard: Проверка завершена со сбоем! Код ошибки: #2");
            
            guard = 3;
            Runtime.getRuntime().exit(1);
            
        }
    }
     
    //Проверка md5 rt.jar JRE
    public static void CheckStarterJava() throws Exception{
        String hashLib = GuardUtils.md5_file(System.getProperty("java.home") + "\\lib\\rt.jar");
        
        if (!hashLib.equalsIgnoreCase("DDB324D9F2BD20AA09028126FF781ACB") && !hashLib.equalsIgnoreCase("1A5C566856A0577D472E6AD68F509CB1")){
            Frame.report("Guard: Проверка завершена со сбоем! Код ошибки: #3");
            
            guard = 4;
            Runtime.getRuntime().exit(1);
            
        }else{
            Frame.report("Guard: Проверка #3 выполнена успешно!");
            guard = 1;
        }
    }
    
    /*
    public static void SendAdminMessage(){

        if(getUrlData("http://simpleminecraft.ru/newlauncher/starter/message.php?get=status").equalsIgnoreCase("true")){
            
            String m = getUrlData("http://simpleminecraft.ru/newlauncher/starter/message.php?get=message");
            String t = getUrlData("http://simpleminecraft.ru/newlauncher/starter/message.php?get=title");
            
            JOptionPane.showMessageDialog(frame, m, t, JOptionPane.WARNING_MESSAGE);
            
        }else{
            return;
        }
        
    }
    
    public static String getUrlData(String urlToRead){
        
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        
        try{
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return result;
    }*/

}
