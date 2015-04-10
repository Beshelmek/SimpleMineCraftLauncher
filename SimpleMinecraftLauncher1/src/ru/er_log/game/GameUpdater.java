package ru.er_log.game;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import ru.er_log.components.Frame;
import ru.er_log.utils.BaseUtils;
import ru.er_log.utils.GuardUtils;
import ru.er_log.utils.ZipUtils;

public class GameUpdater extends Thread {
    
    public int percents = 0;
    public long totalsize = 0;
    public long currentsize = 0;
    public String currentfile = "...";
    public String filename = "...";
    public int downloadspeed = 0;
    public List<String> files = new ArrayList<>();
    public String state = "...";
    public boolean zipupdate = false;
    public boolean natupdate = false;
    public boolean assupdate = false;
    public String[] data;
    
    public GameUpdater(List<String> files, boolean zipupdate, boolean natupdate, boolean assupdate, String[] data)
    {
        super("GameUpdater");
        this.files = files;
        this.zipupdate = zipupdate;
        this.natupdate = natupdate;
        this.assupdate = assupdate;
        this.data = data;
    }
    
    @Override
    public void run()
    {
        try
        {
            String pathTo = BaseUtils.getClientDirectory() + File.separator;
            String urlTo = BaseUtils.getURL("files/clients/" + (BaseUtils.getClientName()) + "/");
            
            File dir = new File(pathTo);
            if (!dir.exists()) dir.mkdirs();

            state = "вычисление размера...";

            for (String file : files)
            {
                URLConnection urlconnection = new URL(spaceParser(urlTo + file)).openConnection();
                urlconnection.setDefaultUseCaches(false);
                totalsize += urlconnection.getContentLength();
            }

            state = "загрузка файлов...";
            if (!files.isEmpty())
                Frame.report("Запуск процесса загрузки файлов: ");

            byte[] buffer = new byte[65536];
            for (String file : files)
            {
                currentfile = filename = file;
                if (currentfile.contains("/"))
                {
                    filename = currentfile.substring(currentfile.lastIndexOf("/") + 1);
                    if (!currentfile.substring(0, 6).equals("check/"))
                    {
                        dir = new File(pathTo + currentfile.substring(0, currentfile.lastIndexOf("/")));
                        if (!dir.exists()) dir.mkdirs();
                    }
                }
                
                Frame.report(" * Загрузка файла: " + currentfile);
                
                InputStream inputstream = new BufferedInputStream(new URL(spaceParser(urlTo + file)).openStream());
                FileOutputStream fos = new FileOutputStream(process_check_path(pathTo, file));
                long downloadStartTime = System.currentTimeMillis();
                int downloadedAmount = 0, bufferSize;
                MessageDigest m = MessageDigest.getInstance("MD5");
                while ((bufferSize = inputstream.read(buffer, 0, buffer.length)) != -1)
                {
                    fos.write(buffer, 0, bufferSize);
                    m.update(buffer, 0, bufferSize);
                    currentsize += bufferSize;
                    percents = (int) (currentsize * 100 / totalsize);
                    downloadedAmount += bufferSize;
                    long timeLapse = System.currentTimeMillis() - downloadStartTime;
                    if (timeLapse >= 1000L)
                    {
                        downloadspeed = (int) ((int) (downloadedAmount / (float) timeLapse * 100.0F) / 100.0F);
                        downloadedAmount = 0;
                        downloadStartTime += 1000L;
                    }
                }
                inputstream.close();
                fos.close();
                Frame.report(" * * Файл " + filename + " загружен");
            }

            state = "завершение...";

            if (zipupdate)
            {
                String md5h = GuardUtils.md5_file(BaseUtils.getClientDirectory() + "/extra.zip");
                BaseUtils.setProperty(BaseUtils.getClientName() + "_hashZip", GuardUtils.sha1(md5h));
                ZipUtils.unzip("extra.zip");
            }
            
            if (natupdate)
            {
                String md5h = GuardUtils.md5_file(BaseUtils.getClientDirectory() + "/bin/natives.zip");
                BaseUtils.setProperty(BaseUtils.getClientName() + "_hashNat", md5h);
                ZipUtils.unzip(BaseUtils.getClientDirectory() + "/bin/", "natives.zip");
            }
            
            if (assupdate)
            {
                String md5h = GuardUtils.md5_file(BaseUtils.getClientDirectory() + "/assets.zip");
                BaseUtils.setProperty(BaseUtils.getClientName() + "_hashAss", md5h);
                ZipUtils.unzip("assets.zip");
            }
            
            BaseUtils.startGame(data);
            
            state = "загрузка завершена";
        } catch (IOException | NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            state = e.toString();
        }
    }
    
    // Удаляет "check/" в названии файла для корректной загрузки содержимого папки "check"
    public String process_check_path(String path, String file)
    {
        if (file.substring(0, 6).equals("check/"))
        {
            file = file.replaceFirst("check/", "");

            if (file.contains("/"))
            {
                File dir = new File(path + file.substring(0, file.lastIndexOf("/")));
                if (!dir.exists()) dir.mkdirs();
            }
        }
        
        return path + file;
    }
    
    private String spaceParser(String str)
    {
        return str.replaceAll(" ", "%20"); // Заменяет все пробелы на "%20" в целях корректной загрузки клиента
    }
}
