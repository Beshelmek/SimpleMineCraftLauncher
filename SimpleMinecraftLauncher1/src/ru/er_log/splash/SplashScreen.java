package ru.er_log.splash;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import ru.er_log.Settings;
import ru.er_log.components.Frame;

public class SplashScreen {

    private static SplashFrame sframe;

    public static void start()
    {
        if (Settings.use_splash_screen) createSplash();
        beforeStart();
        createMainFrame();
    }
    
    private static final void createSplash()
    {
        Frame.report("Подготовка к запуску программы...");
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                sframe = new SplashFrame();
                sframe.show();
            }
        });
    }
    
    private static final void beforeStart()
    {
        status("Подготовка к запуску...");
        try {
            Frame.beforeStart(sframe);
        } catch (Exception ex) {
            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        status("Идет запуск...");
    }
    
    private static final void createMainFrame()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run() {try {
                    Frame.start(sframe);
                } catch (Exception ex) {
                    Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
 }
        });
    }
    
    private static final void status(final String status)
    {
        try
        {
            SwingUtilities.invokeAndWait(new Runnable()
            { public void run() { sframe.setStatus(status); } });
        } catch (InterruptedException | InvocationTargetException e) {}
    }
}