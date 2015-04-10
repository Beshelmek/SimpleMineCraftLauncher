package ru.er_log.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import ru.er_log.Settings;
import ru.er_log.utils.ImageUtils;
import ru.er_log.utils.StyleUtils;

public class Button extends JButton {
    
    public int type = 0;
    public int x = 0;
    public int y = 0;
    public int width = 0;
    public int height = 0;
    
    public BufferedImage defImg, rolImg, preImg;
    
    public static BufferedImage def_button_1 = Panel.button.getSubimage(0, 0, 112, 36);
    public static BufferedImage rol_button_1 = Panel.button.getSubimage(0, 36, 112, 36);
    public static BufferedImage pre_button_1 = Panel.button.getSubimage(0, 72, 112, 36);
    public static BufferedImage loc_button_1 = Panel.button.getSubimage(0, 108, 112, 36);
    
    public static BufferedImage def_button_2 = Panel.button.getSubimage(112, 0, 112, 36);
    public static BufferedImage rol_button_2 = Panel.button.getSubimage(112, 36, 112, 36);
    public static BufferedImage pre_button_2 = Panel.button.getSubimage(112, 72, 112, 36);
    public static BufferedImage loc_button_2 = Panel.button.getSubimage(112, 108, 112, 36);
    
    public Button(String title, int type, int x, int y, int width, int height)
    {
        setText(title);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setFocusable(false);
        setForeground(Color.decode(Settings.elements_text_color));
        setFont(StyleUtils.getFont(13, 1));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBounds(x, y, width, height);
        
        this.type = type;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
    
    public Button(ImageUtils img, String name)
    {
        setText(name);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setFocusable(false);
        setForeground(Color.decode(Settings.elements_text_color));
        setFont(StyleUtils.getFont(13, 1));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBounds(img.x, img.y, img.width, img.height);
        
        this.width  = img.width;
        this.height = img.height;
        this.x = img.x;
        this.y = img.y;
        this.defImg = img.defImg;
        this.rolImg = img.rolImg;
        this.preImg = img.preImg;
    }
    
    public void paintComponent(Graphics g)
    {
        ButtonModel butModel = getModel();
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (defImg != null && rolImg != null && preImg != null)
        {
            if (butModel.isRollover() && butModel.isEnabled())
            {
                if (butModel.isPressed()) g2d.drawImage(preImg, 0, 0, width, height, null);
                else g2d.drawImage(rolImg, 0, 0, width, height, null);
            } else g2d.drawImage(defImg, 0, 0, width, height, null);
        } else if (this.type == 1)
        {
            if (!butModel.isEnabled()) g2d.drawImage(loc_button_1, 0, 0, width, height, null);
            else if (butModel.isRollover())
            {
                if (butModel.isPressed()) g2d.drawImage(pre_button_1, 0, 0, width, height, null);
                else g2d.drawImage(rol_button_1, 0, 0, width, height, null);
            } else g2d.drawImage(def_button_1, 0, 0, width, height, null);
        } else if (this.type == 2)
        {
            if (!butModel.isEnabled()) g2d.drawImage(loc_button_2, 0, 0, width, height, null);
            else if (butModel.isRollover())
            {
                if (butModel.isPressed()) g2d.drawImage(pre_button_2, 0, 0, width, height, null);
                else g2d.drawImage(rol_button_2, 0, 0, width, height, null);
            } else g2d.drawImage(def_button_2, 0, 0, width, height, null);
        }
        
        if(Settings.draw_borders)
        {
            g2d.setColor(Color.BLUE);
            g2d.drawRect(0, 0, width - 1, height - 1);
        }
        
        g2d.dispose();
        super.paintComponent(g);
    }
}
