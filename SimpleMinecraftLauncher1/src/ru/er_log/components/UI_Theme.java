package ru.er_log.components;

public interface UI_Theme
{
    public String themeDirectory();		// Возвращает директорию с темой
    public String themeSplashBack();		// Возвращает название фонового изображения заставки
    public String themeBackground();		// Возвращает название фонового изображения
    public String themeLogotype();		// Возвращает название изображения логотипа
    public String themeFavicon();		// Возвращает название изображения иконки
    public String themeSysButs();		// Возвращает название изображения сис. кнопок
    public String themeButton();		// Возвращает название изображения кнопок
    public String themeCheckBox();		// Возвращает название изображения CheckBox'а
    public String themeComboBox();		// Возвращает название изображения выпадающего меню с серверами (ComboBox)
    public String themeFieldsStaticColor();	// Возвращает основной цвет текста текстовых полей (в HTML формате)
    public String themeFieldsInactiveColor();	// Возвращает цвет текста неактивных текстовых полей (в HTML формате)
    public String themeAuthFields();		// Возвращает название изображения элементов авторизации
    public String themeModalBack();		// Возвращает название изображения модального окна
    public String themeNewsBack();		// Возвращает название изображения окна новостей
    public String themePressedBorder();		// Возвращает название изображения вдавленного элемента
    public String themeFieldBack();		// Возвращает название изображения малых текстовых полей
    public String themeBandColors();		// Возвращает название изображения элементов анимации
    public String themeWaitIcon();		// Возвращает название изображения иконки ожидания
    public String themeAlertIcons();		// Возвращает название изображения иконок оповещаний
    public String themeProgressBar();		// Возвращает название изображения полосы заполненности
    public String themeGameIcon();		// Возвращает название изображения иконки программы при игре
    public String themeFont(int type);		// Возвращает название шрифта темы
}
