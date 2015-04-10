package ru.er_log.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import ru.er_log.components.Frame;
import ru.er_log.Settings;
import ru.er_log.components.Button;
import ru.er_log.components.CheckBox;
import ru.er_log.components.ComboBox;
import ru.er_log.components.Panel;
import ru.er_log.components.PassField;
import ru.er_log.components.STextField;
import ru.er_log.components.LoginField;
import static ru.er_log.components.Frame.authData;
import static ru.er_log.components.Frame.frame;
import static ru.er_log.components.ThemeElements.*;
import static ru.er_log.utils.BaseUtils.definitionFrames;
import static ru.er_log.utils.BaseUtils.startGame;

public class ThemeUtils extends BaseUtils {

    public static Panel pb;
    public static ThemeUtils themeUtils;
    public static boolean[] save_selected;
    public static String save_memory;
    
    public static OfflineGameUtils offlineGameUtils = new OfflineGameUtils();

    public static void updateStyle(Frame frame)
    {
        if (BaseUtils.getPlatform() != 0)
        {
            frame.turn = turn(turn);
            frame.close = close(close);
        }
        
        frame.doAuth       = doAuth(frame.doAuth.getText());
        frame.take         = actionAlertPane(frame.take.getText());
        frame.exit         = actionUpdate(frame.exit.getText());
        frame.upd_take     = takeUpdate(frame.upd_take.getText());
        frame.settings     = toSettings(frame.settings.getText());
        frame.set_cancel   = cancelSettings(frame.set_cancel.getText());
        frame.set_take     = takeSettings(frame.set_take.getText());
        frame.toGame       = toGame(frame.toGame.getText());
        
        frame.login        = new LoginField(57, 179, 232, 36, disableThemeColor, staticThemeColor);
        frame.password     = new PassField(57, 223, 232, 36, disableThemeColor, staticThemeColor);
        frame.serversList  = new ComboBox(getServersNames(), 315, frame);
        
        frame.set_remember = new CheckBox(68, 188, 210, 16, frame.set_remember.getText(), true);
        frame.set_update   = new CheckBox(68, 214, 210, 16, frame.set_update.getText(), frame.set_update.isSelected());
        frame.set_full     = new CheckBox(68, 240, 210, 16, frame.set_full.getText(), frame.set_full.isSelected());
        frame.set_offline  = new CheckBox(68, 266, 210, 16, frame.set_offline.getText(), frame.set_offline.isSelected());
        frame.set_memory   = new STextField(72, 292, 48, 18, frame.set_memory.getText(), 4, disableThemeColor, staticThemeColor);
    }

    public static JButton close(ImageUtils imgUt)
    {
        JButton button = new Button(imgUt, "");

        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                Runtime.getRuntime().exit(1);
            }
        };
        button.addActionListener(action);

        return button;
    }

    public static JButton turn(ImageUtils imgUt)
    {
        JButton button = new Button(imgUt, "");

        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                Frame.frame.setState(Frame.ICONIFIED);
            }
        };
        button.addActionListener(action);

        return button;
    }

    public static JButton toSettings(String name)
    {
        JButton button = new Button(name, 1, 57, 269, 112, 36);
        
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                Frame frame = Frame.frame; frame.toXFrame(2);
                save_selected = new boolean[] { frame.set_remember.isSelected(), frame.set_update.isSelected(), frame.set_full.isSelected(), frame.set_offline.isSelected() };
                save_memory = frame.set_memory.getText();
            }
        };
        button.addActionListener(action);

        return button;
    }
    
    public static JButton cancelSettings(String name)
    {
        JButton button = new Button(name, 1, 57, 326, 112, 36);

        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                Frame frame = Frame.frame; frame.toXFrame(1);
                frame.set_remember.setSelected(save_selected[0]);
                frame.set_update.setSelected(save_selected[1]);
                frame.set_full.setSelected(save_selected[2]);
                frame.set_offline.setSelected(save_selected[3]);
                frame.set_memory.setText(save_memory);
                save_selected = null; save_memory = "";
            }
        };
        button.addActionListener(action);

        return button;
    }
    
    private static ComboBox savedServersList;
    
    public static JButton takeSettings(String name)
    {
        JButton button = new Button(name, 2, 177, 326, 112, 36);
        
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                Frame frame = Frame.frame; 
                
                if (frame.set_offline.isSelected())
                {
                    savedServersList = frame.serversList;
                    frame.serversList = new ComboBox(offlineGameUtils.getClientsList(), 315, frame);
                    frame.panel.add(frame.serversList);
                    frame.doAuth.setText(Settings.auth_but_offline_text);
                } else if (savedServersList != null)
                {
                    frame.serversList = savedServersList;
                    frame.doAuth.setText(Settings.auth_but_auth_text);
                }
                
                String smemory = frame.set_memory.getText();
                if (!save_memory.equals(smemory))
                {
                    int memory = 0;
                    try { memory = new Integer(smemory); } catch (Exception ex) {}
                    if (memory >= 256) { setProperty("memory", memory); restart(); }
                    else { frame.set_memory.setText(save_memory); frame.toXFrame(1); }
                } else frame.toXFrame(1);
            }
        };
        button.addActionListener(action);

        return button;
    }
    
    public static JButton doAuth(String name)
    {
        JButton button = new Button(name, 2, 177, 269, 112, 36);

        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if (StreamUtils.t != null && StreamUtils.t.isAlive() && StreamUtils.t.getName().equals("getServerOnline")) return;
                
                Frame.frame.panel.resetAlerts();
                if (!Frame.frame.set_offline.isSelected())
                {
                    if (!(Frame.frame.login.getText().equals(Settings.login_text)) && !(new String(Frame.frame.password.getPassword()).equals(Settings.pass_text)))
                    { StreamUtils.doLogin(); }
                    else { Frame.frame.setAlert("Неверный логин или пароль", 3, 0); }
                } else if (offlineGameUtils.getClientsList().length == 2 && offlineGameUtils.getClientsList()[1].equals("error"))
                {
                    Frame.frame.setAlert("Используйте режим \"мультиплеер\"", 3, 0);
                } else
                {
                    startGame(Frame.authData);
                }
            }
        };
        button.addActionListener(action);

        return button;
    }
    
    public static JButton actionAlertPane(String name)
    {
        JButton button = new Button(name, 2, 177, 326, 112, 36);
        
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if (authData[8].equals("true") || Settings.use_developer_mode)
                {
                    definitionFrames();
                } else
                {
                    frame.toXFrame(3);
                }
            }
        };
        button.addActionListener(action);

        return button;
    }
    
    public static JButton actionUpdate(String name)
    {
        JButton button = new Button(name, 1, 57, 326, 112, 36);

        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        };
        button.addActionListener(action);

        return button;
    }

    public static JButton takeUpdate(String name)
    {
        JButton button = new Button(name, 2, 177, 326, 112, 36);
        
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                StreamUtils.actionTakeUpdate();
            }
        };
        button.addActionListener(action);

        return button;
    }
    
    public static JButton toGame(String name)
    {
        final JButton button = new Button(name, 2, 177, 326, 112, 36);
        
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                button.setEnabled(false);
                if (Settings.use_loading_news)
                {
                    Panel.news_xcoord = Panel.news_back.getWidth();
                    Frame.frame.panel.remove(Frame.frame.newsScPane);
                }
                
                BaseUtils.prepareUpdate();
            }
        };
        button.addActionListener(action);

        return button;
    }
}
