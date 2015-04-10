package ru.er_log.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import ru.er_log.components.Frame;

public class ImageUtils {

    public int width = 0;
    public int height = 0;
    public int x = 0;
    public int y = 0;
    public BufferedImage defImg, rolImg, preImg;
    
    public ImageUtils(int x, int y, int width, int height, int xImg, int yImg, BufferedImage image)
    {
        this.width  = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.defImg = image.getSubimage(xImg, yImg, width, height);
    }
    
    public ImageUtils(int x, int y, int width, int height, int xImgD, int yImgD, int xImgR, int yImgR, int xImgP, int yImgP, BufferedImage image)
    {
        this.width  = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.defImg = image.getSubimage(xImgD, yImgD, width, height);
        this.rolImg = image.getSubimage(xImgR, yImgR, width, height);
        this.preImg = image.getSubimage(xImgP, yImgP, width, height);
    }
    
    public static void drawOpacityImage(Graphics2D g2d, int percent, BufferedImage img, int x, int y, int width, int height, ImageObserver observer)
    {
        float opacity = (float) (percent) / 100;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2d.drawImage(img, x, y, width, height, observer);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
    }
    
    public static void drawOpacityImage(Graphics2D g2d, int percent, BufferedImage img, int x, int y, ImageObserver observer)
    {
        drawOpacityImage(g2d, percent, img, x, y, img.getWidth(), img.getHeight(), observer);
    }
    
//    public static BufferedImage splitImage(int fullWidth, int imgHeight, int percent, BufferedImage img)
//    {
//        return splitImage(0, fullWidth, img.getWidth(), imgHeight, percent, img);
//    }
    
    public static BufferedImage splitImage(int x, int fullWidth, int imgWidth, int imgHeight, int percent, BufferedImage img)
    {
        int realWidth = (int) ((float) fullWidth / 100 * percent);
        BufferedImage fullImg = new BufferedImage(fullWidth, imgHeight, 2);
        BufferedImage leftPart = img.getSubimage(x, 0, imgWidth / 3, img.getHeight());
        BufferedImage centerPart = img.getSubimage(x + imgWidth / 3, 0, imgWidth / 3, img.getHeight());
        BufferedImage rightPart = img.getSubimage(x + imgWidth / 3 * 2, 0, imgWidth / 3, img.getHeight());
        
        fullImg.getGraphics().drawImage(leftPart, 0, 0, leftPart.getWidth(), imgHeight, null);
        fullImg.getGraphics().drawImage(fill_horizontal(centerPart, fullWidth - leftPart.getWidth() - rightPart.getWidth(), imgHeight), leftPart.getWidth(), 0, fullWidth - leftPart.getWidth() - rightPart.getWidth(), imgHeight, null);
        fullImg.getGraphics().drawImage(rightPart, fullWidth - rightPart.getWidth(), 0, rightPart.getWidth(), imgHeight, null);
        try { fullImg = fullImg.getSubimage(0, 0, realWidth, imgHeight); } catch(Exception e) {};
        
        return fullImg;
    }
    
