package ru.er_log.components;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Timer;
import ru.er_log.Settings;
import ru.er_log.utils.ImageUtils;

public class Animation {
    
    private int tmpInt;
    private boolean tmpBool;
    private Timer timer, timer2;
    private static int anSpeed = Settings.animation_speed, aF = 0;
    private final float onePercent = 100F / anSpeed;
    private static boolean fillEffect = false, increase = true;
    
    public void paint(Graphics2D g2d)
    {
        if(fillEffect && !tmpBool)
        {
            tmpBool = true;
            if(increase)
            {
                tmpInt = 0; final int time = anSpeed * 2; anSpeed = 0;
                timer = new Timer(20, new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                        tmpInt++;
                        if (tmpInt > time) { timer.stop(); fillEffect = false; return; }
                        anSpeed++; Frame.frame.panel.repaint();
                    }
                }); timer.start();
            } else if(!increase)
            {
                tmpInt = 0; final int time = anSpeed;
                timer = new Timer(20, new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                        tmpInt++;
                        if (tmpInt > time)
                        {
                            timer.stop();
                            fillEffect = false;
                            Frame.frame.afterFilling(aF, false);
                            Frame.frame.elEnabled(true);
                            Frame.frame.panel.tmpImage = null;
                            Frame.frame.panel.repaint();
                            return;
                        } anSpeed--; Frame.frame.panel.repaint();
                    }
                }); timer.start();
            }
        }

        if (fillEffect)
            g2d.drawImage(bandColors((int) (onePercent * anSpeed)), 0, 0, Settings.frame_width, Settings.frame_height, null);
    }
    
    public void paneAttenuation(final BufferedImage min, final int goTo)
    {
        if(!Settings.use_animation) { Frame.frame.paneState(goTo); Frame.frame.afterFilling(goTo, false); return; }
        resetAnimation();
        Frame.frame.panel.tmpImage = min; aF = goTo;
        final int time = anSpeed * 2 + 5;
        animate(true, true); Frame.frame.elEnabled(false);
        timer2 = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                tmpInt++;
                if (tmpInt > time)
                {
                    timer2.stop();
                    animate(false, false);
                    processingPanel();
                    animate(true, false);
                }
            }
        }); timer2.start();
    }
    
    private void animate(boolean state, boolean increase)
    {
        this.tmpBool = false;
        this.increase = increase;
        this.fillEffect = state;
        Frame.frame.panel.repaint();
    }
    
    private void processingPanel()
    {
        Frame.frame.paneState(aF);
        Frame.frame.afterFilling(aF, false);
        Frame.frame.panel.tmpImage = null;
        Frame.frame.panel.tmpImage = ImageUtils.takePicture(Frame.frame.panel).getSubimage(0, 0, Frame.frame.panel.getWidth(), Frame.frame.panel.getHeight());
        Frame.frame.afterFilling(aF, true);
    }
    
    private void resetAnimation()
    {
        resetTimers();
        increase = true;
        Frame.frame.panel.tmpImage = null;
        anSpeed = Settings.animation_speed;
        tmpInt = 0;
    }
    
    public BufferedImage bandColors(int num)
    {
        BufferedImage img = Panel.bandColors;
        if (num < 5) return img.getSubimage(0, 0, 1, 1);
        else if (num < 10) return img.getSubimage(1, 0, 1, 1);
        else if (num < 15) return img.getSubimage(2, 0, 1, 1);
        else if (num < 20) return img.getSubimage(3, 0, 1, 1);
        else if (num < 25) return img.getSubimage(4, 0, 1, 1);
        else if (num < 30) return img.getSubimage(5, 0, 1, 1);
        else if (num < 35) return img.getSubimage(6, 0, 1, 1);
        else if (num < 40) return img.getSubimage(7, 0, 1, 1);
        else if (num < 45) return img.getSubimage(8, 0, 1, 1);
        else if (num < 50) return img.getSubimage(9, 0, 1, 1);
        else if (num < 55) return img.getSubimage(10, 0, 1, 1);
        else if (num < 60) return img.getSubimage(11, 0, 1, 1);
        else if (num < 65) return img.getSubimage(12, 0, 1, 1);
        else if (num < 70) return img.getSubimage(13, 0, 1, 1);
        else if (num < 75) return img.getSubimage(14, 0, 1, 1);
        else if (num < 80) return img.getSubimage(15, 0, 1, 1);
        else if (num < 85) return img.getSubimage(16, 0, 1, 1);
        else if (num < 90) return img.getSubimage(17, 0, 1, 1);
        else if (num < 95) return img.getSubimage(18, 0, 1, 1);
        else if (num >= 95) return img.getSubimage(19, 0, 1, 1);
        else return null;
    }
    
    public void resetTimers()
    {
        if (timer != null) timer.stop();
        if (timer2 != null) timer2.stop();
        timer = timer2 = null;
    }
}
