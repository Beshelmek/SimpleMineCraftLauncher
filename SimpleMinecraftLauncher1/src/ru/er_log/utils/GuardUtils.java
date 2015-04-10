package ru.er_log.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.List;
import ru.er_log.Settings;
import ru.er_log.Starter;
import ru.er_log.components.Frame;

public class GuardUtils {
    
    protected static List<String> find_files = new ArrayList<>();
    protected static boolean all_clean = true;
    public static String md5(String s)
    {
        String hash = null;
        try
        {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            hash = new BigInteger(1, m.digest()).toString(16);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return hash;
    }
    
    public static String sha1(String input)
    {
        String hash = null;
        try
        {
            MessageDigest m = MessageDigest.getInstance("SHA1");
            byte[] result = m.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < result.length; i++)
            {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            hash = sb.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return hash;
    }
    
    public static String md5_file(String file_path)
    {
        FileInputStream fis = null;
        DigestInputStream dis = null;
        BufferedInputStream bis = null;
        Formatter formatter = null;
        try
        {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file_path);
            bis = new BufferedInputStream(fis);
            dis = new DigestInputStream(bis, messagedigest);
            while (dis.read() != -1);
            byte abyte0[] = messagedigest.digest();
            formatter = new Formatter();
            byte abyte1[] = abyte0;
            int i = abyte1.length;
            for (int j = 0; j < i; j++)
            {
                byte byte0 = abyte1[j];
                formatter.format("%02x", new Object[] { Byte.valueOf(byte0) });
            }
            return formatter.toString();
        } catch (NoSuchAlgorithmException | IOException e)
        {
            return "";
        } finally
        {
            try { fis.close(); } catch (Exception e) {}
            try { dis.close(); } catch (Exception e) {}
            try { bis.close(); } catch (Exception e) {}
            try { formatter.close(); } catch (Exception e) {}
        }
    }
        
    public static boolean checkProcesses()
    {   
        try
        {
            int platform = BaseUtils.getPlatform();
            if (platform == 2){
          
            String line; Process p;
            List<String> processes = new ArrayList<>();
            
            if (platform == 2) p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
            else p = Runtime.getRuntime().exec("ps -e");
            
            try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream())))
            {
                boolean show_all_processes = true;
                if (show_all_processes) Frame.report("Вывод всех запущенных системой процессов:");
                
                while ((line = input.readLine()) != null)
                {
                    processes.add(line);
                    if (show_all_processes) System.out.println(line);
                }
                
                if (show_all_processes) Frame.report("Вывод всех запущенных системой процессов завершен");
                
            }
            
            
            for (String process : processes)
            {
                for (String blocked_process : "cheatengine<:i:>cheat<:i:>wireshark<:i:>hack<:i:>po100b<:i:>y4ehuk<:i:>creeper".split("<:i:>"))
                {
                    if (!blocked_process.isEmpty() && process.toLowerCase().contains(blocked_process.toLowerCase())){
                        System.exit(1);
                    }
                }
            }
        }    
        } catch (IOException e)
        {
            Frame.reportErr("Не удалось получить список процессов системы: " + e.getCause());
        }
        
        return false;
    }
    
    public static boolean use_process_check_timer = true;
    public static int time_for_process_check = 10;
    public static boolean use_process_check = true;
        
    public static String appPath()
    {
        try { return URLDecoder.decode(Starter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "UTF-8"); }
        catch (UnsupportedEncodingException | URISyntaxException e) { e.printStackTrace(); return null; }
    }
    
    
    public static void checkClient(String[] data, boolean firstCheck)
    {
        if (!Frame.frame.set_offline.isSelected())
        {
            Frame.report("GUARD: Проверка файлов игры...");
            
            String binfolder = BaseUtils.getClientDirectory() + File.separator + "bin" + File.separator;
            
            if (!data[1].equals(md5_file(binfolder + "minecraft.jar")))
            {
                all_clean = false;
                find_files.add(binfolder + "minecraft.jar");
            }
            
            checkMods(BaseUtils.getClientDirectory() + File.separator + "mods", data);
            
            if (all_clean)
            {
                Frame.report("GUARD: Проверка завершена успешно");
            } else
            {
                if (firstCheck) BaseUtils.decisionToSendReport(false);
                if (!firstCheck && !Settings.stop_dirty_drogram)
                {
                    Frame.report("GUARD: Обнаружены посторонние объекты");
                } else if (Settings.stop_dirty_drogram)
                {
                    Frame.report("GUARD: Обнаружены посторонние объекты, игровой процесс будет прерван");
                    System.exit(0);
                }
            }
        }
    }
    
    public static void checkOldClient(String[] data, boolean firstCheck)
    {
        if (!Frame.frame.set_offline.isSelected())
        {
            Frame.report("GUARD: Проверка файлов игры...");
            
            String binfolder = BaseUtils.getClientDirectory() + File.separator + "bin" + File.separator;
            
            if (!data[1].equals(md5_file(binfolder + "minecraft.jar"))) all_clean = false;
            if (!data[2].equals(md5_file(binfolder + "lwjgl.jar"))) all_clean = false;
            if (!data[3].equals(md5_file(binfolder + "lwjgl_util.jar"))) all_clean = false;
            if (!data[4].equals(md5_file(binfolder + "jinput.jar"))) all_clean = false;
            
            checkMods(BaseUtils.getClientDirectory() + File.separator + "mods", data);
            checkMods(BaseUtils.getClientDirectory() + File.separator + "coremods", data);
            
            if (all_clean)
            {
                Frame.report("GUARD: Проверка завершена успешно");
                return;
            } else if (firstCheck)
            {
                BaseUtils.decisionToSendReport(false);
            }
            
            if (!all_clean && Settings.stop_dirty_drogram)
            {
                Frame.report("GUARD: Обнаружены посторонние объекты, игровой процесс будет прерван");
                System.exit(0);
            } else if (!all_clean)
            {
                Frame.report("GUARD: Обнаружены посторонние объекты");
            }
        }
    }
    
    public static void checkMods(String dir, String[] auth_data)
    {
        Frame.report("GUARD: Проверка папки \"" + dir.substring(dir.lastIndexOf(File.separator) + 1) + "\"...");
        
        File file = new File(dir); if (!file.exists()) file.mkdirs();
        String[] content = file.list();
        
        String mods_data = dir.substring(dir.lastIndexOf(File.separator) + 1).equals("mods") ? auth_data[9] : auth_data[13];
        String[] mod_hash = mods_data.split("<:f:>");
        String[] names_mods = new String[mod_hash.length];
        String[] hashes_mods = new String[mod_hash.length];
        
        if (!mods_data.equals("nomods") && !mods_data.equals("nocoremods"))
        for (int i = 0; i < mod_hash.length; i++)
        {
            names_mods[i] = mod_hash[i].split("<:h:>")[0];
            hashes_mods[i] = mod_hash[i].split("<:h:>")[1];
        }
        
        for (String object : content)
        {
            File obj = new File(dir + File.separator + object);
            
            if (mods_data.equals("nomods") || mods_data.equals("nocoremods"))
            {
                BaseUtils.delete(obj);
                Frame.report("GUARD: Удален сторонний объект: " + dir + File.separator + object);
                find_files.add(dir + File.separator + object);
                all_clean = false;
            }
            
            if (obj.isFile())
            {
                String end_titles = "";
                if (object.contains(".")) end_titles = object.substring(object.lastIndexOf("."));
                
                if (end_titles.equalsIgnoreCase(".jar") || end_titles.equalsIgnoreCase(".zip") || end_titles.equalsIgnoreCase(".litemod") || end_titles.equalsIgnoreCase(".class"))
                {
                    int found_num = BaseUtils.findString(names_mods, object);
                    if (found_num == -1)
                    {
                        BaseUtils.delete(obj);
                        String str = ""; if (!obj.exists()) str = "и удалена ";
                        Frame.report("GUARD: Обнаружена " + str + "сторонняя модификация: " + dir + File.separator + object);
                        find_files.add(dir + File.separator + object);
                        all_clean = false;
                    } else
                    {
                        if (!hashes_mods[found_num].equalsIgnoreCase(md5_file(dir + File.separator + object)))
                        {
                            Frame.report("GUARD: Обнаружено несовпадение в хеш-суммах модификаций: " + dir + File.separator + object);
                            find_files.add(dir + File.separator + object);
                            all_clean = false;
                        }
                    }
                }
            } else if (obj.isDirectory())
            {
                checkDir(dir + File.separator + object, mods_data, false);
            }
        }
    }
    
    /**
     * Дополнительная проверка поддиректорий папки "mods" и "coremods".
     * Пропускает JAR файлы, так как некоторые моды могут загружать сюда свои библиотеки.
     * Проверяет файлы: ".zip", ".litemod", ".class".
     * @param dir // директория для проверки ("mods/%dir%/" или "coremods/%dir%/")
     * @param mods_data // мод_1<:h:>хеш-сумма_1<:f:>мод_2<:h:>хеш-сумма_2
     * @param preupdate_check // проверка директорий перед обновлением, удаление старых файлов
     */
    public static void checkDir(String dir, String mods_data, boolean preupdate_check)
    {
        File file = new File(dir); if (!file.exists()) file.mkdirs();
        String[] content = file.list();
        
        for (String object : content)
        {
            File obj = new File(dir + File.separator + object);
            String end_titles = "";
            if (object.contains(".")) end_titles = object.substring(object.lastIndexOf("."));
            
            if (obj.isFile() && (end_titles.equalsIgnoreCase(".jar") |end_titles.equalsIgnoreCase(".zip") | end_titles.equalsIgnoreCase(".litemod") | end_titles.equalsIgnoreCase(".class")))
            {
                if (!checkFilesToExceptions(dir + File.separator + object, mods_data))
                {
                    BaseUtils.delete(obj);
                    String mes = "";
                    if (!obj.exists()) mes = "и удален ";
                    Frame.report("GUARD: Обнаружен " + mes + "сторонний файл: " + dir + File.separator + object);
                    if (!preupdate_check)
                    {
                        find_files.add(dir + File.separator + object);
                        all_clean = false;
                    }
                }
            } else if (obj.isDirectory())
            {
                checkDir(dir + File.separator + object, mods_data, preupdate_check);
            }
        }
    }
    
    public static void checkDir(String dir, String mods_data)
    {
        checkDir(dir, mods_data, false);
    }
    
    /**
     * Проверяет директорию на наличие пустых папок во всех подпапках и удаляет их.
     * @param dir // директория для проверки
     * @param parent_contains_files // содержит ли родительская директория файлы
     * @param first_launch // значение по умолчанию устанавилвать как "true"
     */
    public static void removeEmptyFolders(String dir, List<Boolean> parent_contains_files, boolean first_launch)
    {
        File file = new File(dir); if (!file.exists()) file.mkdirs();
        String[] content = file.list();
        
        boolean dir_contains_files = false, dir_contains_folders = false;
        
        for (String object : content)
        {
            File obj = new File(dir + File.separator + object);
            if (obj.isFile()) dir_contains_files = true;
            else if (obj.isDirectory()) dir_contains_folders = true;
        }
        
        if (first_launch) dir_contains_files = true;
        parent_contains_files.add(dir_contains_files);
        
//        Frame.report("GUARD Cleaner: Проверка директории: " + file.toString() + ", содержит ли файлы: " + dir_contains_files + ", содержит ли папки: " + dir_contains_folders);
        
        if (!first_launch && !dir_contains_files && !dir_contains_folders)
        {
            int num_folders_to_delete = 0;
            
            for (Boolean contains_or_not : parent_contains_files)
            {
                if (!contains_or_not) num_folders_to_delete++;
                else num_folders_to_delete = 0;
            }
            
            if (num_folders_to_delete > 1)
            {
                File dir_to_delete = file;
                for (int i = 0; i < num_folders_to_delete - 1; i++)
                    dir_to_delete = dir_to_delete.getParentFile();
                
                BaseUtils.delete(dir_to_delete);
//                Frame.report("GUARD Cleaner: Удалена пустая директория: " + dir_to_delete.toString());
            } else if (num_folders_to_delete == 1)
            {
                BaseUtils.delete(file);
//                Frame.report("GUARD Cleaner: Удалена пустая директория: " + file.toString());
            }
        }
        
        for (String object : content)
        {
            File obj = new File(dir + File.separator + object);

            if (obj.isDirectory())
            {
                removeEmptyFolders(dir + File.separator + object, parent_contains_files, false);
            }
        }
    }
    
    /**
     * Получает список подпапок в папке клиента и проверяет их на наличие ".jar" файлов.
     * Если будут найдены сторонние ".jar" файлы, лаунчер удалит их и завершит работу.
     * Отправит отчет и список найденных файлов администратору.
     * В методе используется рекурсия.
     * Папки "безопасности": "bin"
     * Папки "mods" и "coremods" не проверяются (проверяются другим специальным методом)
     * @param dir // Директория текущей проверки
     * @param auth_data // Ответ сервера-авторизации
     */
    public static void checkClientsJars(String dir, String[] auth_data)
    {
        File file = new File(dir); if (!file.exists()) file.mkdirs();
        String[] content = file.list();
        
        for (String object : content)
        {
            File obj = new File(dir + File.separator + object);
            
            if (obj.isFile())
            {
                String last_folder = dir.substring(dir.lastIndexOf(File.separator) + 1);
                if (!last_folder.equals("bin") && !(appPath().equalsIgnoreCase(dir + File.separator + object))
                && object.contains(".") && object.substring(object.lastIndexOf(".")).equalsIgnoreCase(".jar"))
                {
                    if (!checkFilesToExceptions(dir + File.separator + object, auth_data[24]))
                    {
                        BaseUtils.delete(obj);
                        find_files.add(dir + File.separator + object);
                        Frame.report("GUARD: Удален сторонний файл: " + dir + File.separator + object);
                        all_clean = false;
                    }
                }
            } else if (obj.isDirectory() && !object.equals("mods") && !object.equals("coremods"))
            {
                checkClientsJars(dir + File.separator + object, auth_data);
            }
        }
    }
    
    /**
     * Отвечает на вопрос: "Есть ли текущий файл в списке файлов-исключений?".
     * При проверке клиента на наличие сторонних JAR файлов и при проверке папок с модификациями,
     * будут учитываться файлы-исключения (из папки "check", "mods", "coremods").
     */
    private static boolean checkFilesToExceptions(String file_path, String check_data)
    {
        boolean there_is = false;
        if (check_data.equals("nocheckfs") || check_data.equals("nomods") || check_data.equals("nocoremods")) return there_is;
        
        String[] files_hashs = check_data.split("<:f:>");
        String md5_file = md5_file(file_path);
        
        for (String file_hash : files_hashs)
        {
            if (md5_file.equalsIgnoreCase(file_hash.split("<:h:>")[1]))
            {
                there_is = true;
                break;
            }
        }
        
        return there_is;
    }
    
    public static List checkSelectedFiles(String files_data)
    {
        String[] files_hashs = files_data.split("<:f:>");
        List<String> to_upload = new ArrayList<>();
        
        for (String file_hash : files_hashs)
        {
            String filename = file_hash.split("<:h:>")[0];
            String hash = file_hash.split("<:h:>")[1];
            
            File file = new File(BaseUtils.getClientDirectory() + File.separator + filename);
            
            if (file.exists())
            {
                if (!md5_file(file.toString()).equalsIgnoreCase(hash))
                {
                    to_upload.add("check/" + filename);
                }
            } else
            {
                to_upload.add("check/" + filename);
            }
        }
        
        return to_upload;
    }
    
    private static final String[] macvb = new String[] { "00-50-56", "00-0C-29", "00-05-69", "00-03-FF", "00-1C-42", "00-0F-4B", "00-16-3E", "08-00-27" };
    
    public static String getHWID()
    {
        try
        {
            NetworkInterface neti = null;
            Enumeration<InetAddress> inetAddresses = null;
            ArrayList<String> ips = new ArrayList<String>();
            
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces()))
            {
                if (!networkInterface.isVirtual()) neti = networkInterface;
                
                if (neti != null)
                {
                    inetAddresses = neti.getInetAddresses();

                    for (InetAddress inetAddress : Collections.list(inetAddresses))
                    {
                        if (inetAddress.isSiteLocalAddress())
                        {
                            ips.add(inetAddress.getHostAddress());
                        }
                    }
                } else
                {
                    Frame.reportErr("Сетевой интерфейс не обнаружен");
                }
            }
            
            String realHWID = "";
            if (!ips.isEmpty())
            {
                for (int i = 0; i < ips.size(); i++)
                {
                    if (BaseUtils.findString(macvb, getHWID(ips.get(i)).substring(0, 8)) == -1)
                        realHWID = getHWID(ips.get(i));
                }
                
                return realHWID;
            } else
            {
                Frame.reportErr("Действительные IP адреса не обнаружены");
                return null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    private static String getHWID(String ip)
    {
        try
        {
            InetAddress address = InetAddress.getByName(ip);
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            
            if (ni != null)
            {
                byte[] mac = ni.getHardwareAddress();
                if (mac != null)
                {
                    String res = "";
                    for (int i = 0; i < mac.length; i++)
                    {
                        res += new Formatter().format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "").toString();
                    }
                    
                    return res;
                } else
                {
                    Frame.reportErr("Адрес не существует или не доступен");
                    return null;
                }
            } else
            {
                Frame.reportErr("Сетевой интерфейс для указанного адреса не найден");
                return null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
