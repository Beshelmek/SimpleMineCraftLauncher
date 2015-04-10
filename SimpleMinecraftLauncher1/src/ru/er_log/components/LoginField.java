package ru.er_log.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JTextField;
import ru.er_log.Settings;
import ru.er_log.utils.StyleUtils;

public class LoginField extends JTextField {
    
    public Panel pb;
    public static final BufferedImage img_static = Panel.authFields.getSubimage(0, 0, 232, 36);
    public static final BufferedImage img_focus = Panel.authFields.getSubimage(0, 72, 232, 36);
    public int width = 0;
    public int height = 0;
    public int x = 0;
    public int y = 0;
    
    public Color staticColor;
    public Color disableColor;
    
    public LoginField(int x, int y, int width, int height, Color disableColor, Color staticColor)
    {       
	setOpaque(false);
	setBorder(null);
	setCaretColor(staticColor);
        setSelectionColor(new Color(51, 153, 255));
        setSelectedTextColor(Color.WHITE);
        setHorizontalAlignment(LEFT);
        setFont(StyleUtils.getFont(14, 1));
        setBounds(x + 13, y, width - 26, height);
        
        this.width = width - 26;
        this.height = height;
        this.x = x;
        this.y = y;
        
        this.disableColor = disableColor;
        this.staticColor = staticColor;
    }
    
//    public LoginField(int x, int y, int width, int height, String str, final int maxlength, Color color)
//    {
//	setOpaque(false);
//	setBorder(null);
//	setCaretColor(color);
//	setForeground(color);
//        setSelectionColor(new Color(51, 153, 255));
//        setSelectedTextColor(Color.WHITE);
//        setHorizontalAlignment(CENTER);
//        setFont(StyleUtils.getFont(12, 1));
//        setBounds(x, y, width, height);
//        
//        setDocument(new PlainDocument()
//        {
//            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
//            {
//                if (str == null) return;
//                if ((getLength() + str.length()) <= maxlength) super.insertString(offset, str, attr);
//            }
//        }); setText(str);
//        
//        this.image = BaseUtils.fieldBack;
//        this.width = width;
//        this.height = height;
//        this.x = x;
//        this.y = y;
//    }
    
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
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
