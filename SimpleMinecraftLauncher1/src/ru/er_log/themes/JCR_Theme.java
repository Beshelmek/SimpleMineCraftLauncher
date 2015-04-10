package ru.er_log.themes;

import ru.er_log.Settings;
import ru.er_log.components.UI_Theme;
import ru.er_log.utils.BaseUtils;

public class JCR_Theme implements UI_Theme
{
    /** Предназначение данных методов можно обнаружить в файле "UI_Theme.java" */
    
    public String themeDirectory()
    {
        return "/ru/er_log/data/JCR_Style/";
    }
    
    public String themeSplashBack()
    {
        return "Splash.png";
    }
    
    public String themeBackground()
    {
        return "Background.png";
    }
    
    public String themeLogotype()
    {
        return "Logotype.png";
    }
    
    public String themeFavicon()
    {
        return "Favicon.png";
    }
    
    public String themeSysButs()
    {
        return "SysButs.png";
    }
    
    public String themeButton()
    {
        return "Button.png";
    }
    
    public String themeCheckBox()
    {
        return "CheckBox.png";
    }
            
    public String themeComboBox()
    {
        return "ComboBox.png";
    }
    
    public String themeFieldsStaticColor()
    {
        return BaseUtils.getPlatform() == 0 ? Settings.linux_fields_static_color : Settings.fields_static_color;
    }
    
    public String themeFieldsInactiveColor()
    {
        return BaseUtils.getPlatform() == 0 ? Settings.linux_fields_inactive_color : Settings.fields_inactive_color;
    }
    
    public String themeAuthFields()
    {
        return "AuthFields.png";
    }
    
    public String themeModalBack()
    {
        return "ModalBack.png";
    }
    
    public String themeNewsBack()
    {
        return "NewsBack.png";
    }
    
    public String themePressedBorder()
    {
        return "PressedBorder.png";
    }
    
    public String themeFieldBack()
    {
        return "FieldBack.png";
    }
    
    public String themeBandColors()
    {
        return "BandColors.png";
    }
    
    public String themeWaitIcon()
    {
        return "Wait.png";
    }
    
    public String themeAlertIcons()
    {
        return "AlertIcons.png";
    }
    
    public String themeProgressBar()
    {
        return "Progress.png";
    }
    
    public String themeGameIcon()
    {
        return "m_favicon.png";
    }
    
    public String themeFont(final int type)
    {
        switch (type)
        {
            case 1: return "Font1.ttf"; 	// Основной шрифт темы
            case 2: return "Font2.ttf"; 	// Шрифт заголовков и прочего
            case 3: return "Font3.ttf"; 	// По желанию можно задействовать в коде
            default: return null; // todo
        }
    }
}