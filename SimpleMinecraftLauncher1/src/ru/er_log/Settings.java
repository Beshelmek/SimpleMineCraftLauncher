package ru.er_log;

import ru.er_log.components.UI_Theme;
import ru.er_log.themes.*;


public class Settings {
    
    /* Основные настройки программы */
    public static final String title				= "SimpleMinecraft.Ru | Launcher"; // Название окна программы
    public static final String title_in_game			= "SimpleMinecraft.Ru | Minecraft"; // Название окна программы после запуска игры
    public static final String version				= "v7.0.5"; // Версия программы (не используйте символ пробела)
    
            
    public static final String par_directory			= "AppData"; // Родительская папка для Minecraft (только для Windows) [ProgramFiles, AppData]
    public static final String game_directory			= ".simplemc"; // Папка с Minecraft (.minecraft)
    
    /* Настройки подключения */
    public static final String domain				= "simpleminecraft.ru"; // Домен сайта в формате: example.com
    public static final String site_dir				= "newlauncher"; // Директория к папке программы от корня сайта, если мак то ставим authml!
    public static final String protect_key			= "0b0c4ab10c418e94f31e0ed05a9f8a11"; // Ключ защиты доступа к веб-части (равен ключу в веб-части). Пример: 17@Ee'45x_Fq;04
    
// Надпись под логотипом, Размер шрифта, Номер шрифта (в файле настроек темы), Цвет (HTML формат)
    public static final String[] string_under_logo =
    {
        " ", "14", "#3", "#c3cad0"
    };
    
    public static final String login_text			= "Логин"; 		// Начальный текст поля для логина
    public static final String pass_text			= "Пароль"; 		// Начальный текст поля для пароля
    
    public static final String auth_but_auth_text		= "Войти на сервер"; 		// Текст кнопки авторизации для входа в онлайн-игру
    public static final String auth_but_offline_text		= "В игру"; 		// Текст кнопки авторизации для входа в оффлайн-игру
    
    public static final String off_user				= "Player"; 		// Имя пользователя одиночной игры
    public static final String off_sess 			= "123456"; 		// Сессия пользователя для одиночной игры
    
    /* Глобальные параметры программы */
    public static final boolean use_splash_screen		= true; 		// Использовать заставку пред запуском (оптимально для online темы)
    public static final boolean use_animation			= true; 		// Использовать анимацию переходов между окнами в программе
        public static final int animation_speed			    = 10; 		    // Скорость анимации переходов между окнами (в разумных пределах от 10 до 30)
    
    public static final boolean use_logo_as_url			= true; 		// Использовать логотип как ссылку на сайт
        public static final String logo_url			    = "simpleminecraft.ru"; 		    // Ссылка (в формате: example.com), которая откроется при клике на логотип. Оставтье пустым, если хотите, чтобы открывался ваш сайт указанный выше
    public static final int logo_indent				= 67; 			// Положение логотипа относительно оси Y
    
    public static final UI_Theme current_theme			= new JCR_Theme(); 	// Название темы программы (имя файла темы)
    
    /* Основные параметры программы */
    public static final boolean use_personal			= false; 		// Использовать личный кабинет пользователя (Внимание! Кабинет в разработке! В данный момент присутствует только вывод скина и плаща)
    public static final boolean use_monitoring			= true; 		// Использовать мониторинг при выборе сервера в списке серверов
    public static final boolean use_update_mon                  = true; 		// Автоматически загружать статус текущего сервера при запуске программы
    public static final boolean use_multi_client		= true; 		// Мультиклиентность (своя подпапка для каждого сервера)
    public static final boolean use_auto_entrance		= true; 		    // Использовать автоматический вход на выбранный сервер после авторизации
    public static final boolean use_pass_remember		= true; 		// Использовать функцию запоминания пароля (помимо логина)
    public static final boolean use_loading_news		= false; 		// Загружать новости и отображать их в личном кабинете игрока
        // Скорость анимации выезда новостей
        public static final int news_drawing_time		= 50; 			// Таймер прорисовки. Каждые [n] милисекунд прорисовывать окно (чем больше значение смещения (ниже), тем меньше должно быть значение таймера прорисовки)
        public static final int news_offset_panel		= 5; 			// Смещение. Каждые [n] милисекунд (выше) смещать окно на [x] пикселей (Внимание! Должно быть кратно ширине новостной панели (300))
    
