package com.mycompany.lan_monitoring;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;


/**
 * Главный класс приложения
 * @author User
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        stage = new Stage();
        
        // создаем иконку в трэе
        createTrayIcon(stage);
        Platform.setImplicitExit(false);
        
        // подключаем базу данных
        SQLiteDB.connectDB();
        SQLiteDB.createDB();

        MainView root = new MainView();
        
        // запускаем сервер
        Server server = new Server(root);
        server.startServer();
        
        
//        server.findNetworkIPs();
        
        List<PCInfo> list = SQLiteDB.getAllPCInfos();

        root.setListItems(list);
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Server");
        stage.setScene(scene);
        stage.show();
    }
    
    private TrayIcon trayIcon;
    
    /**
     * Метод для создания иконки в трэе
     * @param stage 
     */
    public void createTrayIcon(final Stage stage) {
        
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            java.awt.Image image = null;
            try {
                image = ImageIO.read(MainApp.class.getResource("/system-report.png"));
            } catch (IOException ex) {
                System.out.println(ex);
            }


            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    hide(stage);
                }
            });
            
            final ActionListener closeListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.exit(0);
                }
            };

            ActionListener showListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.show();
                        }
                    });
                }
            };
            
            // контекстное меню
            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem("Открыть");
            showItem.addActionListener(showListener);
            popup.add(showItem);

            MenuItem closeItem = new MenuItem("Закрыть");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);
            
            trayIcon = new TrayIcon(image, "Открыть", popup);
            
            trayIcon.addActionListener(showListener);
            
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }            
        }
    }
    
    private void hide(final Stage stage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (SystemTray.isSupported()) {
                    stage.hide();                    
                } else {
                    System.exit(0);
                }
            }
        });
    }
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        launch(args);        
    }
    
}
