/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lan_monitoring;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * Главный класс графического интерфейса
 * @author User
 */
public class MainView extends BorderPane{
    
    // компоненты графического интерфейса
    public ListView listView;
    public TextArea textArea;
    private TabPane tabPane;
    private Tab localNetworkTab, reportsTab;
    public ReportsView reportsView;
    private Button save;
    private ComboBox comboBox;
    
    private List<PCInfo> pcInfos = new ArrayList<>();
    
    private ObservableList<String> pcComponents = FXCollections.observableArrayList(
            "Операционная система", "Материнская плата", "Процессор", "Оперативная память", 
            "Жесткие диски", "Видеокарта", "Монитор", "Аудио устройства", "BIOS"
    );
    
    public MainView(){
        
        initTabPane();
        
        GridPane centralLayout = new GridPane();
        
        listView = new ListView();
        textArea = new TextArea();
        
        comboBox = new ComboBox(pcComponents);
        
        Label l1 = new Label("Локальная Сеть (Имена ПК)");
        Label l2 = new Label("Информация");
        
        GridPane.setConstraints(l1, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(listView, 0, 1, 1, 6, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(l2, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(comboBox, 2, 0, 1, 1, HPos.LEFT, VPos.CENTER);
        GridPane.setConstraints(textArea, 1, 1, 2, 6, HPos.CENTER, VPos.CENTER);        
        
        centralLayout.getChildren().addAll(
                l1, l2, listView, textArea, comboBox
        );
        centralLayout.setHgap(5.0);
        centralLayout.setVgap(5.0);
        centralLayout.setPadding(new Insets(10,10,10,10));
        centralLayout.setAlignment(Pos.CENTER);
        
        localNetworkTab.setContent(centralLayout);
        reportsView = new ReportsView();
        reportsTab.setContent(reportsView);
        
        setListItems(SQLiteDB.getAllPCInfos());
        
        GridPane bottomLayout = new GridPane();
        
        Separator bottomSep = new Separator();
        bottomSep.setPadding(new Insets(10, 2, 10, 2));
        
        bottomLayout.add(bottomSep, 0, 0);
        bottomLayout.setPadding(new Insets(10, 2, 10, 2));
        
        this.setBottom(bottomSep);
        
        bindValues();
        
        comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                
                if(listView.getSelectionModel().isEmpty())
                    listView.getSelectionModel().selectFirst();
                
                int index = listView.getSelectionModel().getSelectedIndex();
                
                if(!textArea.getText().isEmpty())
                    textArea.clear();
                switch(newValue){
                    case "Операционная система":
                        textArea.setText(pcInfos.get(index).os);
                        break;
                    case "Материнская плата":
                        textArea.setText(pcInfos.get(index).motherBoard);
                        break;
                    case "Процессор":
                        textArea.setText(pcInfos.get(index).cpu);
                        break;
                    case "Оперативная память":
                        textArea.setText(pcInfos.get(index).memory);
                        break;
                    case "Жесткие диски":
                        textArea.setText(pcInfos.get(index).hdd);
                        break;
                    case "Видеокарта":
                        textArea.setText(pcInfos.get(index).gpu);
                        break;
                    case "Монитор":
                        textArea.setText(pcInfos.get(index).monitor);
                        break;
                    case "Аудио устройства":
                        textArea.setText(pcInfos.get(index).audio);
                        break;
                    case "BIOS":
                        textArea.setText(pcInfos.get(index).bios);
                        break;
                }
            }
            
        });
    }
    
    public void setListItems(final List<PCInfo> list){
        
        if(!this.listView.getItems().isEmpty()){
            this.listView.getItems().clear();
            this.pcInfos.clear();
        }
            
        
        for(PCInfo item : list){
            this.listView.getItems().add(item.ipAddress);
            this.pcInfos.add(item);
        }
        
    }
    
    public void setComputerInfo(String value){
        this.textArea.setText(value);
    }
    
    private void initTabPane(){
        tabPane = new TabPane();
        localNetworkTab = new Tab("Локальная Сеть");
        reportsTab = new Tab("Журнал о неполадках");
        
        localNetworkTab.setClosable(false);
        reportsTab.setClosable(false);
        
        tabPane.getTabs().addAll(
                localNetworkTab, reportsTab
        );
        this.setTop(tabPane);
    }
    
    private void bindValues(){
        textArea.minWidthProperty().bind(this.widthProperty().multiply(0.6));
        textArea.minHeightProperty().bind(this.heightProperty().multiply(0.8));
        this.reportsView.context.minWidthProperty().bind(this.widthProperty().multiply(0.6));
        this.reportsView.context.minHeightProperty().bind(this.heightProperty().multiply(0.8));
    }
}
