package ru.er_log;

import java.io.File;
import java.io.IOException;
import ru.er_log.java.launcher.JavaProcessLauncher;
import ru.er_log.splash.SplashScreen;
import ru.er_log.utils.BaseUtils;
import ru.er_log.utils.GuardUtils;

public class Starter {
    
    private static boolean program_is_already_started = false;
        
    public static void main(String[] args) throws Exception
    {

        try
        {
            program_is_already_started = true;
            
            System.out.println(GuardUtils.appPath());
            String apppath = GuardUtils.appPath();
            int memory = BaseUtils.getPropertyInt("memory");
            if (memory == 0) memory = "32".equals(System.getProperty("sun.arch.data.model")) ? 512 : 1024;
            
            JavaProcessLauncher programLaunchProcess = new JavaProcessLauncher(new String[0], new File(apppath).getParentFile());
            
            programLaunchProcess.addCommands(new String[]
            {
                "-Xmx" + memory + "m",
                "-Xms" + memory + "m",
                "-Dsun.java2d.noddraw=true",
                "-Dsun.java2d.d3d=false",
                "-Dsun.java2d.opengl=false",
                "-Dsun.java2d.pmoffscreen=false",
                "-classpath",
                apppath,
                "ru.er_log.Main"
            });
            
            programLaunchProcess.start("", null);
        } catch (IOException e)
        {
            e.printStackTrace();
            SplashScreen.start();
        }
    }
    
    public static boolean isStarted()
    {
        return program_is_already_started;
    }
}