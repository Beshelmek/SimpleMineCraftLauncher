package ru.er_log.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import ru.er_log.components.Frame;
import static ru.er_log.utils.BaseUtils.baseUtils;

public class StyleUtils {

    public static Font font;
    public static String themeDir = BaseUtils.getTheme().themeDirectory();
    
    public static Map<Integer, Font> fonts = new HashMap<Integer, Font>();

    public static Font getFont(float size, int type)
    {
        try
        {
            if (fonts.containsKey(type))
                return (Font) fonts.get(type).deriveFont(size);
            
            switch (type)
            {
                case 1:
                    font = Font.createFont(0, StyleUtils.class.getResourceAsStream(themeDir + BaseUtils.getTheme().themeFont(1))).deriveFont(size);
                    break;
                case 2:
                    font = Font.createFont(0, StyleUtils.class.getResourceAsStream(themeDir + BaseUtils.getTheme().themeFont(2))).deriveFont(size); // 28691096
                    break;
                case 3:
                    font = Font.createFont(0, StyleUtils.class.getResourceAsStream(themeDir + BaseUtils.getTheme().themeFont(3))).deriveFont(size);
                    break;
                default:
                    font = new Font("Arial", 0, (int) (size));
                    Frame.reportErr("Шрифт #" + type + " не обнаружен, установлен шрифт " + font.getFontName());
            }
        } catch (FontFormatException | IOException e)
        {
            font = new Font("Arial", 0, (int) (size));
            Frame.reportErr("Установка шрифта #" + type + " не удалась, установлен шрифт " + font.getFontName());
        }
        
        fonts.put(type, font);
        
        return font.deriveFont(size);
    }

    public static Font getFont(float size, int type, int style)
    {
        try
        {
            switch (type)
            {
                case 1:
                    font = Font.createFont(0, StyleUtils.class.getResourceAsStream(themeDir + BaseUtils.getTheme().themeFont(1))).deriveFont(size).deriveFont(style);
                    break;
                case 2:
                    font = Font.createFont(0, StyleUtils.class.getResourceAsStream(themeDir + BaseUtils.getTheme().themeFont(2))).deriveFont(size).deriveFont(style);
                    break;
                case 3:
                    font = Font.createFont(0, StyleUtils.class.getResourceAsStream(themeDir + BaseUtils.getTheme().themeFont(3))).deriveFont(size).deriveFont(style);
                    break;
                default:
                    font = new Font("Arial", style, (int) (size));
                    Frame.reportErr("Шрифт #" + type + " не обнаружен, установлен шрифт " + font.getFontName());
            }
        } catch (FontFormatException | IOException e)
        {
            font = new Font("Arial", style, (int) (size));
            Frame.reportErr("Установка шрифта #" + type + " не удалась, установлен шрифт " + font.getFontName());
        }

        return font.deriveFont(size);
    }
    
    public static String getOnlineThemeColor()
    {
        try
        {
            return baseUtils.sendGET(BaseUtils.getURLSc("jcr_theme.php"), "action=theme&request=color", false);
        } catch (Exception e)
        {
            Frame.reportErr("Не удалось загрузить online цветовую схему программы");
            return BaseUtils.getTheme().themeFieldsInactiveColor() + ":s:" + BaseUtils.getTheme().themeFieldsStaticColor();
        }
    }
}
