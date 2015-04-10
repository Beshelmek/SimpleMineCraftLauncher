package ru.er_log.java.launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.er_log.utils.OperatingSystem;

public class JavaProcessLauncher {

    private final File directory;
    private final String jvmPath;
    private final List<Object> commands;

    public JavaProcessLauncher(String[] commands, File directory)
    {
        this.directory = directory;
        jvmPath = OperatingSystem.getCurrentPlatform().getJavaDir();
        this.commands = new ArrayList(commands.length);
        addCommands(commands);
    }

    public JavaProcess start() throws IOException
    {
        return start("", null);
    }
    
    public JavaProcess start(JavaProcessRunnable javaProcesRunnable) throws IOException
    {
        return start("", javaProcesRunnable);
    }
    
    public JavaProcess start(String prefix, JavaProcessRunnable javaProcesRunnable) throws IOException
    {
        List full = getFullCommands();
        return new JavaProcess(full, new ProcessBuilder(full).directory(this.directory).redirectErrorStream(true).start(), prefix, javaProcesRunnable);
    }

    public List<String> getFullCommands()
    {
        List result = new ArrayList(this.commands);
        result.add(0, jvmPath);
        return result;
    }

    public void addCommands(Object[] commands)
    {
        this.commands.addAll(Arrays.asList(commands));
    }
    
    public void addSplitCommands(String commands)
    {
        addCommands(commands.split(" "));
    }
}
