package ru.er_log.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

import ru.er_log.Settings;
import static ru.er_log.utils.BaseUtils.servers;
import ru.er_log.utils.ImageUtils;
import ru.er_log.utils.StyleUtils;

public class ComboBox extends JComponent implements MouseListener, MouseMotionListener {
    
    public final String[] elements;
    
    public int axisY = 0;
    public boolean selectValue = false;
    public boolean error = false;
    
    private final int paneY = 6;
    private boolean entered = false;
    private boolean pressed = false;
    private int selected = 0;
    private int y = 0;
    
    public static final BufferedImage def_line = Panel.combobox.getSubimage(0, 0, 232, 19);
    public static final BufferedImage rol_line = Panel.combobox.getSubimage(0, 25, 232, 19);
    public static final BufferedImage pre_line = Panel.combobox.getSubimage(0, 50, 232, 19);
    public static final BufferedImage def_pane = Panel.combobox.getSubimage(0, 75, 232, 70);
        
    public ComboBox(String[] elements, int y, Frame frame)
    {
        try
        {
            if (!frame.set_offline.isSelected())
            for (int i = 0; i < elements.length; i++)
                elements[i] = servers[i].split(" :: ")[3] + ": " + elements[i];
        } catch (Exception e) {}
        
        this.elements = elements;
        this.axisY = y;
        
        if (frame != null && this.elements.length > 7)
        {
            int new_width = Settings.frame_width;
            int new_height = Settings.frame_height + ((this.elements.length - 7) * 15);
            frame.setSize(new_width, new_height);
            Frame.report("Размеры окна программы были автоматически изменены: " + new_width + " x "+ new_height);
        }
        
        setForeground(Color.decode(Settings.elements_text_color));
        setFont(StyleUtils.getFont(12, 1));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBounds(57, 324, 232, 19);
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        
        if (elements.length == 2 && elements[1].equals("error"))
        {
            error = true;
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        
        if (pressed && !error)
        {
            g2d.drawImage(pre_line, 0, 0, w, pre_line.getHeight(), null);
            
            int righth = pre_line.getHeight() + (elements.length * 8) + (elements.length * 7 - 7) + (paneY * 3) - 2; // Высота выпадающей панели с серверами
            int righty = axisY;
            
            if (getY() != righty || getHeight() != righth)
            {
                setLocation(getX(), righty);
                setSize(getWidth(), righth);
                return;
            }
            
            int cut_size = (elements.length == 1 ? 8 : 10);
            BufferedImage d_pane = ImageUtils.splitImage(cut_size, cut_size, (elements.length * 8) + (elements.length * 7 - 7) + (paneY * 2) - 2, def_pane);
            g2d.drawImage(d_pane, 0, pre_line.getHeight() + paneY, null);
            g2d.setComposite ( AlphaComposite.Src);
            
            for (int i = 0; i < elements.length; i++)
            {
                g2d.drawString(elements[i], 7, pre_line.getHeight() * (i + 1) + pre_line.getHeight() - (i * 4));
            }
            
            g2d.drawString(elements[selected], 7, (def_line.getHeight() - (g2d.getFontMetrics().getHeight() / 2)) + 1);
        } else if (entered)
        {
            int righth = rol_line.getHeight();
            if (getY() != axisY || getHeight() != righth)
            {
                setLocation(getX(), axisY);
                setSize(getWidth(), righth);
                return;
            }
            g2d.drawImage(rol_line, 0, 0, w, rol_line.getHeight(), null);
            g2d.drawString(elements[selected], 7, (rol_line.getHeight() - (g2d.getFontMetrics().getHeight() / 2)) + 1);
        } else
        {
            int righth = def_line.getHeight();
            if (getY() != axisY || getHeight() != righth)
            {
                setLocation(getX(), axisY);
                setSize(getWidth(), righth);
                return;
            }
            g2d.drawImage(def_line, 0, 0, w, def_line.getHeight(), null);
            g2d.drawString(elements[selected], 7, (def_line.getHeight() - (g2d.getFontMetrics().getHeight() / 2)) + 1);
        }
        
        if (Settings.draw_borders)
        {
            g2d.setColor(Color.GREEN);
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
        
        g2d.dispose();
    }
    
    public void mouseClicked(MouseEvent e)
    {
        boolean yTrue = false;
        
        if(y > pre_line.getHeight() + paneY)
        {
            y = (y - (pre_line.getHeight() + paneY + 2)) / 15;
            yTrue = true;
            selectValue = true;
        } else selectValue = false;
        
        if (pressed && yTrue && y < elements.length)
        {
            selected = y;
            Frame.frame.panel.resetAlerts();
        }

        pressed = !pressed;
        repaint();
    }
    
    public void mouseMoved(MouseEvent e)
    {
        y = e.getY();
        repaint();
    }
    
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) { entered = true; repaint(); }
    public void mouseExited(MouseEvent e) { entered = false; repaint(); }
    
    public void setSelectedIndex(int num)
    {
        selected = num;
    }
    
    public int getSelectedIndex()
    {
        return selected;
    }
    
    public boolean getSelectValue()
    {
        return selectValue;
    }
    
    public boolean getPressed()
    {
        return pressed;
    }
}