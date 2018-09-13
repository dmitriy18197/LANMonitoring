/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lan_monitoringclient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * Главный класс графического интерфейса
 * @author User
 */
public class MainView extends BorderPane{
    
    private TextArea textArea;
    private Button sendReport;
    
    public MainView(){
        
        textArea = new TextArea();
        textArea.setPromptText("Поле для отчете");
        sendReport = new Button("Отправить");
        
        GridPane centralLayout = new GridPane();
        centralLayout.setHgap(10.0);
        centralLayout.setVgap(10.0);
        centralLayout.setPadding(new Insets(10,10,10,10));
        centralLayout.setAlignment(Pos.CENTER);
        
        Separator sep = new Separator();
        sep.setPadding(new Insets(5, 2, 5, 2));
        
        GridPane.setConstraints(textArea, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(sep, 0, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(sendReport, 0, 2, 1, 1, HPos.RIGHT, VPos.CENTER);
        
        centralLayout.getChildren().addAll(
                textArea, sendReport, sep
        );
        
        this.setCenter(centralLayout);
        handleEvents();
    }
    
    public void handleEvents(){
        sendReport.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                String msg = textArea.getText();
                try {
                    LANUtils.sendReport(msg);
                    Alert alert = new Alert(AlertType.INFORMATION, "Отчет отправлен");
                    alert.show();
                    textArea.clear();
                } catch (Exception ex) {
                    new Alert(AlertType.ERROR, "Ошибка").show();
                }
            }
        });
    }
    
}
