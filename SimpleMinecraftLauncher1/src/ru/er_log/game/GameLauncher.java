package ru.er_log.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import ru.er_log.Settings;
import ru.er_log.components.Frame;
import ru.er_log.java.eURLClassLoader;
import ru.er_log.utils.BaseUtils;
import ru.er_log.utils.GuardUtils;
import ru.er_log.utils.OperatingSystem;
import ru.er_log.utils.ThemeUtils;

public final class GameLauncher {
    
    public GameLauncher(String[] data) throws IOException
    {
        File gameDirectory = new File(BaseUtils.getClientDirectory());
        File binDirectory = new File(gameDirectory, "bin");
        File nativesDirectory = new File(gameDirectory, "bin/natives");
        File assetsDirectory = new File(gameDirectory, "assets");
        
        
        boolean forge = Boolean.valueOf(Frame.frame.set_offline.isSelected() ? ThemeUtils.offlineGameUtils.useForge(Frame.frame.serversList.getSelectedIndex()) : BaseUtils.getServerAbout()[4]);
        boolean liteloader = Boolean.valueOf(Frame.frame.set_offline.isSelected() ? ThemeUtils.offlineGameUtils.useLiteLoader(Frame.frame.serversList.getSelectedIndex()) : BaseUtils.getServerAbout()[5]);
        
        List<String> all_arguments = new ArrayList<>();
        List<String> properties = new ArrayList<>();
        List<String> tweak_args_list = new ArrayList<>();
        
        if (OperatingSystem.getCurrentPlatform().equals(OperatingSystem.OSX))
        {
            properties.add("-Xdock:icon=" + new File(assetsDirectory, "icons/minecraft.icns").getAbsolutePath());
            properties.add("-Xdock:name=Minecraft");
        }
        
        if (forge)
        {
            properties.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
            properties.add("-Dfml.ignorePatchDiscrepancies=true");
        }
        
        properties.add("-Djava.library.path=" + nativesDirectory.toString());
        properties.add("-Dorg.lwjgl.librarypath=" + nativesDirectory.toString());
        properties.add("-Dnet.java.games.input.librarypath=" + nativesDirectory.toString());
        
        int memory = BaseUtils.getPropertyInt("memory");
        if (memory == 0) memory = "32".equals(System.getProperty("sun.arch.data.model")) ? 512 : 1024;
        
        all_arguments.add("-Xmx" + memory + "M");
        all_arguments.add("-cp"); all_arguments.add(getLibraryList(binDirectory, forge, liteloader));
        all_arguments.add((forge || liteloader) ? Settings.lwrap_mine_class : Settings.mine_class);
        
        tweak_args_list.add("--username");       tweak_args_list.add(Frame.frame.set_offline.isSelected() ? Settings.off_user : data[23]);
        tweak_args_list.add("--session");        tweak_args_list.add(Frame.frame.set_offline.isSelected() ? Settings.off_sess + "<::>" + BaseUtils.getClientName() + "<::>" + GuardUtils.appPath() + "<::>null<::>" + GuardUtils.getHWID() + "<::>" + getLibraryList(binDirectory, forge, liteloader) + "<::>" + Settings.game_directory + "<::>" + Settings.par_directory : GuardUtils.md5(data[7]) + "<::>" + BaseUtils.getClientName() + "<::>" + GuardUtils.appPath() + "<::>" + data[20] + "<::>" + GuardUtils.getHWID() + "<::>" + getLibraryList(binDirectory, forge, liteloader) + "<::>" + Settings.game_directory + "<::>" + Settings.par_directory);
        tweak_args_list.add("--version");        tweak_args_list.add(BaseUtils.getClientVersion());
        tweak_args_list.add("--gameDir");        tweak_args_list.add(gameDirectory.toString());
        tweak_args_list.add("--assetsDir");      tweak_args_list.add(assetsDirectory.toString());
        
        if (Frame.frame.set_full.isSelected())
        {
            tweak_args_list.add("--fullscreen"); tweak_args_list.add("true");
        }
        
        if (Settings.use_auto_entrance && !Frame.frame.set_offline.isSelected())
        {
            tweak_args_list.add("--server");     tweak_args_list.add(BaseUtils.getServerAbout()[1]);
            tweak_args_list.add("--port");       tweak_args_list.add(BaseUtils.getServerAbout()[2]);
        }
        
        if (forge || liteloader)
        {
            tweak_args_list.add("--tweakClass");
        }
        
        if (liteloader)
        {
            tweak_args_list.add("com.mumfrey.liteloader.launch.LiteLoaderTweaker");
        }
        
        if (forge && liteloader)
        {
            // Начиная с версии 1.6.4, клиент использует "--tweakClass" вместо "--cascadedTweaks", в качестве второстепенного класса
            String arg = BaseUtils.versionCompare(BaseUtils.getClientVersion(), "1.6.4") == 2 ? "--cascadedTweaks" : "--tweakClass";
            tweak_args_list.add(arg);
        }
        
        if (forge)
        {
            tweak_args_list.add("cpw.mods.fml.common.launcher.FMLTweaker");
        }
        
        if (Settings.use_jar_check && !Frame.frame.set_offline.isSelected())
        {
            Frame.report("GUARD: Проверка клиента на наличие сторонних JAR файлов...");
            GuardUtils.checkClientsJars(BaseUtils.getClientDirectory(), data);
            Frame.report("GUARD: Проверка завершена");
        }
        GuardUtils.checkClient(Frame.authData, false);
        
        if (Settings.use_mod_check && Settings.use_mod_check_timer && !Frame.frame.set_offline.isSelected())
        {
            new Timer(Settings.time_for_mods_check * 1000, new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    GuardUtils.checkClient(Frame.authData, false);
                }
            }).start();
        }
        
        setSystemProperties(properties);
        all_arguments.addAll(tweak_args_list);
        
        Frame.report("-------------------------");
        Frame.report("Переход к запуску игры...");
        Frame.report("-------------------------");
        
        Frame.frame.setAlert("Игра запущена", 1, 391, 2);
        BaseUtils.sleep(1.5);
        Frame.frame.setVisible(false);
        
        try
        {
            URL[] urls = toURLs(getLibraryList(binDirectory, forge, liteloader), getCPSeparator());
            String main_class = (forge || liteloader) ? Settings.lwrap_mine_class : Settings.mine_class;
            
            eURLClassLoader loader = new eURLClassLoader(urls);
            Class cls = loader.loadClass(main_class);
            
            Method main = cls.getMethod("main", new Class[] { String[].class });
            main.invoke(null, new Object[] { all_arguments.toArray(new String[0]) });
            
        } catch (Exception e)
        {
            Frame.frame.setAlert("Ошибка в работе приложения", 3, 391, 2);
            Frame.frame.setVisible(true);
            
            Frame.reportErr("Ошибка при запуске или работе приложения!");
            e.printStackTrace();
        }
    }
    
    private void setSystemProperties(List<String> properties)
    {
        Frame.report("Установка системных параметров...");
        
        for (String propertie : properties)
        {
            String[] key_value = propertie.replaceFirst("-D", "").replaceFirst("-X", "").split("=", 2);
            try
            {
                System.setProperty(key_value[0], key_value[1]);
            } catch (Exception e)
            {
                Frame.reportErr("Не удалось установить параметр: " + propertie);
                e.printStackTrace();
            }
        }
        Frame.report("Все системные параметры установлены");
    }
    
    private URL[] toURLs(String line, String sep)
    {
        String[] strs = line.split(sep);
        URL[] urls = new URL[strs.length];
        
        try
        {
            for (int i = 0; i < urls.length; i++)
                urls[i] = new File(strs[i]).toURI().toURL();
            
            return urls;
        } catch (MalformedURLException e)
        {
            Frame.reportErr("Ошибка при преобразовании в URL");
            e.printStackTrace();
        }
        
        return null;
    }
    
    private String getLibraryList(File bin_folder, boolean using_forge, boolean using_liteloader)
    {
        String sep = getCPSeparator();
        List<String> lib = new ArrayList<>();
        
        lib.add(bin_folder.toString() + File.separator + "libraries.jar" + sep);
        if (using_forge) lib.add(bin_folder.toString() + File.separator + "forge.jar" + sep);
        if (using_liteloader) lib.add(bin_folder.toString() + File.separator + "liteloader.jar" + sep);
        lib.add(bin_folder.toString() + File.separator + "minecraft.jar" + sep);
        lib.add(new File(GuardUtils.appPath()).toString());
        
        String library = "";
        for (String str : lib)
        {
            library += str;
        }
        
        return library;
    }
    
    private String getCPSeparator()
    {
        return (BaseUtils.getPlatform() != 2) ? ":" : ";";
    }
}
