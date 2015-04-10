/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.er_log.components;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;
import ru.er_log.utils.GuardUtils;

/**
 *
 * @author Goodvise & Beshelmek chiteri sasay!
 */
public class Launcher {
    private static String url;
    public static void checkLauncher() throws IOException{
        File Launcher = new File(GuardUtils.appPath().toLowerCase());
        
        if (Launcher.exists()){
            String s = GuardUtils.md5_file(Launcher.getAbsolutePath());
            md5(s);
            
        } else if(!Launcher.exists()){
            JOptionPane.showMessageDialog(null, "Пожалуйста обновите лаунчер до новой версии!", "SimpleMinecraft.Ru | Updater", JOptionPane.ERROR_MESSAGE, null);
            Runtime.getRuntime().exit(1);
        }
        
            
    }
    public static void md5(String s) throws MalformedURLException, IOException{
        URLConnection conn = new URL("http://simpleminecraft.ru/newauth/scripts/gb_checklauncher.php?md5=" + s).openConnection();
        
            conn.setDoOutput(true);

        OutputStreamWriter out =
            new OutputStreamWriter(conn.getOutputStream(), "ASCII");
                out.write(s.toString());
                out.write("\r\n");
                out.flush();
                out.close();

        String html = readStreamToString(conn.getInputStream(), "UTF-8");

        if (!html.equalsIgnoreCase("0")){
            JOptionPane.showMessageDialog(null, "Пожалуйста обновите лаунчер до новой версии!", "SimpleMinecraft.Ru | Updater", JOptionPane.ERROR_MESSAGE, null);
            Runtime.getRuntime().exit(1);    
        }
    }
    
    public static String readStreamToString(InputStream in, String encoding)
        throws IOException {
            StringBuffer b = new StringBuffer();
            InputStreamReader r = new InputStreamReader(in, encoding);
            int c;
            while ((c = r.read()) != -1) {
                b.append((char)c);
    }
    return b.toString();
}  
            
}
