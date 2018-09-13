/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lan_monitoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * Компонент графического интерфейса для отчетов
 * @author User
 */
public class ReportsView extends BorderPane{
    
    private final ListView<String> listView;
    private List<String> reportContent = new ArrayList<>();
    private List<String> fromIP = new ArrayList<>();
    
    private Button save, delete;
    public TextArea context;
    
    public ReportsView(){
        
        save = new Button("Сохранить");
        save.setPadding(new Insets(10, 25, 10, 25));
        
        delete = new Button("Удалить");
        delete.setPadding(new Insets(10, 25, 10, 25));
        
        listView = new ListView();
        
        setListItems(SQLiteDB.getAllReports());
        
        final List<String> checkedItems = new ArrayList<String>();
        
        listView.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(final String item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener(new ChangeListener<Boolean>(){
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if(newValue){
                            checkedItems.add(item);
                        }
                        else{
                            checkedItems.remove(item);
                        }
                    }
                }
            );
            return observable ;
            }
        }));
        
        context = new TextArea();
        
        GridPane centralLayout = new GridPane();
        centralLayout.setAlignment(Pos.CENTER);
        centralLayout.setHgap(5.0);
        centralLayout.setVgap(5.0);
        centralLayout.setPadding(new Insets(10,10,10,10));
        
        GridPane.setConstraints(listView, 0, 2, 1, 2, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(delete, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(context, 1, 2, 1, 2, HPos.LEFT, VPos.CENTER);
        GridPane.setConstraints(save, 1, 4, 1, 1, HPos.RIGHT, VPos.CENTER);
        
        centralLayout.getChildren().addAll(
                listView, context, save, delete
        );
        
        this.setCenter(centralLayout);
        this.setPadding(new Insets(10,10,10,10));
        
        delete.setOnAction(new EventHandler<ActionEvent>(){
            
            @Override
            public void handle(ActionEvent event) {
                Alert confAlert = new Alert(AlertType.CONFIRMATION, "Удалить все выбранные отчеты?");                
                if(checkedItems.isEmpty()){
                    new Alert(AlertType.ERROR, "Выберете отчеты которые хотите удалить").showAndWait();
                }
                else{
                    Optional<ButtonType> result = confAlert.showAndWait();
                    if(result.get() == ButtonType.OK){
                        for(String item : checkedItems){
                            //                            setListItems(SQLiteDB.getAllReports());
                            int i = listView.getItems().indexOf(item);
                            listView.getItems().remove(item);
                            fromIP.remove(i);
                            reportContent.remove(i);
                            SQLiteDB.deleteReportFromDB(item);
                        }
                        checkedItems.clear();
                    }
                }           
            }
        });
        
        listView.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>(){
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        int selectedIndex = (int) newValue;
                        
                        if(!context.getText().isEmpty())
                            context.clear();
                        
                        try{
                            String textAreaString = "ОТ: " + fromIP.get(selectedIndex) + "\n" + reportContent.get(selectedIndex);
                            context.setText(textAreaString);
                            
                        }
                        catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                        
                    }
                }
        );
        handleSaveButton();
    }
    
    public void setListItems(final List<Report> list){
        
        listView.getItems().clear();
        reportContent.clear();
        fromIP.clear();
        Collections.reverse(list);
        for(Report report : list){
            listView.getItems().add(report.date);
            reportContent.add(report.reportContent);
            fromIP.add(report.from);
        }
        
    }
    
    public void handleSaveButton(){
        save.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("save button is pushed");
                
                String[] newContext = context.getText().split("\n");
                String newCont = "";
                for(int i=2 ; i<newContext.length ; i++)
                    newCont += newContext[i] + "\n";
                
                String selected = listView.getSelectionModel().getSelectedItem();
                int selectedIndex = listView.getSelectionModel().getSelectedIndex();
                SQLiteDB.updateReport(selected , newCont);
                reportContent.set(selectedIndex, newCont);
            }
        });
    }
    
}
