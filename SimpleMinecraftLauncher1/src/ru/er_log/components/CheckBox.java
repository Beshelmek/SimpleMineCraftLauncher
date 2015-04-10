package ru.er_log.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import ru.er_log.Settings;
import ru.er_log.utils.StyleUtils;

public class CheckBox extends JCheckBox {
    
    public BufferedImage imageOn, imageOff;
    public int width = 0;
    public int height = 0;
    public int x = 0;
    public int y = 0;
    
    public CheckBox(int x, int y, int width, int height, String name, boolean selected)
    {
        super(name);
        setOpaque(false);
        setFocusable(false);
        setSelected(selected);
        setForeground(Color.decode(Settings.fields_static_color));
        setFont(StyleUtils.getFont(13, 1));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBounds(x, y, width, height);
        
        this.width = width;
        this.height = height;
        
        setIcon(new ImageIcon(Panel.checkbox.getSubimage(0, 0, 16, 16)));
        setRolloverIcon(new ImageIcon(Panel.checkbox.getSubimage(32, 0, 16, 16)));
        setSelectedIcon(new ImageIcon(Panel.checkbox.getSubimage(16, 0, 16, 16)));
        setRolloverSelectedIcon(new ImageIcon(Panel.checkbox.getSubimage(48, 0, 16, 16)));
    }
    
    protected void paintComponent(Graphics g)
    {
        if (Settings.draw_borders)
        {
            g.setColor(Color.MAGENTA);
            g.drawRect(0, 0, width - 1, height - 1);
        }
        super.paintComponent(g);
    }
}
