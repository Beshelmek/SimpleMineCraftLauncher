package ru.er_log.components;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;
import ru.er_log.Settings;

import ru.er_log.utils.BaseUtils;
import ru.er_log.utils.StyleUtils;
import static ru.er_log.utils.ImageUtils.*;

public class Panel extends JPanel {
    
    public static Components compt;
    public static Animation animation;
    
    public int unit = 0;
    public Timer timer;
    public int tmpInt = 0;
    public String tmpString = "";
    public boolean tmpBool = false;
    public BufferedImage tmpImage;
    
    public static boolean dispNews = false;
    public boolean waitIconBool, drawAlerts;
    private boolean drawAlertIcon = true;
    
    public int waitY = 367, alertY = 376;
    private int waitState = 0, alertIconInt, draw_in_frame;
    public static int news_xcoord;
    
    public static BufferedImage favicon, background, logotype, authFields,
            sysButs, button, combobox, checkbox, fieldBack, progBarImage,
            modalBack, news_back, pressBorder, waitIcon, alertIcons, bandColors, def_skin;
    
    public Panel()
    {
        compt = new Components();
        animation = new Animation();
        this.setOpaque(false);
        this.setLayout(null);
        this.setDoubleBuffered(true);
        this.setBorder(null);
        this.setFocusable(true);
    }

