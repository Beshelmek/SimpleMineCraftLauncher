package ru.er_log.components;

import com.sun.awt.AWTUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import ru.er_log.Settings;
import ru.er_log.Starter;
import static ru.er_log.components.Guard.StartGuard;
import static ru.er_log.components.Launcher.checkLauncher;
import static ru.er_log.components.startMinecraft.appletMinecraft;
import static ru.er_log.components.startMinecraft.startMinecraft;
import ru.er_log.splash.SplashFrame;
import ru.er_log.utils.BaseUtils;
import static ru.er_log.utils.BaseUtils.*;
import ru.er_log.utils.GuardUtils;
import ru.er_log.utils.ImageUtils;
import static ru.er_log.utils.StartUtils.CheckLauncherPach;
import ru.er_log.utils.StreamUtils;
import ru.er_log.utils.ThemeUtils;

/**
 *
 * @author Goodvise
 * 
 */
public class Frame extends JFrame implements FocusListener, KeyListener {
    
    public static String[] authData = null;
    public static String[] onlineData = null;
    private static int debLine = 1;
    
    public JButton turn  = new JButton();
    public JButton close = new JButton();
    private int X = 0, Y = 0;
    
    public static Frame frame;
    public GuardUtils gu = new GuardUtils();
    public Panel panel = new Panel();
        public JLabel logotype           = new JLabel(new ImageIcon(Panel.logotype));
        public ComboBox serversList      = new ComboBox(null, 324, null);
        public JTextPane news_pane       = new JTextPane();
        public JScrollPane newsScPane    = new JScrollPane(news_pane);
        public JTextField login          = new JTextField();
        public JPasswordField password   = new JPasswordField();
        public JButton toGame            = new JButton("В игру");
        public JButton take              = new JButton("Продолжить");
        public JButton exit              = new JButton("Выход");
        public JButton upd_take          = new JButton("Обновить");
        public JButton doAuth            = new JButton("Войти");
        public JButton settings          = new JButton("Настройки");
            public JButton set_cancel     = new JButton("Отмена");
            public JButton set_take       = new JButton("Принять");
            public JCheckBox set_remember = new JCheckBox("Запомнить логин и пароль");
            public JCheckBox set_update   = new JCheckBox("Перекачать клиент игры");
            public JCheckBox set_full     = new JCheckBox("Полноэкранный режим");
            public JCheckBox set_offline  = new JCheckBox("Оффлайн режим");
            public JTextField set_memory  = new JTextField("32".equals(System.getProperty("sun.arch.data.model")) ? "512" : "1024");
            
        
    public Frame()
    {
        if (getPlatform() != 0)
        {
            this.setUndecorated(true); AWTUtilities.setWindowOpaque(this, false);
            this.setPreferredSize(new Dimension(Settings.frame_width, Settings.frame_height));
        } else this.setPreferredSize(new Dimension(Settings.frame_width + Settings.linux_frame_w, Settings.frame_height + Settings.linux_frame_h));
        this.setSize(this.getPreferredSize());
        this.setTitle(Settings.title + " :: " + Settings.version);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setIconImage(Panel.favicon); mouseListener();
        try { ThemeUtils.updateStyle(this); } catch(Exception e) {}
        
            login.setText(Settings.login_text);
            login.addActionListener(null);
            login.addFocusListener(this);
            
            password.setText(Settings.pass_text);
            password.addActionListener(null);
            password.addFocusListener(this);
            
            news_pane.setOpaque(false);
            news_pane.setBorder(null);
            news_pane.setContentType("text/html");
            news_pane.setEditable(false);
            news_pane.setFocusable(false);
            news_pane.addHyperlinkListener(new HyperlinkListener() {
                public void hyperlinkUpdate(HyperlinkEvent e)
                {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                    openLink(e.getURL().toString());
                }
            });
            
            newsScPane.setOpaque(false);
            newsScPane.getViewport().setOpaque(false);
            newsScPane.setBorder(null);
            newsScPane.setBounds(Settings.frame_width + 25, 10 + 35, Panel.news_back.getWidth() - 25 * 2, 430 - 35 * 2);
            
            if (Settings.use_monitoring)
            serversList.addMouseListener(new MouseListener()
            {
                public void mouseReleased(MouseEvent e) {}
                public void mousePressed(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
                public void mouseEntered(MouseEvent e) {}
                public void mouseClicked(MouseEvent e)
                {
                    if(!serversList.error)
                    {
                        if (serversList.getPressed() || e.getButton() != MouseEvent.BUTTON1) return;
                        
                        if (serversList.getSelectValue())
                        { panel.waitIcon(true, 0); StreamUtils.getServerOnline(); }
                    }
                }
            });
            
            logotype.setBounds((Settings.frame_width - Panel.logotype.getWidth()) / 2, Settings.logo_indent, Panel.logotype.getWidth(), Panel.logotype.getHeight());
            if (Settings.use_logo_as_url) logotype.addMouseListener(new MouseListener()
            {
                public void mouseReleased(MouseEvent e) {}
                public void mousePressed(MouseEvent e) {}
                public void mouseExited(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); }
                public void mouseEntered(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); }
                public void mouseClicked(MouseEvent e) { openLink("http://" + (Settings.logo_url.isEmpty() ? Settings.domain : Settings.logo_url)); }
            });
            
