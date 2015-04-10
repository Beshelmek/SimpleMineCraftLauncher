package ru.er_log.splash;

import com.sun.awt.AWTUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import ru.er_log.Settings;
import static ru.er_log.components.startMinecraft.appletMinecraft;
import ru.er_log.utils.BaseUtils;

public class SplashFrame extends JFrame {

    private final SplashPanel panel = new SplashPanel();
    
    public SplashFrame()
    {
        this.setUndecorated(true);
        if (BaseUtils.getPlatform() != 0) AWTUtilities.setWindowOpaque(this, false);
        this.setPreferredSize(new Dimension(Settings.splash_width, Settings.splash_height));
        this.setSize(this.getPreferredSize());
        this.setTitle(Settings.title + " :: " + Settings.version);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setIconImage(BaseUtils.openImage(BaseUtils.getTheme().themeFavicon()));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        
        this.setContentPane(panel);
    }
    
    public void setStatus(final String status)
    {
        SplashPanel.status = status;
        panel.repaint();
    }
}