    public void paintComponent(final Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = Settings.frame_width, h = Settings.frame_height;
        
        if (dispNews)
        {
            g2d.drawImage(news_back, Settings.frame_width - news_xcoord, 0, null);
            if (Settings.draw_borders && news_xcoord == 0)
            {
                g2d.setColor(Color.ORANGE);
                g2d.drawRect(Settings.frame_width - news_xcoord, 0, news_back.getWidth() - 1, h - 1);
                g2d.drawRect(Settings.frame_width + 25, 10 + 35, news_back.getWidth() - 25 * 2, 430 - 35 * 2);
            }
        }
        
        g2d.drawImage(background, 0, 0, w, h, null);
        
        if (unit == 0)
        {   

            /*g2d.setFont(StyleUtils.getFont(12, 1));
            g2d.setColor(Color.gray);
            g2d.drawString("Голосуйте за нас и получайте по 35000 эконов!", 53, 150);
            g2d.drawString("Желаем вам приятной игры!", 95, 163);*/
            
            drawInscription(w, g2d);
            
            if (Frame.frame.login.isFocusOwner()) { drawCenteredImage(LoginField.img_focus, w, 179, Color.RED, g2d); repaint(); }
            else drawCenteredImage(LoginField.img_static, w, 179, Color.RED, g2d);
            if (Frame.frame.password.isFocusOwner()) { drawCenteredImage(PassField.img_focus, w, 223, Color.RED, g2d); repaint(); }
            else drawCenteredImage(PassField.img_static, w, 223, Color.RED, g2d);
        } else if (unit == 1)
        {
            drawTitle("Обновление", w, g2d);
            drawInscription(w, g2d);
            
            g2d.setColor(new Color(52, 73, 94, toValue(5)));
            drawCenteredImage(splitImage(232, 139, modalBack), w, 179, Color.BLACK, g2d);

            g2d.setColor(Color.decode(Settings.panel_text_color));
            g2d.setFont(StyleUtils.getFont(13, 1));

            g2d.drawString("Пожалуйста, обновите программу", 66, 202);
            g2d.drawString("до более новой версии.", 66, 217);

            g2d.drawString("Обновление содержит исправления", 66, 245);
            g2d.drawString("и улучшения, необходимые для более", 66, 260);
            g2d.drawString("удобной игры на наших серверах.", 66, 275);

            g2d.drawString("Для обновления нажмите «Обновить»", 66, 303);
        } else if (unit == 2)
        {
            int leftTime = 0;
            try { leftTime = (int) ((BaseUtils.gameUpdater.totalsize - BaseUtils.gameUpdater.currentsize) / (BaseUtils.gameUpdater.downloadspeed * 1024)); }
            catch (Exception e) {}
            
            drawTitle("Обновление", w, g2d);
            drawInscription(w, g2d);
            
            g2d.setColor(new Color(52, 73, 94, toValue(5)));
            drawCenteredImage(splitImage(232, 139, modalBack), w, 179, Color.BLACK, g2d);

            g2d.setColor(Color.decode(Settings.panel_text_color));
            g2d.setFont(StyleUtils.getFont(13, 1));
            
            g2d.drawString("Файл: " + BaseUtils.gameUpdater.filename, 66, 202);
            g2d.drawString("Скорость: " + BaseUtils.gameUpdater.downloadspeed + " kB/s", 66, 217);
            g2d.drawString("Состояние: " + BaseUtils.gameUpdater.state, 66, 232);
            
            g2d.drawString("Размер обновления: " + BaseUtils.gameUpdater.totalsize / 1024 + " kB", 66, 260);
            g2d.drawString("Загружено: " + BaseUtils.gameUpdater.currentsize / 1024 + " kB", 66, 275);
            
            g2d.drawString("До завершения: " + leftTime + " секунд", 66, 303);
            
            g2d.drawImage(splitImage(0, 232, 24, 26, 100, progBarImage), 57, 324, 232, 26, null);
            
            g2d.setColor(new Color(245, 245, 245));
            g2d.setFont(StyleUtils.getFont(12, 1));
            BufferedImage img = splitImage(24, 232, 24, 26, BaseUtils.gameUpdater.percents, progBarImage);         	
            try
            {
                int percent = (int) (BaseUtils.gameUpdater.percents * 232 / 100);
                compt.drawImage(img.getSubimage(0, 0, percent, 26), 57, 324, percent, 26, Color.WHITE, g2d);
                g2d.drawString(BaseUtils.gameUpdater.percents + "%", 251, 341);
            } catch (Exception e) {}
        } else if (unit == 3)
        {
            drawTitle("Настройки", w, g2d);
            drawInscription(w, g2d);
            
            drawCenteredImage(splitImage(232, 139, modalBack), w, 179, Color.BLACK, g2d);
            
            g2d.setColor(Color.decode(Settings.fields_static_color));
            g2d.setFont(StyleUtils.getFont(13, 1));
            g2d.drawString("- выделено памяти (mB)", 125, 305);
        } else if (unit == 4)
        {
            PersonalCab.draw(g2d);
        } else if (unit == 5)
        {
            drawTitle("Оповещение", w, g2d);
            drawInscription(w, g2d);
            
            g2d.setColor(new Color(52, 73, 94, toValue(5)));
            drawCenteredImage(splitImage(232, 139, modalBack), w, 179, Color.BLACK, g2d);

            g2d.setColor(Color.decode(Settings.panel_text_color));
            g2d.setFont(StyleUtils.getFont(13, 1));
            
            g2d.drawString("Уважаемый игрок, внимание!", 66, 202);
            g2d.drawString("На сервере включена превязка по", 66, 217);
            g2d.drawString("устройству.", 66, 232);

            g2d.drawString("Вы не сможете войти на сервер под", 66, 260);
            g2d.drawString("данным ником с другого компьютера,", 66, 275);
            g2d.drawString("так как Ваш ник привязан к этому", 66, 290);
            g2d.drawString("устройству. Это сделанно для", 66, 305);
            g2d.drawString("безопасности вашего аккаунта.", 66, 305);
        }
        
        
        try { g2d.drawImage(tmpImage, 0, 0, null); } catch (Exception e) {}
        if (Settings.use_animation) animation.paint(g2d);
        if (waitIconBool && unit == draw_in_frame) compt.drawImage(waitIcon.getSubimage(waitState, 0, 36, 12), 154, waitY, Color.PINK, g2d);
        if (drawAlerts && unit == draw_in_frame)
        {
            g2d.setColor(Color.decode(Settings.alert_color));
            g2d.setFont(StyleUtils.getFont(14, 1));
            drawCenteredString(tmpString, w, alertY, null, g2d);
            if (drawAlertIcon)
                compt.drawImage(alertIcons.getSubimage(alertIconInt, 0, 13, 13), ((w - g2d.getFontMetrics().stringWidth(tmpString)) / 2) - 19, alertY - 12, Color.PINK, g2d);
        }
        
        if (Settings.draw_borders)
        {
            g2d.setColor(Color.BLACK);
            g2d.drawRect(0, 0, w - 1, h - 1);
        }
    }
    
    public void setAuthState() { unit = 0; }
    public void setSettings() { unit = 3; }
    public void setUpdateState() { unit = 1; }
    public void setPersonalState() { unit = 4; if (Settings.use_loading_news) openNewsPane(); }
    public void setAlertState() { unit = 5; }
    public void setGameUpdateState()
    {
        unit = 2;
        timer = new Timer(300, new ActionListener()
        {
            public void actionPerformed(ActionEvent e) { repaint(); }
        }); timer.start();
    }
    
    private void drawTitle(String str, int width, Graphics2D g2d)
    {
        g2d.setFont(StyleUtils.getFont(36, 2));
        g2d.setColor(Color.decode(Settings.panel_title_color));
        drawCenteredString(str, width, 117, null, g2d);
    }
    
    private void drawInscription(int width, Graphics2D g2d)
    {
        int size = new Integer(Settings.string_under_logo[1]);
        g2d.setFont(StyleUtils.getFont(size, new Integer(Settings.string_under_logo[2].replace("#", ""))));
        g2d.setColor(Color.decode(Settings.string_under_logo[3]));
        drawCenteredString(Settings.string_under_logo[0], width, 147, null, g2d);
    }
    