    public static BufferedImage splitImage(int width, int height, BufferedImage img)
    {
        int partWidth = img.getWidth() / 3; int partHeight = img.getHeight() / 3;
        BufferedImage fullImg = new BufferedImage(width, height, 2);
        BufferedImage topLeftPart = img.getSubimage(0, 0, partWidth, partHeight);
        BufferedImage topCenterPart = img.getSubimage(partWidth, 0, partWidth, partHeight);
        BufferedImage topRightPart = img.getSubimage(partWidth * 2, 0, partWidth, partHeight);
        BufferedImage middleLeftPart = img.getSubimage(0, partHeight, partWidth, partHeight);
        BufferedImage middleCenterPart = img.getSubimage(partWidth, partHeight, partWidth, partHeight);
        BufferedImage middleRightPart = img.getSubimage(partWidth * 2, partHeight, partWidth, partHeight);
        BufferedImage bottomLeftPart = img.getSubimage(0, partHeight * 2, partWidth, partHeight);
        BufferedImage bottomCenterPart = img.getSubimage(partWidth, partHeight * 2, partWidth, partHeight);
        BufferedImage bottomRightPart = img.getSubimage(partWidth * 2, partHeight * 2, partWidth, partHeight);
        
        fullImg.getGraphics().drawImage(topLeftPart, 0, 0, partWidth, partHeight, null);
        fullImg.getGraphics().drawImage(fill(topCenterPart, width - partWidth * 2, partHeight), partWidth, 0, width - partWidth * 2, partHeight, null);
        fullImg.getGraphics().drawImage(topRightPart, width - partWidth, 0, partWidth, partHeight, null);
        fullImg.getGraphics().drawImage(fill(middleLeftPart, partWidth, height - partHeight * 2), 0, partHeight, partWidth, height - partHeight * 2, null);
        fullImg.getGraphics().drawImage(fill(middleCenterPart, width - partWidth * 2, height - partHeight * 2), partWidth, partHeight, width - partWidth * 2, height - partHeight * 2, null);
        fullImg.getGraphics().drawImage(fill(middleRightPart, partWidth, height - partHeight * 2), width - partWidth, partHeight, partWidth, height - partHeight * 2, null);
        fullImg.getGraphics().drawImage(bottomLeftPart, 0, height - partHeight, partWidth, partHeight, null);
        fullImg.getGraphics().drawImage(fill(bottomCenterPart, width - partWidth * 2, partHeight), partWidth, height - partHeight, width - partWidth * 2, partHeight, null);
        fullImg.getGraphics().drawImage(bottomRightPart, width - partWidth, height - partHeight, partWidth, partHeight, null);
        
        return fullImg;
    }
    
    public static BufferedImage splitImage(int cut_top_height, int cut_lower_height, int new_height, BufferedImage img)
    {
        BufferedImage fullImg = new BufferedImage(img.getWidth(), new_height, 2);
        BufferedImage topLine = img.getSubimage(0, 0, img.getWidth(), cut_top_height);
        BufferedImage centerLine = img.getSubimage(0, cut_top_height, img.getWidth(), img.getHeight() - cut_lower_height - cut_top_height);
        BufferedImage lowerLine = img.getSubimage(0, img.getHeight() - cut_lower_height, img.getWidth(), cut_lower_height);
        
        fullImg.getGraphics().drawImage(topLine, 0, 0, null);
        fullImg.getGraphics().drawImage(fill(centerLine, img.getWidth(), new_height - cut_top_height - cut_lower_height), 0, cut_top_height, null);
        fullImg.getGraphics().drawImage(lowerLine, 0, new_height - cut_lower_height, null);
        
        return fullImg;
    }
    
    public static BufferedImage fill(BufferedImage img, int width, int height)
    {
        int imgw = img.getWidth();
        int imgh = img.getHeight();
        BufferedImage fullImg = new BufferedImage(width, height, 2);
        for (int x = 0; x <= width / imgw; x++)
            for (int y = 0; y <= height / imgh; y++)
                fullImg.getGraphics().drawImage(img, x * imgw, y * imgh, null);
        return fullImg;
    }
    
    public static BufferedImage fill_horizontal(BufferedImage img, int width, int height_to_stretching)
    {
        int imgw = img.getWidth();
        BufferedImage fullImg = new BufferedImage(width, height_to_stretching, 2);
        for (int x = 0; x <= width / imgw; x++)
            fullImg.getGraphics().drawImage(img, x * imgw, 0, imgw, height_to_stretching, null);
        return fullImg;
    }
    
    public static BufferedImage takePicture(JComponent c)
    {
        int w = c.getWidth();
        int h = c.getHeight();
        BufferedImage img = new BufferedImage(w, h, 2);
        Graphics2D g = img.createGraphics();
        c.paint(g);
        g.dispose();
        return img;
    }
    
    public static BufferedImage flipImage(BufferedImage img)
    {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-img.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(img, null);
    }
    
    public static BufferedImage getImageByURL(URL url, boolean send)
    {
        try
        {
            BufferedImage img = ImageIO.read(url);
            if (send) Frame.report("Загружено изображение: " + url.toString());
            return img;
        } catch (IOException ex)
        {
            if (send) Frame.reportErr("Не удалось загрузить изображение: " + url.toString());
            return null;
        }
    }
}
