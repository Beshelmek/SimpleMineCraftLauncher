package ru.er_log.splash;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import ru.er_log.Settings;
import ru.er_log.utils.BaseUtils;
import ru.er_log.utils.StyleUtils;

public class SplashPanel extends JPanel {
    
    public static String status = "";
    public static final BufferedImage splashBack = BaseUtils.openImage(BaseUtils.getTheme().themeSplashBack());
    
    public SplashPanel()
    {
        this.setLayout(null);
        this.setFocusable(false);
    }
    
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.drawImage(splashBack, 0, 0, getWidth(), getHeight(), null);
        
        g2d.setColor(Color.decode(Settings.panel_text_color));
        g2d.setFont(StyleUtils.getFont(12, 1));
        g2d.drawString(status, Settings.splash_X_align, Settings.splash_Y_align);
    }
}