    public static void drawCenteredString(String s, int width, int y, Color color, Graphics2D g2d)
    {
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(s)) / 2;

        g2d.drawString(s, x, y);
        if (Settings.draw_borders)
        {
            g2d.setColor(color);
            g2d.drawRect(x, (int) (y - fm.getHeight() / 1.35F), fm.stringWidth(s) - 1, fm.getHeight() - 1);
        }
    }
    
    public static void drawCenteredImage(BufferedImage img, int pwidth, int y, Color color, Graphics2D g2d)
    {
        drawCenteredImage(img, pwidth, y, img.getWidth(), img.getHeight(), color, g2d);
    }
    
    public static void drawCenteredImage(BufferedImage img, int pwidth, int y, int width, int height, Color color, Graphics2D g2d)
    {
        int x = (pwidth - width) / 2;
        g2d.drawImage(img, x, y, null);
        
        if(Settings.draw_borders)
        {
            g2d.setColor(color);
            g2d.drawRect(x, y, width - 1, height - 1);
        }
    }
    
    public static void drawAlignedImage(BufferedImage img, int width, int height, Color color, Graphics2D g2d)
    {
        int x = (width - img.getWidth()) / 2;
        int y = (height - img.getHeight()) / 2;
        g2d.drawImage(img, x, y, null);
        
        if(Settings.draw_borders)
        {
            g2d.setColor(color);
            g2d.drawRect(x, y, img.getWidth() - 1, img.getHeight() - 1);
        }
    }
    
    public static int toValue(int percent)
    {
        return (int) (255F / 100F * percent);
    }
    
    public void openNewsPane()
    {
        Frame.frame.exit.setEnabled(false);
        Frame.frame.toGame.setEnabled(false);
        
        if (BaseUtils.getPlatform() != 0)
            Frame.frame.setSize(Settings.frame_width + news_back.getWidth(), Settings.frame_height);
        else
            Frame.frame.setSize(Settings.frame_width + news_back.getWidth() + Settings.linux_frame_w, Settings.frame_height + Settings.linux_frame_h);
        
        Frame.frame.repaint();
        
        news_xcoord = news_back.getWidth(); dispNews = true;
        timer = new Timer(Settings.news_drawing_time, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (news_xcoord == 0)
                {
                    timer.stop();
                    Frame.frame.newsScPane.setViewportView(Frame.frame.news_pane);
                    add(Frame.frame.newsScPane);
                    Frame.frame.exit.setEnabled(true);
                    Frame.frame.toGame.setEnabled(true);
                    repaint();
                } else { news_xcoord -= Settings.news_offset_panel; repaint(); }
            }
        }); timer.start();
    }
    
    public void waitIcon(boolean working, int in_frame)
    {
        resetTimer();
        if (working)
        {
            draw_in_frame = in_frame;
            waitIconBool = true;
            timer = new Timer(450, new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (waitState == 72) waitState = 0;
                    else if (waitState == 36 && waitState != 0 && waitState != 72) waitState = 72;
                    else if (waitState == 0) waitState = 36;
                    repaint();
                }
            }); timer.start();
        } else
        {
            waitIconBool = false;
            repaint();
        }
    }

    public void waitIcon(boolean working, int y, int in_frame)
    {
        draw_in_frame = in_frame;
        this.waitY = y;
        waitIcon(working, in_frame);
    }
    
    public void resetTimer()
    {
        if (timer != null) timer.stop();
        timer = null;
    }
    
    public void alertIcons(String alert, int type, int in_frame)
    {
        drawAlerts = drawAlertIcon = true;
        tmpString = alert;
        draw_in_frame = in_frame;
        if (type == 0) drawAlertIcon = false;
        else if (type == 1) alertIconInt = 0;
        else if (type == 2) alertIconInt = 13;
        else if (type == 3) alertIconInt = 26;
        repaint();
    }

    public void alertIcons(String alert, int type, int y, int in_frame)
    {
        drawAlerts = drawAlertIcon = true;
        tmpString = alert; alertY = y;
        draw_in_frame = in_frame;
        if (type == 0) drawAlertIcon = false;
        else if (type == 1) alertIconInt = 0;
        else if (type == 2) alertIconInt = 13;
        else if (type == 3) alertIconInt = 26;
        repaint();
    }

    public void hideAlerts(boolean hide)
    {
        if (hide) drawAlerts = false;
        else if (!hide && !tmpString.equals("")) drawAlerts = true;
        repaint();
    }

    public void resetAlerts()
    {
        waitIcon(false, -1);
        drawAlerts = false;
        drawAlertIcon = true;
        tmpString = "";
    }
}
