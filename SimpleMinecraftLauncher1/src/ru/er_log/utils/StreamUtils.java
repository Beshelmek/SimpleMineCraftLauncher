package ru.er_log.utils;

import java.awt.Color;
import java.io.IOException;
import ru.er_log.Settings;
import ru.er_log.components.Frame;
import ru.er_log.components.ThemeElements;
import ru.er_log.components.PersonalCab;
import static ru.er_log.utils.BaseUtils.*;
import static ru.er_log.components.Frame.*;

public class StreamUtils {
    
    public static Thread t = null;
    
    public static void doLogin()
    {
        join(t);
        t = new Thread() {
            public void run()
            {
                report("Авторизация...");
                frame.elEnabled(false);
                frame.panel.waitIcon(true, 0);

                String auth = null;
                
                try
                {
                    auth = baseUtils.sendPOST(
                            getURLSc("jcr_auth.php")
                            , "action=auth&login=" + frame.login.getText() + "&password=" + new String(frame.password.getPassword()) + "&hash=" + GuardUtils.md5_file(GuardUtils.appPath()) + "&format=" + getProgramFormat() + "&client=" + getClientName() + "&version=" + getClientVersion() + "&forge=" + getServerAbout()[4] + "&liteloader=" + getServerAbout()[5] + "&mac=" + GuardUtils.getHWID() + "&code=" + GuardUtils.sha1(Settings.protect_key)
                            , true
                    );
                } catch (Exception e)
                {
                    Frame.reportErr("Не удалось пройти авторизацию");
                }
                
                frame.panel.resetAlerts();
                
                if (auth == null)
                {
                    frame.setAlert("Ошибка подключения", 2, 0);
                } else if (auth.trim().equals("BadParams") || auth.toLowerCase().contains("error"))
                {
                    frame.setAlert("Внутренняя ошибка", 2, 0);
                    reportErr("Ошибка в переданных параметрах");
                } else if (auth.trim().equals("BadCode"))
                {
                    frame.setAlert("Внутренняя ошибка", 2, 0);
                    reportErr("Неверный код доступа на web-сервер");
                } else if (auth.trim().equals("BadHWID"))
                {
                    frame.setAlert("Ошибка доступа", 3, 0);
                    reportErr("Неверный HWID пользователя");
                } else if (auth.trim().equals("BadUserHWID"))
                {
                    frame.setAlert("Войдите со своего компьюетра", 3, 0);
                    reportErr("HWID пользователя не совпал с оригинальным");
                } else if (auth.trim().equals("Banned"))
                {
                    frame.setAlert("Вы заблокированы", 2, 0);
                    reportErr("Пользователь заблокирован");
                } else if (auth.contains("<::>"))
                {
                    authData = auth.replaceAll("<br>", "").split("<::>");

                    BaseUtils.writeConfig();

                    frame.setAlert("Вход выполнен успешно", 1, 0);
                    BaseUtils.sleep(1.0);
                    
                    if (authData[21].equals("true")) frame.toXFrame(6);
                    else if (authData[8].equals("true") || Settings.use_developer_mode)
                    {
                        definitionFrames();
                    } else
                    {
                        frame.toXFrame(3);
                    }
                    
                    PersonalCab.setImages(authData);
                    frame.panel.repaint();
                } else
                {
                    frame.setAlert("Неверный логин или пароль", 3, 0);
                }
                
                frame.elEnabled(true);
            }
        }; t.setName("doLogin"); t.start();
    }
    
    public static void loadNewsPage(final String url)
    {
        Frame.report("Загрузка новостей...");
        final Color c = ThemeElements.staticThemeColor;
        join(t);
        t = new Thread() {
            public void run()
            {
                try
                {
                    Frame.frame.news_pane.setPage(url);
                    Frame.report("Страница новостей загружена");
                } catch (IOException e)
                {
                    Frame.frame.news_pane.setText("<center><font color=\"rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")\" style=\"font-family: Arial, Tahoma, Helvetica, sans-serif\">Не удалось загрузить новости</font></center>");
                    Frame.reportErr("Ошибка при загрузке новостей");
                }
            }
        }; t.setName("loadNewsPage"); t.start();
    }
    
    public static void getServerOnline()
    {
        if (t != null && t.getName().equals("getServerOnline")) { t.interrupt(); t.stop(); } else join(t);
        t = new Thread() {
            public void run()
            {
                try
                {
                    String server[] = getServerAbout();
                    String url = baseUtils.sendGET(
                            getURLSc("jcr_status.php")
                            , "action=status&ip=" + server[1] + "&port=" + server[2]
                            , false
                    );

                    frame.panel.resetAlerts();

                    if (url == null)
                    {
                        reportErr("Ошибка подключения к серверу: " + url);
                        frame.setAlert("Ошибка подключения", 2, 0);
                        frame.panel.waitIcon(false, 0);
                    } else if (url.contains("<::>"))
                    {
                        String[] result = url.split("<::>");
                        if (new Integer(result[0]) >= new Integer(result[1]))
                        {
                            frame.setAlert("Сервер доступен: " + result[0] + " из " + result[1], 1, 0);
                        } else
                        {
                            frame.setAlert("Сервер доступен: " + result[0] + " из " + result[1], 1, 0);
                        }
                        frame.panel.waitIcon(false, 0);
                    } else if (url.trim().equals("OFF"))
                    {
                        frame.setAlert("Сервер недоступен", 2, 0);
                        frame.panel.waitIcon(false, 0);
                    } else if (url.trim().equals("TechWork"))
                    {
                        frame.setAlert("Технические работы", 3, 0);
                        frame.panel.waitIcon(false, 0);
                    } else
                    {
                        reportErr("Внутренняя ошибка");
                        frame.setAlert("Внутренняя ошибка", 2, 0);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }; t.setName("getServerOnline"); t.start();
    }

    public static void actionTakeUpdate()
    {
        join(t);
        t = new Thread() {
            public void run()
            {
                try
                {
                    frame.upd_take.setEnabled(false);
                    frame.panel.waitIcon(true, 382, 1);
                    frame.setAlert("", 0, 1);
                    BaseUtils.sleep(1.0);
                    updateProgram();
                } catch (Exception e)
                {
                    frame.setAlert("Ошибка при обновлении", 3, 391, 1);
                    reportErr("Ошибка при обновлении программы");
                    
                    frame.upd_take.setEnabled(true);
                    frame.upd_take.setText("Еще раз");
                    
                    frame.panel.waitIcon(false, 1);
                }
            }
        }; t.setName("actionTakeUpdate"); t.start();
    }
    
    public static void join(Thread t)
    {
        if (t != null)
        {
            try { t.join(); }
            catch (InterruptedException ex)
            { Frame.report("Не удалось дождаться завершения потока: " + t.getName()); }
        }
    }
}
