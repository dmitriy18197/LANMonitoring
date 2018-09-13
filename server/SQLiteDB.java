/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lan_monitoring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Класс для работы с базами данных
 * @author User
 */

public class SQLiteDB {
    
    private static final String driverName = "org.sqlite.JDBC";
    private static final String connectionString = "jdbc:sqlite:LAN.db";
    
    private static Connection conn;
    private static ResultSet resSet;
    private static Statement statmt;
    
    private static final String reports_sql = "CREATE TABLE IF NOT EXISTS reports (\n"
                                        + "id integer PRIMARY KEY,\n"
                                        + " fromIP text,\n"
                                        + " created_at text,\n"
                                        + " reportContent text);";
    
    private static final String pcInfoSql = "CREATE TABLE IF NOT EXISTS pcInfo(\n"
            + "id integer PRIMARY KEY,\n"
            + "IP text,\n"
            + "OS text,\n"
            + "MotherBoard text,\n"
            + "CPU text,\n"
            + "Memory text,\n"
            + "HDD text,\n"
            + "GPU text,\n"
            + "Monitor text,\n"
            + "Audio text,\n"
            + "Bios text);";
    
    
    
    public static void connectDB(){
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            System.out.println("Не найден драйвер для БД");
            e.printStackTrace();
            return;
        }
        
        try {
            conn = DriverManager.getConnection(connectionString);
            System.out.println("Соединение с БД установлено");
        } catch (SQLException e) {
            System.out.println("Не получилось подключиться к БД. Не верный URL");
            e.printStackTrace();
            return;
        }
    }
    
    public static void createDB(){
        try {
            statmt = conn.createStatement();
            statmt.execute(reports_sql);
            System.out.println("Таблицы с отчета создана или уже существует.");
            statmt.execute(pcInfoSql);
            System.out.println("Таблица с информацией о компьютерах в локальной сети создана или уже существует.");
        } catch (SQLException ex) {
            System.out.println("Не удалось создать БД");
        }
    }
    
    public static void addReport(Report report) {
        // Создадим подготовленное выражение, чтобы избежать SQL-инъекций
        try (PreparedStatement statement = SQLiteDB.conn.prepareStatement(
                        "INSERT INTO reports(`fromIP`, `created_at`, `reportContent`) " +
                         "VALUES(?, ?, ?)")) {
            statement.setObject(1, report.from);
            statement.setObject(2, report.date);
            statement.setObject(3, report.reportContent);
            // Выполняем запрос
            statement.execute();
            System.out.println("Добавлен новый отчет в БД");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void updateReport(String date, String newReport) {
        String sql = "UPDATE reports SET reportContent = ? "
                + "WHERE created_at = ?";
 
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            // set the corresponding param
            pstmt.setString(1, newReport);
            pstmt.setString(2, date);
            // update 
            pstmt.executeUpdate();
            System.out.println("БД успешно обновлена");
            new Alert(AlertType.INFORMATION, "Отчет успешно сохранен").show();
        } catch (SQLException e) {
            System.out.println("Не удалось обновить БД");
            new Alert(AlertType.ERROR, "Не удалось сохранить отчет").show();
            System.out.println(e.getMessage());
        }
    }
    
    public static List<Report> getAllReports(){
        try (Statement statement = SQLiteDB.conn.createStatement()) {            
            List<Report> list = new ArrayList<Report>();
            ResultSet resultSet = statement.executeQuery("SELECT fromIP, created_at, reportContent FROM reports");
            while(resultSet.next()){
                list.add(new Report(resultSet.getString("fromIP"),
                                    resultSet.getString("created_at"),
                                    resultSet.getString("reportContent")));
            }
            return list;
            
        } catch (SQLException ex) {
            System.out.println("Не удалось подключиться к БД");
            return Collections.emptyList();
        }        
    }
    
    
    public static void deleteReportFromDB(String date){
        
        String sql = "DELETE FROM reports "
                + "WHERE created_at = ?";
        
        try (PreparedStatement statement = SQLiteDB.conn.prepareStatement(sql)) {
            statement.setObject(1, date);
            // Выполняем запрос
            statement.execute();
            System.out.println("Из БД был удален " + date);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // DONE
    public static void addPCInfo(PCInfo pcInfo){
        // Создадим подготовленное выражение, чтобы избежать SQL-инъекций
        String prepStatement = "INSERT INTO pcInfo(`IP`,`OS`,`MotherBoard`,`CPU`,`Memory`,`HDD`,`GPU`,`Monitor`,`Audio`,`Bios`)" + 
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = SQLiteDB.conn.prepareStatement(prepStatement)){
            
            statement.setObject(1, pcInfo.ipAddress);
            statement.setObject(2, pcInfo.os);
            statement.setObject(3, pcInfo.motherBoard);
            statement.setObject(4, pcInfo.cpu);
            statement.setObject(5, pcInfo.memory);
            statement.setObject(6, pcInfo.hdd);
            statement.setObject(7, pcInfo.gpu);
            statement.setObject(8, pcInfo.monitor);
            statement.setObject(9, pcInfo.audio);
            statement.setObject(10, pcInfo.bios);
            
            // Выполняем запрос
            statement.execute();
            System.out.println("Добавлена новая информации о компьюетере (IP : " +pcInfo.ipAddress + " )");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean checkPC(String ip){
        try (Statement statement = SQLiteDB.conn.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT IP FROM pcInfo");
            while(resultSet.next()){
                if(resultSet.getString("IP") == null ? ip == null : resultSet.getString("IP").equals(ip))
                    return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    // DONE
    public static List<PCInfo> getAllPCInfos(){
        try (Statement statement = SQLiteDB.conn.createStatement()) {            
            List<PCInfo> list = new ArrayList<PCInfo>();
            ResultSet resultSet = statement.executeQuery("SELECT IP, OS, MotherBoard, CPU, Memory, HDD, GPU, Monitor, Audio, Bios FROM pcInfo");
            while(resultSet.next()){
                list.add(new PCInfo(resultSet.getString("IP"),
                                    resultSet.getString("OS"),
                                    resultSet.getString("MotherBoard"),
                                    resultSet.getString("CPU"),
                                    resultSet.getString("Memory"),
                                    resultSet.getString("HDD"),
                                    resultSet.getString("GPU"),
                                    resultSet.getString("Monitor"),
                                    resultSet.getString("Audio"),
                                    resultSet.getString("Bios")));
            }
            return list;            
        } catch (SQLException ex) {
            System.out.println("Не удалось подключиться к БД");
            return Collections.emptyList();
        }
    }
    
    public static void closeDB(){
        try {
            statmt.close();
            conn.close();
            System.out.println("Соединения закрыты");
        } catch (SQLException e) {
            System.out.println("Не получилось закрыть БД");
            e.printStackTrace();
            return;
        }
    }
    
}
