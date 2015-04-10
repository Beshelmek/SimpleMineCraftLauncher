package ru.er_log.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import net.minecraft.Launcher;
import ru.er_log.Settings;
import ru.er_log.Starter;
import ru.er_log.components.Frame;
import static ru.er_log.components.Frame.frame;
import ru.er_log.components.Guard;
import ru.er_log.components.Panel;
import ru.er_log.components.ThemeElements;
import ru.er_log.components.UI_Theme;
import ru.er_log.game.GameLauncher;
import ru.er_log.game.GameUpdater;
import ru.er_log.java.eURLClassLoader;

public class BaseUtils {
    public static final String empty = "";
    public static Launcher launcher;
    public static GameUpdater gameUpdater;
    public static BaseUtils baseUtils = new BaseUtils();
    public static final ConfigUtils config = new ConfigUtils("config", getParentDirectory());
    
    public static String[] servers = null;
    public static boolean offlineTheme = false;
    
    public static BufferedImage openImage(String name)
    {
        try
        {
            BufferedImage img = ImageIO.read(BaseUtils.class.getResource(getTheme().themeDirectory() + name));
            Frame.report("Открыто локальное изображение: " + name);
            return img;
        } catch (IOException e)
        {
            Frame.reportErr("Ошибка при открытии изображения: " + name);
            return new BufferedImage(1, 1, 2);
        }
    }