          panel.addKeyListener(this);
          login.addKeyListener(this);
          password.addKeyListener(this);
          
          addFrameElements(false);
          addAuthElements(false);
          
        this.setContentPane(panel);
    }
    
    public static void beforeStart(SplashFrame sframe) throws Exception
    {
        report("Запуск лаунчера: " + Settings.version);
        try
        {
            if (sframe!= null) sframe.setStatus("установка системного LnF...");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            report("Установка системного LnF успешно завершена");
        } catch (Exception e)
        { report("Не удалось установить системный LnF"); }
        
        if (sframe!= null) sframe.setStatus("Идет загрузка настроек...");
        onlineData = baseUtils.loadOnlineSettings();
        
        if (sframe!= null) sframe.setStatus("Идет проверка...");
        setTheme();

        if(BaseUtils.getPlatform() == 2){
            if(Settings.test_mode != true){
                StartGuard();
                Frame.report("Запуск версии для системы: Windows.");
            }else{
                Frame.report("Запуск тестовой версии лаунчера.");
                checkLauncher();
            }
        }else{
            Frame.report("Запуск версии для системы: Mac, Linux.");
            checkLauncher();
        }
       

    }
    
    public static void start(final SplashFrame sframe) throws Exception
    {
        frame = new Frame();
        frame.panel.setAuthState();
        boolean Internet_connected = checkInternetConnection();
        readConfig(frame);
        if (Settings.use_loading_news)
        {
            Color c = ThemeElements.staticThemeColor;
            StreamUtils.loadNewsPage(getURLSc("jcr_news.php?color=rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")"));
        }
        
        if (GuardUtils.use_process_check)
        {
            if (GuardUtils.checkProcesses())
            {
                frame.elEnabled(false);
                frame.setAlert("Завершите сторонние процессы", 3, 0);
                reportErr("В системе обнаружены запрещенные запущенные процессы");
                return;
            }
            
            if (GuardUtils.use_process_check_timer)
            {
                new Timer(GuardUtils.time_for_process_check * 1000, new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                        
                        startMinecraft();
                        appletMinecraft();                        
                        
                        File smcguard  =  new File(BaseUtils.getClientDirectory() + File.separator + "bin" + File.separator + "minecraft1.jar");
                        File smcguard1  =  new File(BaseUtils.getClientDirectory() + File.separator + "bin" + File.separator + "forge1.jar");
                        File smcguard2 = new File(BaseUtils.getClientDirectory() + File.separator + "bin" + File.separator + "minecraft2.jar");
                        
                        if(smcguard.exists()){
                            if(smcguard.delete()){; 
                                Runtime.getRuntime().exit(1);
                            }
                        }
                        
                        if(smcguard1.exists()){
                            if(smcguard1.delete()){
                                Runtime.getRuntime().exit(1); 
                            }
                        }
                        
                        if(smcguard2.exists()){
                            if(smcguard2.delete()){ 
                                Runtime.getRuntime().exit(1); 
                            }
                        }
                        
                        if (GuardUtils.checkProcesses())
                        {
                            reportErr("В системе обнаружены запрещенные запущенные процессы");
                            reportErr("Завершение работы программы...");
                            System.exit(1);
                        }
                    }
                }).start();
            }
        }
        
        if (Settings.use_update_mon && Internet_connected)
        {
            frame.panel.waitIcon(true, 0);
            StreamUtils.getServerOnline();
        }
        
        SwingUtilities.invokeLater(new Runnable()
        { public void run() { if (sframe != null) sframe.dispose(); } });
        
        
        frame.show();
    }
    
    private void mouseListener()
    {
        if (getPlatform() == 0) return;
        
        this.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                setLocation(getX() + e.getX() - X, getY() + e.getY() - Y);
            }
        });
        this.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                X = e.getX();
                Y = e.getY();
            }
        });
    }
    
    public void elEnabled(boolean enable)
    {
        serversList.setEnabled(enable);
        login.setEnabled(enable);
        password.setEnabled(enable);
        doAuth.setEnabled(enable);
        take.setEnabled(enable);
        settings.setEnabled(enable);
//        exit.setEnabled(enable);
        upd_take.setEnabled(enable);
        set_cancel.setEnabled(enable);
        set_take.setEnabled(enable);
        set_remember.setEnabled(enable);
        set_update.setEnabled(enable);
        set_full.setEnabled(enable);
        set_offline.setEnabled(enable);
        set_memory.setEnabled(enable);
        login.setEnabled(enable);
        password.setEnabled(enable);
    }
    
    public void toXFrame(int type)
    {
        BufferedImage min = ImageUtils.takePicture(panel).getSubimage(0, 0, panel.getWidth(), panel.getHeight());
        panel.removeAll();
        Panel.animation.paneAttenuation(min, type);
        panel.hideAlerts(true);
        addFrameElements(false);
        repaint();
    }
    
    public void paneState(int goTo)
    {
        switch (goTo)
        {
            case 1: panel.setAuthState(); break;
            case 2: panel.setSettings(); break;
            case 3: panel.setUpdateState(); break;
            case 4: panel.setGameUpdateState(); break;
            case 5: panel.setPersonalState(); break;
            case 6: panel.setAlertState(); break;
        }
    }
    
    public void afterFilling(final int aF, boolean remove)
    {
        switch (aF)
        {
            case 1: addAuthElements(remove); break;
            case 2: addSettingsElements(remove); break;
            case 3: addUpdateElements(remove); break;
            case 4: break;
            case 5: addPersonalElements(remove); break;
            case 6: addAlertPaneElements(remove); break;
        }
    }
    
    protected final void addFrameElements(boolean remove)
    {
        if(!remove)
        {
            panel.add(turn);
            panel.add(close);
        } else
        {
            panel.remove(turn);
            panel.remove(close);
        }
    }
    
    protected final void addAuthElements(boolean remove)
    {
        if(!remove)
        {
            panel.add(logotype);
            panel.add(serversList);
            panel.add(doAuth);
            panel.add(settings);
            panel.add(login);
            panel.add(password);
        } else
        {
            panel.remove(logotype);
            panel.remove(serversList);
            panel.remove(doAuth);
            panel.remove(settings);
            panel.remove(login);
            panel.remove(password);
        }
    }
    
    protected void addSettingsElements(boolean remove)
    {
        if(!remove)
        {
            panel.add(set_cancel);
            panel.add(set_take);
            panel.add(set_remember);
            panel.add(set_update);
            panel.add(set_full);
            panel.add(set_offline);
            panel.add(set_memory);
        } else
        {
            panel.remove(set_cancel);
            panel.remove(set_take);
            panel.remove(set_remember);
            panel.remove(set_update);
            panel.remove(set_full);
            panel.remove(set_offline);
            panel.remove(set_memory);
        }
    }
    
    protected void addUpdateElements(boolean remove)
    {
        if(!remove)
        {
            panel.add(exit);
            panel.add(upd_take);
        } else
        {
            panel.remove(exit);
            panel.remove(upd_take);
        }
    }
    
    protected void addPersonalElements(boolean remove)
    {
        if(!remove)
        {
            panel.add(exit);
            panel.add(toGame);
        } else
        {
            panel.remove(exit);
            panel.remove(toGame);
        }
    }
    
    protected void addAlertPaneElements(boolean remove)
    {
        if(!remove)
        {
            panel.add(exit);
            panel.add(take);
        } else
        {
            panel.remove(exit);
            panel.remove(take);
        }
    }
    
    public void setAlert(String alert, int type, int in_frame)
    {
        panel.alertIcons(alert, type, in_frame);
    }
    
    public void setAlert(String alert, int type, int y, int in_frame)
    {
        panel.alertIcons(alert, type, y, in_frame);
    }
    
    public static void report(String mes)
    {
        if(Settings.use_debugging)
        {
            String num = null;
            if(Integer.toString(debLine).length() == 1) num = "00" + debLine + ": ";
            else if(Integer.toString(debLine).length() == 2) num = "0" + debLine + ": ";
            else if(Integer.toString(debLine).length() == 3) num = debLine + ": ";
            else num = "999: ";
            
            System.out.println((Starter.isStarted() ? "" : num) + mes);
            debLine++;
        }
    }
    
    public static void reportErr(String errMes)
    {
        if(Settings.use_debugging)
        {
            String num = null;
            if(Integer.toString(debLine).length() == 1) num = "00" + debLine + ": ";
            else if(Integer.toString(debLine).length() == 2) num = "0" + debLine + ": ";
            else if(Integer.toString(debLine).length() == 3) num = debLine + ": ";
            else num = "999: ";
            
            System.err.println((Starter.isStarted() ? "" : num) + errMes);
            debLine++;
        }
    }

    public void focusGained(FocusEvent e)
    {
        if(e.getSource() == login && login.getText().equals(Settings.login_text)) login.setText("");
        if(e.getSource() == password && new String(password.getPassword()).equals(Settings.pass_text)) password.setText("");
    }

    public void focusLost(FocusEvent e)
    {
        if(e.getSource() == login && login.getText().equals("")) login.setText(Settings.login_text);
        if(e.getSource() == password && new String(password.getPassword()).equals("")) password.setText(Settings.pass_text);
    }
    
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyText(e.getKeyCode()).equals("Enter"))
        {
            if (panel.unit == 0) doAuth.doClick();
            else if (panel.unit == 4) toGame.doClick();
        }
    }
}
