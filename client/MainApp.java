package com.mycompany.lan_monitoringclient;

import static javafx.application.Application.launch;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;

/**
 * Главный класс приложения
 * @author User
 */
public class MainApp extends Application {
    
    private TrayIcon trayIcon;
    public static String admin = "192.168.88.27";
    
    @Override
    public void start(final Stage stage) throws Exception {
        
        LANUtils.admin = this.admin;
        
        MainView root = new MainView();
        
        createTrayIcon(stage);
        Platform.setImplicitExit(false);
        
        Scene scene = new Scene(root);
        stage.setTitle("Отправить отчет о поломке");
        stage.setResizable(false);
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        
        //set Stage boundaries to the lower right corner of the visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 450);
        stage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 300);
        stage.setWidth(450);
        stage.setHeight(300);
        
        stage.setScene(scene);
        stage.show();
        
        try{
            LANUtils.sendPCInfo();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
            Alert alert = new Alert(AlertType.CONFIRMATION, "LAN Monotoring");
            alert.setHeaderText("Не удалось подключится к IP");
            alert.setContentText("Измените IP адрес");
            Optional<ButtonType> resp = alert.showAndWait();
            if(resp.isPresent()){
                getAdminIP(admin);
            }
            else{
                alert.close();
            }            
        }
        
    }
    
    private void getAdminIP(String defaultIP){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("LAN Monitoring");
        dialog.setHeaderText("IP");
        dialog.setContentText("Введите пожалуйста IP сервера:");
        dialog.getEditor().setText(defaultIP);
        
        Optional<String> response = dialog.showAndWait();
        if(response.isPresent()){
            String ip = response.get();
            if(ip.isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Введите IP!!!");
                alert.show();
            }
            else if(!LANUtils.checkAdminIP(ip)){
                getAdminIP(ip);
            }
            else{
                dialog.close();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Соединение в сервером установлено");
                alert.show();
                this.admin = ip;
                LANUtils.admin = ip;
                try {
                    LANUtils.sendPCInfo();
                } catch (IOException ex) {
                    System.out.println("Error");
                }
            }
        }
        else{
            Platform.exit();
        }
    }
    
    public void createTrayIcon(final Stage stage) {
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            java.awt.Image image = null;
            try {
                image = ImageIO.read(MainApp.class.getResource("/system-report.png"));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
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
            
            trayIcon = new TrayIcon(image, "Отправить отчет", popup);
            
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