    public String sendGET(String URL, String params, boolean send)
    {
        HttpURLConnection ct = null;
        try
        {
            params = "?" + params;
            if (send) Frame.report("Установка соединения с: " + URL + (Settings.use_developer_mode ? params : ""));

            URL url = new URL(URL + params);
            ct = (HttpURLConnection) url.openConnection();
            ct.setRequestMethod("GET");
            ct.connect();

            InputStream is = ct.getInputStream();
            StringBuilder response;
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(is)))
            {
                response = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null)
                {
                    response.append(line);
                }
            }

            String str = response.toString();

            if (send) Frame.report("Соединение установлено. Получен ответ: '" + str + "'");
            return str;
        } catch (IOException e)
        {
            if (send) Frame.reportErr("Не удалось установить соединение с: " + URL + ", возвращаю null");
            return null;
        } finally
        {
            if (ct != null) ct.disconnect();
        }
    }
    
    public String sendPOST(String URL, String params, boolean send)
    {
        HttpURLConnection ct = null;
        try
        {
            if (send) Frame.report("Установка соединения с: " + URL + (Settings.use_developer_mode ? ", с параметрами: " + params : ""));

            URL url = new URL(URL);
            ct = (HttpURLConnection) url.openConnection();
            ct.setRequestMethod("POST");
            ct.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            ct.setRequestProperty("Content-Length", "0");
            ct.setRequestProperty("Content-Language", "en-US");
            ct.setUseCaches(false);
            ct.setDoInput(true);
            ct.setDoOutput(true);
            
            DataOutputStream wr = new DataOutputStream(ct.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();

            InputStream is = ct.getInputStream();
            StringBuilder response;
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(is)))
            {
                response = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null)
                {
                    response.append(line);
                }
            }

            String str = response.toString();

            if (send) Frame.report("Соединение установлено. Получен ответ: '" + str + "'");
            return str;
        } catch (IOException e)
        {
            if (send) Frame.reportErr("Не удалось установить соединение с: " + URL + ", возвращаю null");
            return null;
        } finally
        {
            if (ct != null) ct.disconnect();
        }
    }

    public static void sleep(double second)
    {
        try
        {
            Thread.sleep((long) (second * 1000));
        } catch (InterruptedException e) {}
    }

    public static UI_Theme getTheme()
    {
        return Settings.current_theme;
    }
    
    public static void setTheme()
    {
        String script = "jcr_theme.php?action=theme&version=" + Settings.version + "&request=";
        
        try
        {
            if (useOnlineTheme())
            {
                Frame.report("Загужаю тему лаунчера...");
                
                String imgList[] = Frame.onlineData[1].split("<:i:>");
                for (int i = 0; i < imgList.length; i++)
                {
                    setThemeImages(loadImageIO(getURLSc(script), imgList[i]), i);
                    if (offlineTheme) break;
                }
                
                if (!offlineTheme) Frame.report("Тема лаунчера успешо загружена");
                else Frame.reportErr("Не удалось загрузить элементы online темы, запускаю тему по умолчанию");
            } else offlineTheme = true;
        } catch (Exception e)
        {
            offlineTheme = true;
            Frame.reportErr("Не удалось загрузить online тему, запускаю тему по умолчанию");
        }
        
        setThemeImages(null, -2);
        
        if (offlineTheme)
        {
            setThemeImages(null, -1);
            ThemeElements.disableThemeColor = Color.decode(getTheme().themeFieldsInactiveColor());
            ThemeElements.staticThemeColor = Color.decode(getTheme().themeFieldsStaticColor());
        } else
        {
            String colors = StyleUtils.getOnlineThemeColor();
            ThemeElements.disableThemeColor = Color.decode(colors.split(":s:")[0]);
            ThemeElements.staticThemeColor = Color.decode(colors.split(":s:")[1]);
        }
    }
    
    public static BufferedImage loadImageIO(String url, String name) throws Exception
    {
        BufferedImage img;
        try
        {
            img = ImageIO.read(new URL(url + name));
            Frame.report(" * Загружено изображение: " + name); return img;
        } catch (IOException e)
        {
            Frame.reportErr(" * Загрузка прервана на элементе: " + name);
            offlineTheme = true;
            return null;
        }
    }
    
    public static void setThemeImages(BufferedImage img, int num)
    {
        if (img == null && num == -1)
        {
            Panel.favicon		= openImage(getTheme().themeFavicon());
            Panel.background		= openImage(getTheme().themeBackground());
            Panel.logotype		= openImage(getTheme().themeLogotype());
            Panel.authFields		= openImage(getTheme().themeAuthFields());
            Panel.sysButs		= openImage(getTheme().themeSysButs());
            Panel.button		= openImage(getTheme().themeButton());
            Panel.combobox		= openImage(getTheme().themeComboBox());
            Panel.checkbox		= openImage(getTheme().themeCheckBox());
            Panel.fieldBack		= openImage(getTheme().themeFieldBack());
            Panel.progBarImage		= openImage(getTheme().themeProgressBar());
            Panel.modalBack		= openImage(getTheme().themeModalBack());
            Panel.news_back		= openImage(getTheme().themeNewsBack());
            Panel.pressBorder		= openImage(getTheme().themePressedBorder());
            Panel.waitIcon		= openImage(getTheme().themeWaitIcon());
            Panel.alertIcons		= openImage(getTheme().themeAlertIcons());
            Panel.bandColors		= openImage(getTheme().themeBandColors());
            Panel.def_skin		= openImage("char.png");
        } else if (img == null && num == -2)
        {
            Panel.def_skin		= openImage("char.png");
        } else
        {
            switch (num)
            {
                case 0: Panel.favicon = img;
                case 1: Panel.background = img;
                case 2: Panel.logotype = img;
                case 3: Panel.authFields = img;
                case 4: Panel.sysButs = img;
                case 5: Panel.button = img;
                case 6: Panel.combobox = img;
                case 7: Panel.checkbox = img;
                case 8: Panel.fieldBack = img;
                case 9: Panel.progBarImage = img;
                case 10: Panel.modalBack = img;
                case 11: Panel.news_back = img;
                case 12: Panel.pressBorder = img;
                case 13: Panel.waitIcon = img;
                case 14: Panel.alertIcons = img;
                case 15: Panel.bandColors = img;
            }
        }
    }
    
    public static String getURL(String path)
    {
        return "http://" + Settings.domain + "/" + Settings.site_dir + "/" + path;
    }
    
    public static String getURLSc(String script)
    {
        return getURL("scripts/" + script);
    }
    
    public static String getURLFi(String folder)
    {
        return getURL("files/" + folder);
    }
    
    public String[] loadOnlineSettings()
    {
        Frame.report("Загрузка настроек...");
        try
        {
            String url = sendGET(getURLSc("jcr_theme.php"), "action=settings&request=elements&version=" + Settings.version, false);
            
            if (url == null)
            {
                Frame.reportErr("Не удалось загрузить настройки");
                return null;
            } else if (url.contains("<::>"))
            {
                return url.replaceAll("<br>", "").split("<::>");
            } else
            {
                Frame.reportErr("Не удалось загрузить online настройки");
                return null;
            }
        } catch (Exception e)
        {
            Frame.reportErr("Не удалось загрузить online настройки");
            return null;
        }
    }
    
    public static boolean useOnlineTheme()
    {
        try
        {
            return Frame.onlineData[0].equals("true");
        } catch (Exception e) { return false; }
    }
    
    public static String[] getServersNames()
    {
        String[] error = { "Ошибка подключения", "error" };
        try
        {
            String url = baseUtils.sendGET(getURLSc("jcr_status.php"), "action=servers", false);

            if (url == null)
            {
                Frame.reportErr("Не удалось загрузить список серверов");
                return error;
            } else if (url.contains(" :: "))
            {
                servers = url.replaceAll("<br>", "").split("<::>");
                String[] serversNames = new String[servers.length];

                for (int a = 0; a < servers.length; a++)
                {
                    serversNames[a] = servers[a].split(" :: ")[0];
                }

                return serversNames;
            } else
            {
                Frame.reportErr("Не удалось загрузить список серверов");
                return error;
            }
        } catch (Exception e)
        {
            Frame.reportErr("Не удалось загрузить список серверов");
            return error;
        }
    }

    public static String[] getServerAbout()
    {
        int index = Frame.frame.serversList.getSelectedIndex();
        
        if (servers != null)
            return servers[index].split(" :: ");
        return null;
    }
    
    public static String getClientName()
    {
        if (Frame.frame.set_offline.isSelected())
            return ThemeUtils.offlineGameUtils.getClientName(Frame.frame.serversList.getSelectedIndex());
        
        String[] server_info = getServerAbout();
        if(server_info != null) return server_info[0];
        
        return null;
    }
    
    public static String getClientVersion()
    {
        if (Frame.frame.set_offline.isSelected())
            return ThemeUtils.offlineGameUtils.getClientVersion(Frame.frame.serversList.getSelectedIndex());
        
        String[] server_info = getServerAbout();
        
        if(server_info != null)
            return server_info[3];
        
        return null;
    }
    
    public static String getParentDirectory()
    {
        String dir;
        if (getPlatform() != 0)
            dir = getGameDirectory(Settings.game_directory).toString();
        else
            dir = getGameDirectory(Settings.game_directory.replaceFirst(".", "")).toString();
        
        return dir;
    }
    
    public static String getClientFolder()
    {
        if(Settings.use_multi_client)
            return getClientName();
        return "main_client";
    }
    
    public static String getClientDirectory()
    {
        if (Frame.frame.set_offline.isSelected())
            return ThemeUtils.offlineGameUtils.getClientPath(Frame.frame.serversList.getSelectedIndex());
        
        String dir;
        if (getPlatform() != 0)
            dir = getGameDirectory(Settings.game_directory).toString();
        else
            dir = getGameDirectory(Settings.game_directory.replaceFirst(".", "")).toString();
        
        return dir + File.separator + getClientFolder();
    }
    
    public static File getGameDirectory(String gameDirectory)
    {
        String home = System.getProperty("user.home", ".");
        File fiDir;
        switch (getPlatform())
        {
            case 0:
            case 1:
                fiDir = new File(home, gameDirectory + File.separator);
                break;
            case 2:
                String appData = System.getenv(Settings.par_directory);
                if (appData != null)
                    fiDir = new File(appData, gameDirectory + File.separator);
                else
                    fiDir = new File(home, gameDirectory + File.separator);
                break;
            case 3:
                fiDir = new File(home, "Library/Application Support/" + gameDirectory + File.separator);
                break;
            default:
                fiDir = new File(home, gameDirectory + File.separator);
        }
        if (!fiDir.exists() && !fiDir.mkdirs())
        {
            Frame.reportErr("Директория не найдена: " + fiDir);
        }
        return fiDir;
    }
    
    public static int getPlatform()
    {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win"))
            return 2;
        if (osName.contains("mac"))
            return 3;
        if (osName.contains("solaris"))
            return 1;
        if (osName.contains("sunos"))
            return 1;
        if (osName.contains("linux"))
            return 0;
        if (osName.contains("unix"))
            return 0;

        return 4;
    }
    
    static { config.load(); }
    
    public static void setProperty(String s, Object value)
    {
        if (config.checkProperty(s))
            config.changeProperty(s, value);
        else config.put(s, value);
    }
    
    public static void deleteProperty(String s)
    {
        if (config.checkProperty(s))
            config.deleteProperty(s);
    }
    
    public static String getPropertyString(String s)
    {
        if (config.checkProperty(s))
            return config.getPropertyString(s);
        return null;
    }

    public static boolean getPropertyBoolean(String s)
    {
        if (config.checkProperty(s))
            return config.getPropertyBoolean(s);
        return false;
    }

    public static int getPropertyInt(String s)
    {
        if (config.checkProperty(s))
            return config.getPropertyInteger(s);
        return 0;
    }
    
    public static void writeConfig()
    {
        Frame frame = Frame.frame;
        
        if (frame.set_remember.isSelected())
        {
            setProperty("login", EncodingUtils.encode(Frame.authData[23]));
            if (Settings.use_pass_remember)
                setProperty("password", EncodingUtils.encode(new String(frame.password.getPassword())));
            
            setProperty("server", frame.serversList.getSelectedIndex());
        } else
        {
            deleteProperty("login");
            if (Settings.use_pass_remember)
                deleteProperty("password");
        }
        
        int remPr;
        if (frame.set_remember.isSelected()) remPr = 1; else remPr = 2;
        setProperty("remember", remPr);
        
        setProperty("full_screen", frame.set_full.isSelected());
    }
    
    public static void readConfig(final Frame frame)
    {
        String loginPr = EncodingUtils.decode(getPropertyString("login"));
        if (!loginPr.equals("")) frame.login.setText(loginPr);
        
        if (Settings.use_pass_remember)
        {
            String passPr = EncodingUtils.decode(getPropertyString("password"));
            if (!passPr.isEmpty()) frame.password.setText(passPr);
        }
        
        int serversListPr = getPropertyInt("server");
        if (servers != null && serversListPr <= servers.length)
            frame.serversList.setSelectedIndex(serversListPr);
        
        int remPr = getPropertyInt("remember");
        if (remPr == 0 || remPr == 1) frame.set_remember.setSelected(true);
        else frame.set_remember.setSelected(false);
        
        frame.set_full.setSelected(getPropertyBoolean("full_screen"));
        
        int memory = getPropertyInt("memory");
        if (memory >= 256) frame.set_memory.setText(memory+"");
    }
    
    public static void definitionFrames()
    {
        if (Settings.use_personal) Frame.frame.toXFrame(5);
        else prepareUpdate();
    }
    
    public static void prepareUpdate()
    {
        if (Frame.frame.set_update.isSelected())
        {
            deleteProperty(getClientName() + "_hashZip");
            deleteProperty(getClientName() + "_hashNat");
            deleteProperty(getClientName() + "_hashAss");
            delete(new File(getClientDirectory()));
            Frame.frame.set_update.setSelected(false);
        }
        
        boolean zipupdate = false;
        boolean natupdate = false;
        boolean assupdate = false;
        String[] data = Frame.authData;
        String[] mods = data[9].split("<:f:>");
        String[] coremods = data[13].split("<:f:>");
        String[] configs = data[22].split("<:f:>");
        List<String> files = new ArrayList<>();
        
        Frame.report("Проверка директорий перед обновлением...");
        List<Boolean> list = new ArrayList<>(); list.add(true);
        GuardUtils.removeEmptyFolders(BaseUtils.getClientDirectory() + File.separator + "mods", list, true);
        GuardUtils.checkDir(BaseUtils.getClientDirectory() + File.separator + "mods", data[9], true);
        
        if (BaseUtils.versionCompare(BaseUtils.getClientVersion(), "1.6") == 2)
        {
            list.clear(); list.add(true);
            GuardUtils.removeEmptyFolders(BaseUtils.getClientDirectory() + File.separator + "coremods", list, true);
            GuardUtils.checkDir(BaseUtils.getClientDirectory() + File.separator + "coremods", data[13], true);
        }
        Frame.report("Проверка директорий перед обновлением завершена");
        
        String binfolder = getClientDirectory() + File.separator + "bin" + File.separator;
        String modsfolder = getClientDirectory() + File.separator + "mods" + File.separator;
        String coremodsfolder = getClientDirectory() + File.separator + "coremods" + File.separator;
        String configfolder = getClientDirectory() + File.separator + "config" + File.separator;
        
        if (!data[0].equalsIgnoreCase(getPropertyString(getClientName() + "_hashZip"))) { files.add("extra.zip"); zipupdate = true; }
        if (!data[1].equalsIgnoreCase(GuardUtils.md5_file(binfolder + "minecraft.jar"))) files.add("bin/minecraft.jar");
        if (!data[15].equalsIgnoreCase(getPropertyString(getClientName() + "_hashNat")) || !new File(binfolder, "natives").exists()) { files.add("bin/natives.zip"); natupdate = true; }
        if (versionCompare(getClientVersion(), "1.6") == 2)
        {
            if (!data[2].equalsIgnoreCase(GuardUtils.md5_file(binfolder + "lwjgl.jar"))) files.add("bin/lwjgl.jar");
            if (!data[3].equalsIgnoreCase(GuardUtils.md5_file(binfolder + "lwjgl_util.jar"))) files.add("bin/lwjgl_util.jar");
            if (!data[4].equalsIgnoreCase(GuardUtils.md5_file(binfolder + "jinput.jar"))) files.add("bin/jinput.jar");
        } else
        {
            if (!data[17].equalsIgnoreCase(GuardUtils.md5_file(binfolder + "libraries.jar"))) files.add("bin/libraries.jar");
            if (!new File(getClientDirectory() + "/assets").exists() || !data[16].equalsIgnoreCase(getPropertyString(getClientName() + "_hashAss"))) { files.add("assets.zip"); assupdate = true; }
            if (getServerAbout()[4].equalsIgnoreCase("true") && !data[18].equals(GuardUtils.sha1(GuardUtils.md5_file(binfolder + "forge.jar")))) files.add("bin/forge.jar");
            if (getServerAbout()[5].equalsIgnoreCase("true") && !data[19].equals(GuardUtils.sha1(GuardUtils.md5_file(binfolder + "liteloader.jar")))) files.add("bin/liteloader.jar");
        }
        
        if (!mods[0].equals("nomods"))
        for (String mod : mods)
        {
            String[] mHash = mod.split("<:h:>");
            if (!mHash[1].equals(GuardUtils.md5_file(modsfolder + mHash[0]))) files.add("mods/" + mHash[0]);
        }
        
        if (BaseUtils.versionCompare(BaseUtils.getClientVersion(), "1.6") == 2)
        {
            if (!coremods[0].equals("nocoremods"))
            for (String coremod : coremods)
            {
                String[] mHash = coremod.split("<:h:>");
                if (!mHash[1].equals(GuardUtils.md5_file(coremodsfolder + mHash[0]))) files.add("coremods/" + mHash[0]);
            }
        }
        
        if (!configs[0].equals("noconfigs"))
        {
            for (String confdata : configs)
            {
                String[] conf_hash = confdata.split("<:h:>");
                if (!GuardUtils.md5_file(configfolder + conf_hash[0]).equalsIgnoreCase(conf_hash[1]))
                {
                    files.add("config/" + conf_hash[0]);
                }
            }
        }
        
        if (!data[24].equals("nocheckfs"))
        files.addAll(GuardUtils.checkSelectedFiles(data[24]));
        
        if (!files.isEmpty())
        {
            Frame.report("Список загружаемых файлов: ");
            for (Object s : files.toArray()) Frame.report(" * " + s.toString());
        }
        
        if (BaseUtils.getPlatform() == 0)
            Frame.frame.setSize(Settings.frame_width + Settings.linux_frame_w, Settings.frame_height + Settings.linux_frame_h); 
        
        gameUpdater = new GameUpdater(files, zipupdate, natupdate, assupdate, data);
        
        Frame.frame.toXFrame(4);
        
        gameUpdater.start();
    }
    
    public static void startGame(String[] data)
    {
        if (!Frame.frame.set_offline.isSelected())
            setProperty(getClientFolder(), getServerAbout()[0] + "-::-" + getClientVersion() + "-::-" + getServerAbout()[4] + "-::-" + getServerAbout()[5]);

        if(Guard.guard != 1 && getPlatform() == 2){
            frame.setAlert("Запустите лаунчер с помощью стартера!", 0, 2);
            return;
        }
        
        if (BaseUtils.versionCompare(BaseUtils.getClientVersion(), "1.6") == 1){
            try { new GameLauncher(data); }
            catch (IOException e) { Frame.reportErr("Ошибка при запуске игры"); e.printStackTrace(); }
        }
    }
    
    /* Вернет 1, если первая версия игры будет больше второй, 2 если вторая будет больше первой */
    public static int versionCompare(String one, String two)
    {
        try
        {
            String[] one_split = one.split("\\.");
            String[] two_split = two.split("\\.");
            
            int[] one_int = new int[one_split.length];
            int[] two_int = new int[two_split.length];
            
            for (int i = 0; i < one_split.length; i++)
                one_int[i] = new Integer(one_split[i]);
            
            for (int i = 0; i < two_split.length; i++)
                two_int[i] = new Integer(two_split[i]);
            
            if (one_int.length == 3 && two_int.length == 3)
            {
                if (one_int[0] > two_int[0]) return 1;
                else if (one_int[0] < two_int[0]) return 2;
                else
                {
                    if (one_int[1] > two_int[1]) return 1;
                    else if (one_int[1] < two_int[1]) return 2;
                    else
                    {
                        if (one_int[2] > two_int[2]) return 1;
                        else if (one_int[2] == two_int[2]) return 1;
                        else return 2;
                    }
                }
            } else if (one_int.length == 2 || two_int.length == 2)
            {
                if (one_int[0] > two_int[0]) return 1;
                else if (one_int[0] < two_int[0]) return 2;
                else
                {
                    if (one_int[1] > two_int[1]) return 1;
                    else if (one_int[1] == two_int[1]) return 1;
                    else return 2;
                }
            } else return 213;
        } catch(NumberFormatException e)
        {
            e.printStackTrace();
            return 0;
        }
    }
    
    public static boolean checkInternetConnection()
    {
        try
        {
            return checkInternetConnection(new URL("http://" + Settings.domain));
        } catch (MalformedURLException ex)
        {
            return false;
        }
    }
    
    public static boolean checkInternetConnection(URL url)
    {
        boolean result = false;
        HttpURLConnection con = null;
        try
        {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("HEAD");
            result = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (IOException e) {}
        finally
        {
            if (con != null)
            {
                try { con.disconnect(); }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
        return result;
    }
    
    public static void decisionToSendReport(boolean create_new_stream)
    {
        if (Settings.use_send_report && !GuardUtils.all_clean)
        {
            if (create_new_stream) new Thread() { public void run() { sendReport((String[]) GuardUtils.find_files.toArray(new String[] {}), ""); }}.start();
            else sendReport((String[]) GuardUtils.find_files.toArray(new String[] {}), "");
        }
    }
    
    /**
     * Отправляет оповещание администратору на веб-сервер, если обнаружены сторонние файлы
     * @param find_files // массив с директориями найденных файлов
     * @param note // заметка
     */
    private static void sendReport(String[] find_files, String note)
    {
        Frame.report("GUARD: Отправка отчета...");
        
        if (find_files.length == 0) { Frame.report("GUARD: Отчет не был отправлен, так как список файлов пуст"); return; }
        
        String files = "";
        for (String find_file : find_files) files += find_file + "<:f:>";
        files = files.substring(0, files.length() - 5).replaceAll(" ", "_");
        
        try
        {
            String answer = baseUtils.sendPOST(
                    getURLSc("jcr_auth.php")
                    , "action=report&login=" + Frame.authData[23] + "&files=" + files + "&message=" + note + "&session=" + Frame.authData[7] + "&code=" + GuardUtils.sha1(Settings.protect_key)
                    , false
            );
            
            if (answer.equalsIgnoreCase("Done"))
            {
                Frame.report("GUARD: Отчет успешно отправлен");
            } else
            {
                Frame.reportErr("GUARD: Не удалось отправить отчет");
            }
        } catch (Exception e)
        {
            Frame.reportErr("GUARD: Не удалось отправить отчет");
        }
    }
    
    public static void openLink(String url)
    {
        try
        {
            Object o = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            o.getClass().getMethod("browse", new Class[] { URI.class }).invoke(o, new Object[] { new URI(url) });
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | URISyntaxException e)
        { Frame.reportErr("Не удалось открыть ссылку: " + url); }
    }
    
    public static void delete(File file)
    {
        try {
            if (!file.exists()) return;
            if (file.isDirectory())
            {
                for (File f : file.listFiles()) delete(f);
                file.delete();
            } else file.delete();
        } catch (Exception e)
        { Frame.reportErr("Удаление не удалось: " + file.toString()); }
    }
    
    public static void patchClient(eURLClassLoader cl)
    {
        try
        {
            if (versionCompare(getClientVersion(), "1.6") == 1) return;
            String mcver = Frame.frame.set_offline.isSelected() ? getClientVersion() : getServerAbout()[3];
            String[] library = Settings.libraryForPath;
            
            Frame.report("Запуск процесса патчинга: ");
            Frame.report(" * Обнаружение клиента...");
            Frame.report(" * Клиент: " + getClientName() + " :: " + mcver);
            Frame.report(" * Поиск версии в библиотеке...");
            
            for (String lib : library)
            {
                if (mcver.contains(lib.split("::")[0].replace("x", "")))
                {
                    Frame.report(" * Патчинг клиента...");
                    Field f = cl.loadClass(Settings.old_mine_class).getDeclaredField(lib.split("::")[1]);
                    Field.setAccessible(new Field[] { f }, true);
                    f.set(null, new File(getClientDirectory()));
                    Frame.report(" * Файл пропатчен: " + Settings.old_mine_class + " :: " + lib.split("::")[1]);
                    Frame.report(" * Патчинг клиента успешно завершен");
                    return;
                }
            }
            
            Frame.reportErr(" * Данная версия клиента не обнаружена!");
            Frame.reportErr(" * Не удалось произвести патчинг клиента");
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
            Frame.reportErr(" * Ошибка: поле клиента не корректно");
        }
    }
    
    public static void updateProgram() throws Exception
    {
        Frame.report("Запуск процесса обновления программы...");
        
        String appURL = getURLFi("program/" + Frame.authData[14]);
        Frame.report("Загрузка файла: " + appURL);
        
        InputStream is = new BufferedInputStream(new URL(appURL).openStream());
        FileOutputStream fos = new FileOutputStream(GuardUtils.appPath());

        int bs = 0;
        byte[] buffer = new byte[65536];
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        while ((bs = is.read(buffer, 0, buffer.length)) != -1)
        {
            fos.write(buffer, 0, bs);
            md5.update(buffer, 0, bs);
        }
        is.close();
        fos.close();
        Frame.report("Файл загружен: " + appURL);
        
        {
            JOptionPane.showMessageDialog(null, "Лаунчер успешно обновлен! Перезапустите его.", "SimpleMinecraft.Ru | Updater", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    public static String getProgramFormat()
    {
        String[] format = { ".jar", ".exe" };
        String path = GuardUtils.appPath().toLowerCase();
        if (path.substring(path.lastIndexOf("/")).contains(format[1]))
            return format[1];
        else
            return format[0];
    }
    
    public static void restart()
    {
        Frame.report("Перезапуск программы...");
        try { Starter.main(null); }
        catch (Exception e) { e.printStackTrace(); return; }
        System.exit(0);
    }
    
    public static int findString(String[] array, String str)
    {
        for (int a = 0; a < array.length; a++)
            for (int b = 0; b < array.length; b++)
                if (array[a] != null && array[a].equals(str)) return a;
        
        return -1;
    }
    
    public static long random(long from, long to)
    {
        Random random = new Random();
        return from + Math.abs(random.nextLong()) % (to - (from - 1));
    }
}
