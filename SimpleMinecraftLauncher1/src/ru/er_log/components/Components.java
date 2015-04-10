package ru.er_log.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import ru.er_log.Settings;

public class Components {
    
    private int type = 0;
    public int x = 0;
    public int y = 0;
    public int width = 0;
    public int height = 0;
    public int curve = 0;
    public BufferedImage image;
    public String txt = "";
    
    public void drawImage(BufferedImage image, int x, int y, Color color, Graphics g)
    {
        this.type = 1;
        this.image = image;
        this.x = x;
        this.y = y;
        this.width  = image.getWidth();
        this.height = image.getHeight();
        this.paintComponent(g, color);
    }
    
    public void drawImage(BufferedImage image, int x, int y, int width, int height, Color color, Graphics g)
    {
        this.type = 1;
        this.image = image;
        this.x = x;
        this.y = y;
        this.width  = width;
        this.height = height;
        this.paintComponent(g, color);
    }
    
    public void drawFillRoundRect(int x, int y, int width, int height, int curve, Color color, Graphics g)
    {
        this.type = 2;
        this.x = x;
        this.y = y;
        this.width  = width;
        this.height = height;
        this.curve = curve;
        this.paintComponent(g, color);
    }
    
    public void paintComponent(Graphics g, Color color)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if(type == 1) g2d.drawImage(image, x, y, width, height, null);
        if(type == 2) g2d.fillRoundRect(x, y, width, height, curve, curve);
        
        if(Settings.draw_borders)
        {
            g2d.setColor(color);
            g2d.drawRect(x, y, width - 1, height - 1);
        } g2d.dispose();
    }
}
