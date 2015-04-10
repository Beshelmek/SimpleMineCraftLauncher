package ru.er_log.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import ru.er_log.Settings;
import ru.er_log.utils.StyleUtils;

public class STextField extends JTextField {
    
    public Panel pb;
    public static BufferedImage static_image = Panel.fieldBack.getSubimage(0, 0, 48, 18);
    public static BufferedImage focus_image = Panel.fieldBack.getSubimage(0, 18, 48, 18);
    public int width = 0;
    public int height = 0;
    public int x = 0;
    public int y = 0;
    
    public Color staticColor;
    public Color disableColor;
    
    public STextField(int x, int y, int width, int height, Color color)
    {       
	setOpaque(false);
	setBorder(null);
	setCaretColor(color);
	setForeground(color);
        setSelectionColor(new Color(51, 153, 255));
        setSelectedTextColor(Color.WHITE);
        setHorizontalAlignment(LEFT);
        setFont(StyleUtils.getFont(14, 1));
        setBounds(x+16, y, width-32, height);
        
        this.width = width - 32;
        this.height = height;
        this.x = x;
        this.y = y;
    }
    
    public STextField(int x, int y, int width, int height, String str, final int maxlength, Color disableColor, Color staticColor)
    {
	setOpaque(false);
	setBorder(null);
	setCaretColor(staticColor);
	if (this.isFocusOwner()) setForeground(staticColor);
        else setForeground(disableColor);
        setSelectionColor(new Color(51, 153, 255));
        setSelectedTextColor(Color.WHITE);
        setHorizontalAlignment(CENTER);
        setFont(StyleUtils.getFont(12, 1));
        setBounds(x, y, width, height);
        
        setDocument(new PlainDocument()
        {
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
            {
                if (str == null) return;
                if ((getLength() + str.length()) <= maxlength) super.insertString(offset, str, attr);
            }
        }); setText(str);
        
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        
        this.disableColor = disableColor;
        this.staticColor = staticColor;
    }
    
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if(static_image != null && focus_image != null)
        {
            if (this.isFocusOwner())
            {
                g2d.drawImage(focus_image, 0, 0, null);
                repaint();
            } else
            {
                g2d.drawImage(static_image, 0, 0, null);
            }
        }
        
        if (this.isFocusOwner()) setForeground(staticColor);
        else setForeground(disableColor);
        
        if(Settings.draw_borders)
        {
            g2d.setColor(Color.GRAY);
            g2d.drawRect(0, 0, width - 1, height - 1);
        }
        
        g2d.dispose();
        super.paintComponent(g);
    }
}
