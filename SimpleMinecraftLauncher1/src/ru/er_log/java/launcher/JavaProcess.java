package ru.er_log.java.launcher;

import java.util.List;
import ru.er_log.Settings;

public class JavaProcess {

    private final List<String> commands;
    private final LimitedCapacityList<String> sysOutLines = new LimitedCapacityList(String.class, 5);
    private final Process process;
    private JavaProcessRunnable onExit;
    private ProcessMonitorThread monitor;

    public JavaProcess(List<String> commands, Process process, String prefix, JavaProcessRunnable javaProcesRunnable)
    {
        this.commands = commands;
        this.process = process;
        
        monitor = new ProcessMonitorThread(this, prefix);
        
        if (Settings.use_game_debug_mode)
            this.monitor.start();
        
        if (javaProcesRunnable != null)
            safeSetExitRunnable(javaProcesRunnable);
    }

    public Process getRawProcess()
    {
        return this.process;
    }

    public boolean isRunning()
    {
        try
        {
            this.process.exitValue();
        } catch (IllegalThreadStateException ex)
        {
            return true;
        }

        return false;
    }

    public LimitedCapacityList<String> getSysOutLines()
    {
        return this.sysOutLines;
    }

    public void setExitRunnable(JavaProcessRunnable runnable)
    {
        this.onExit = runnable;
    }

    public final void safeSetExitRunnable(JavaProcessRunnable runnable)
    {
        setExitRunnable(runnable);

        if ((!isRunning())
                && (runnable != null))
            runnable.onJavaProcessEnded(this);
    }

    public JavaProcessRunnable getExitRunnable()
    {
        return this.onExit;
    }
}
