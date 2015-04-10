package ru.er_log.java.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.er_log.components.Frame;

public class ProcessMonitorThread extends Thread {

    private final JavaProcess process;
    private final String prefix;

    public ProcessMonitorThread(JavaProcess process, String prefix)
    {
        this.process = process;
        this.prefix = prefix;
    }

    public void run()
    {
        try
        {
            InputStreamReader reader = new InputStreamReader(this.process.getRawProcess().getInputStream(), "cp1251");
            BufferedReader buf = new BufferedReader(reader);
            String line = null;

            while (this.process.isRunning())
            {
                try
                {
                    while ((line = buf.readLine()) != null)
                    {
                        Frame.report(this.prefix + line);
                        this.process.getSysOutLines().add(line);
                    }
                } catch (IOException ex)
                {
                    Logger.getLogger(ProcessMonitorThread.class.getName()).log(Level.SEVERE, null, ex);
                } finally
                {
                    try
                    {
                        buf.close();
                    } catch (IOException ex)
                    {
                        Logger.getLogger(ProcessMonitorThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            JavaProcessRunnable onExit = this.process.getExitRunnable();
            
            if (onExit != null) onExit.onJavaProcessEnded(this.process);
        } catch (UnsupportedEncodingException e)
        {
            Frame.reportErr("Не удалось установить кодировку при выводе сообщений об отладке");
            e.printStackTrace();
        }
    }
}