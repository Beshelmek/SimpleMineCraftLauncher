package ru.er_log.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import ru.er_log.Settings;
import ru.er_log.utils.BaseUtils;
import ru.er_log.utils.ImageUtils;
import static ru.er_log.utils.ImageUtils.*;

public class PersonalCab {
    
    public static BufferedImage skinImage;
    public static BufferedImage cloakImage;
    
    public static void draw(Graphics2D g2d)
    {
        g2d.drawImage(splitImage(104, 189, Panel.pressBorder), 65, 65, null);
        g2d.drawImage(splitImage(74, 104, Panel.pressBorder), 207, 65, null);

        g2d.drawImage(assembleSkin(skinImage, 5), 77, 77, null);
        g2d.drawImage(assembleCloak(cloakImage, 5), 219, 77, null);
        
        if (Settings.draw_borders)
        {
            g2d.drawRect(77, 77, 80, 165);
            g2d.drawRect(219, 77, 50, 80);
        }
    }
    
    public static void setImages(String[] data)
    {
        try
        {
            String skins_url = (data[11].startsWith("http:") | data[11].startsWith("https:")) ? data[11] : BaseUtils.getURLFi("skins/") + data[11];
            String cloaks_url = (data[11].startsWith("http:") | data[11].startsWith("https:")) ? data[12] : BaseUtils.getURLFi("cloaks/") + data[12];
            
            skinImage  = ImageUtils.getImageByURL(new URL(skins_url), false);
            cloakImage = ImageUtils.getImageByURL(new URL(cloaks_url), false);
        } catch (MalformedURLException e) {}
    }
    
    public static BufferedImage assembleSkin(BufferedImage img, final int xN)
    {
        if (img == null) img = Panel.def_skin;
        
        int w = img.getWidth() / 64, h = img.getHeight() / 32;
        BufferedImage fullImg = new BufferedImage(16 * xN, 32 * xN + xN, 2);
        Graphics g = fullImg.getGraphics();
        
        g.drawImage(img.getSubimage(w * 8, h * 8, w * 8, h * 8), 4 * xN, xN, 8 * xN, 8 * xN, null); 					// Голова
        g.drawImage(img.getSubimage(w * 20, h * 20, w * 8, h * 12), 4 * xN, 8 * xN + xN, 8 * xN, 12 * xN, null); 			// Туловище
        g.drawImage(img.getSubimage(w * 44, h * 20, w * 4, h * 12), 0, 8 * xN + xN, 4 * xN, 12 * xN, null); 				// Левая рука
        g.drawImage(ImageUtils.flipImage(img.getSubimage(w * 44, h * 20, w * 4, h * 12)), 12 * xN, 8 * xN + xN, 4 * xN, 12 * xN, null); // Правая рука
        g.drawImage(img.getSubimage(w * 4, h * 20, w * 4, h * 12), 4 * xN, 20 * xN + xN, 4 * xN, 12 * xN, null); 			// Левая нога
        g.drawImage(ImageUtils.flipImage(img.getSubimage(w * 4, h * 20, w * 4, h * 12)), 8 * xN, 20 * xN + xN, 4 * xN, 12 * xN, null); 	// Правая нога
        g.drawImage(img.getSubimage(w * 40, h * 8, w * 8, h * 8), (4 - 1) * xN, 0, (8 + 2) * xN, (8 + 2) * xN, null); 			// Головной убор

        return fullImg;
    }
    
    public static BufferedImage assembleCloak(BufferedImage img, final int xN)
    {
        if (img == null) return null;
        
        BufferedImage fullImg = new BufferedImage(22 * xN, 17 * xN, 2);
        
        int w = img.getWidth(), h = img.getHeight();
        if (img.getWidth() % 64 == 0 && img.getHeight() % 32 == 0) { w /= 64; h /= 32; }
        else { w /= 22; h /= 17; }
        
        fullImg.getGraphics().drawImage(img.getSubimage(w, h, w * 10, h * 16), 0, 0, 10 * xN, 16 * xN, null);
        
        return fullImg;
    }
}