    /* Параметры функции "GUARD" в программе */
    public static final boolean use_mods_delete			= false; 		// Удалять папку mods при запуске игры (к примеру, при хранении модификаций в "minecraft.jar")
    public static final boolean use_send_report                 = true;                 // Отправлять отчет о найденных файлах администратору на веб-сервер
    public static final boolean use_jar_check                   = true;                 // Использовать проверку клиента на наличие сторонних файлов с расширением ".jar" (рекомендуется оставить включенным)
    public static final boolean use_mod_check			= true; 		// Использовать проверку модов
    public static final boolean stop_dirty_drogram		= true; 		    // Завершать работу программы после обнаружения сторонних модификаций и их удаления
    public static final boolean use_mod_check_timer             = true; 		    // Каждые [n] секунд (ниже) производить перепроверку модов
    public static final int time_for_mods_check                 = 30; 		    // Время (в секундах), после которого будет произведена перепроверка модов
    
    public static final boolean stop_game_after_start		= false; 		// Завершать работу программы после запуска игры (версия 1.6+, программа завершит работу в любом случае, если не используется личный кабинет)
    public static final boolean path_client			= true; 		// Использовать автоматическую замену директории в клиенте (во избежании использования стандартной папки .minecraft) (до версии 1.6)
    public static final String old_mine_class	= "net.minecraft.client.Minecraft";	// Главный класс Minecraft (до версии 1.6, используется при патчинге клиента)
    public static final String mine_class	= "net.minecraft.client.main.Main";	// Главный класс Minecraft (версия 1.6+)
    public static final String lwrap_mine_class	= "net.minecraft.launchwrapper.Launch";	// Главный класс загрузчика  (версия 1.6+)
    
    /* Настройки отладки */
    public static final boolean test_mode                       = true;                // Запускать лаунчер без Starter
    public static final boolean use_developer_mode		= true; 		// ВНИМАНИЕ! Использовать режим разработчика (пропускать запрос об обновлении программы) ВНИМАНИЕ! НЕ ЗАБУДЬТЕ ОТКЛЮЧИТЬ ДАННЫЙ ПАРАМЕТР ЕСЛИ ОН АКТИВЕН
    public static final boolean testvoid                        = false;                // Тестирование новых методов.
    public static final boolean use_debugging			= false; 		// Режим отладки (вывод сообщений в консоль), рекомендуется отключить после окончательной настройки программы (как и отладку после запуска игры)
    public static final boolean use_game_debug_mode		= true; 		// Производить отладку после запуска игры (при версии 1.6+ лаунчер будет закрыт при значении параметра "false")
    public static final boolean draw_borders			= false; 		// Отрисовка границы элементов программы
    
    /* Настройки окна заставки( для опытных) */
    public static final int splash_width			= 280; 			// Ширина окна заставки
    public static final int splash_height			= 200; 			// Высота окна заставки
    public static final int splash_X_align			= 30; 			// Положение статуса на заставке относительно оси X
    public static final int splash_Y_align			= 172; 			// Положение статуса на заставке относительно оси Y
    
    /* Настройки стиля */
    public static final String fields_static_color		= "#34495e"; 		// Основной цвет текста текстовых полей, цвет текста окна настроек (в HTML формате)
        public static final String linux_fields_static_color	    = "#000000"; 	// Основной цвет текста текстовых полей на ОС Linux (в HTML формате)
    public static final String fields_inactive_color		= "#979c9f"; 		// Цвет текста неактивных текстовых полей (в HTML формате)
        public static final String linux_fields_inactive_color	    = "#979c9f"; 	// Цвет текста неактивных текстовых полей на ОС Linux (в HTML формате)
    public static final String alert_color			= "#ffffff"; 		// Цвет оповещаний (статус сервера..) (в HTML формате)
    public static final String panel_title_color		= "#34495e"; 		// Цвет заголовка на панели (окно обновления, окно оповещания...) (в HTML формате)
    public static final String panel_text_color			= "#34495e"; 		// Цвет текста на панели (статус окна splash, окно обновления...) (в HTML формате)
    public static final String elements_text_color		= "#ffffff"; 		// Цвет текста кнопок и комбобокс'а (в HTML формате)
    
    /* Блокированные настройки */
    public static final int frame_width				= 346; 			// Ширина фрейма [346]
    public static final int frame_height			= 450; 			// Высота фрейма [450]
    public static final int linux_frame_w                       = 2;                    // Ширина, занимаемая системным фреймом на ОС Linux
    public static final int linux_frame_h                       = 29;                   // Высота, занимаемая системным фреймом на ОС Linux
    
    public static final String[] libraryForPath = { "1.2.5::aj", "1.3.x::am", "1.4.x::an", "1.5.x::an" }; // Библиотека зашифрованных полей Minecraft.class для подмены директории. Поле соответсвтует версии Minecraft.
}
