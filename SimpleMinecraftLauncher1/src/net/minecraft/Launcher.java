package net.minecraft;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;
import ru.er_log.Settings;
import ru.er_log.components.Frame;
import static ru.er_log.components.startMinecraft.appletMinecraft;
import static ru.er_log.components.startMinecraft.startMinecraft;
import ru.er_log.java.eURLClassLoader;
import ru.er_log.utils.BaseUtils;
import ru.er_log.utils.GuardUtils;
import ru.er_log.Settings.*;
import static ru.er_log.utils.GuardUtils.checkProcesses;

public class Launcher extends Applet implements AppletStub {

    private static final long serialVersionUID = 1L;
    private Applet mcApplet = null;
    public Map<String, String> customParameters = new HashMap<String, String>();
    private int context = 0;
    private boolean active = false;
    private final URL[] urls;
    private final String bin;

    public Launcher(String bin, URL[] urls)
    {
        this.bin = bin;
        this.urls = urls;
    }

    public void init()
    {
        if (mcApplet != null)
        {
            mcApplet.init();
            return;
        }
        init(0);
    }

    public void init(int i)
    {
        if (Settings.use_mods_delete && !Settings.use_mod_check)
        {
            checkProcesses(); 
            BaseUtils.delete(new File(BaseUtils.getClientDirectory() + File.separator + "mods"));
            startMinecraft();
        }

        eURLClassLoader cl = new eURLClassLoader(urls);
        System.setProperty("org.lwjgl.librarypath", bin + "natives");
        System.setProperty("net.java.games.input.librarypath", bin + "natives");
        try
        {
            if (Settings.path_client)
                BaseUtils.patchClient(cl);
            Class<?> Mine = cl.loadClass("net.minecraft.client.MinecraftApplet");
            Applet applet = (Applet) Mine.newInstance();
            mcApplet = applet;
            applet.setStub(this);
            applet.setSize(getWidth(), getHeight());
            setLayout(new BorderLayout());
            add(applet, "Center");
            applet.init();
            active = true;
            validate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getParameter(String name)
    {
        String custom = (String) customParameters.get(name);
        if (custom != null)
            return custom;
        try
        {
            return super.getParameter(name);
        } catch (Exception e)
        {
            customParameters.put(name, null);
        }
        return null;
    }
                    
    public void start()
    {
        if (mcApplet != null)
        {
            Frame.report("-------------------------------");
            Frame.report("Перенаправление на Minecraft...");
            Frame.report("-------------------------------");
            
            startMinecraft();
            appletMinecraft();
            mcApplet.start();
            return;
        }
    }
    
    public boolean isActive()
    {
        if (context == 0)
        {
            context = -1;
            try
            {
                if (getAppletContext() != null)
                    context = 1;
            } catch (Exception e)
            {
            }
        }
        if (context == -1)
            return active;
        return super.isActive();
    }

    public URL getDocumentBase()
    {
        try
        {
            return new URL("http://www.minecraft.net/game/");
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void stop()
    {
        if (mcApplet != null)
        {
            active = false;
            mcApplet.stop();
            return;
        }
    }

    public void destroy()
    {
        if (mcApplet != null)
        {
            mcApplet.destroy();
            return;
        }
    }

    public void appletResize(int w, int h) {}

    public void replace(Applet applet)
    {
        mcApplet = applet;
        applet.setStub(this);
        applet.setSize(getWidth(), getHeight());

        setLayout(new BorderLayout());
        add(applet, "Center");

        startMinecraft();
        applet.init();
        active = true;
        applet.start();
        validate();
        appletMinecraft();
    }
}