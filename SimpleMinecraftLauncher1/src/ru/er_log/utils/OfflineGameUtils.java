package ru.er_log.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import ru.er_log.Settings;

public class OfflineGameUtils {
    
    private final Map<Integer, String> num_pathSver = new HashMap<>();
    private static String[][] clients_list; // название - путь - версия
    
    public OfflineGameUtils()
    {
        get_offline_clients_names();
    }
    
    private void get_offline_clients_names()
    {
        String[][] not_finded = { {"Игровые клиенты не обнаружены"}, {"error"} };
        int client_num = 0;
        
        File gameDir = BaseUtils.getGameDirectory(Settings.game_directory);
        String[] objects = gameDir.list();
        for (String object : objects)
        {
            File obj = new File(gameDir + File.separator + object);
            if (obj.isDirectory() && check_for_client_completeness(obj))
            {
                String prop = BaseUtils.getPropertyString(object);
                if (prop != null)
                {
                    num_pathSver.put(client_num, prop.split("-::-")[0] + " :: " + obj.getAbsolutePath() + " :: " + prop.split("-::-")[1] + " :: " + prop.split("-::-")[2] + " :: " + prop.split("-::-")[3]);
                    client_num++;
                }
            }
        }
        
        if (!num_pathSver.isEmpty())
        {
            clients_list = new String[num_pathSver.size()][5];
            for (int i = 0; i < clients_list.length; i++)
            {
                clients_list[i][0] = "Client " + num_pathSver.get(i).split(" :: ")[2] + ": " + num_pathSver.get(i).split(" :: ")[0];
                clients_list[i][1] = num_pathSver.get(i).split(" :: ")[1]; // Abs. path to client
                clients_list[i][2] = num_pathSver.get(i).split(" :: ")[2]; // Client version
                clients_list[i][3] = num_pathSver.get(i).split(" :: ")[3]; // Forge (true or false)
                clients_list[i][4] = num_pathSver.get(i).split(" :: ")[4]; // LiteLoader (true or false)
            }
        } else
        {
            clients_list = not_finded;
        }
    }
    
    private boolean check_for_client_completeness(File dir)
    {
        String[] objects = dir.list();
        
        for (String object : objects)
        {
            File obj = new File(dir + File.separator + object);
            if (obj.isDirectory() && object.equals("bin"))
            {
                File minecraft_jar = new File(obj + File.separator + "minecraft.jar");
                File libraries_jar = new File(obj + File.separator + "libraries.jar");
                File natives = new File(obj + File.separator + "natives");
                if (minecraft_jar.exists() && libraries_jar.exists() && natives.exists() && natives.list().length != 0)
                {
                    return true; // 1.6.1+
                } else
                {
                    File jinput_jar = new File(obj + File.separator + "jinput.jar");
                    File lwjgl_jar = new File(obj + File.separator + "lwjgl.jar");
                    File lwjgl_util_jar = new File(obj + File.separator + "lwjgl_util.jar");
                    
                    if (minecraft_jar.exists() && jinput_jar.exists() && lwjgl_jar.exists() && lwjgl_util_jar.exists() && natives.exists() && natives.list().length != 0)
                    {
                        return true; // 1.5.2-
                    }
                }
            }
        }
        
        return false;
    }
    
    public String getClientName(int index)
    {
        return clients_list[index][0];
    }
    
    public String getClientPath(int index)
    {
        return clients_list[index][1];
    }
    
    public String getClientVersion(int index)
    {
        return clients_list[index][2];
    }
    
    public String useForge(int index)
    {
        return clients_list[index][3];
    }
    
    public String useLiteLoader(int index)
    {
        return clients_list[index][4];
    }
    
    public String[] getClientsList()
    {
        String[] list = new String[clients_list.length];
        for (int i = 0; i < list.length; i++)
            list[i] = clients_list[i][0];
        
        return list;
    }
}
