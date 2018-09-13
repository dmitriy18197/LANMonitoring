/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lan_monitoring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Класс отвечающий за функциональность сервера
 * @author User
 */
public class Server {
    
    // главный класс графического интерфейса
    private MainView mainView;
    
    // номер порта (может быть случайным числом от 1025 до 65535)
    private static int port = 6666;
    
    //кол-во подключаемых пользователей
    private static final int numberOfThreads = 10;
    
    /**
     * Конструктор для сервера
     * @param mainView - главный класс графического интерфейса
     */
    public Server(MainView mainView){
        this.mainView = mainView;
    }
    
    //массив IP адресов в локальной сети
    public static List<String> localIPs = new ArrayList<>();
    
    
    /**
     * Метод для поиска IP адресов в локальной сети
     */
    public static void findNetworkIPs() {
        
        byte[] ip;
        try {
            ip = InetAddress.getLocalHost().getAddress();
        } catch (Exception e) {
            return;
        }
        
        // в цикле от 0 до 254 ищем активные соединения
        for(int i=1;i<=254;i++) {
            int j = i;  
            try {
                ip[3] = (byte)j;
                InetAddress address = InetAddress.getByAddress(ip);
                String output = address.toString().substring(1);
                // если IP активен, то записываем его в массив
                if(address.isReachable(100)) {
                    System.out.println(output + " в сети");
                    localIPs.add(output);
                }
            } catch (Exception e) {
                        System.out.println("Ошибка" + e.getMessage());
            }
        }
    }
    
    
    
    /**
     * Метод для запуска сервера
     * Запускает сервер и ждет подключений пользователей локальной сети
     * Кол-во максимально подключаемых пользователей 10 (зависит от переменной numberOfThreads)
     * После того как в локальной сети появляется активное подключений выполняется ClientTask
     */
    public void startServer() {
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(numberOfThreads);
        System.out.println("Сервер запущен!");
        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    System.out.println("В ожидании подключений...");
                    System.out.println(InetAddress.getLocalHost());
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        clientProcessingPool.submit(new ClientTask(clientSocket, mainView));
                    }
                } catch (IOException e) {
                    System.err.println("Не получилось обработать запрос клиента");
                    e.printStackTrace();
                }
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();
    }
    
    
    /**
     * Класс для обработки запросов клиентов
     * Запускается отдельным потоком
     */
    private class ClientTask implements Runnable {
        
        // сокет получаемй от клиента
        private final Socket clientSocket;
        private final MainView mainView;
        
        /**
         * Конструктор для обработчика клиентских запросов
         * @param clientSocket - сокет получаемый от пользователя
         * @param mainView  - главный класс графического интерфейса
         */
        private ClientTask(Socket clientSocket, MainView mainView) {
            this.clientSocket = clientSocket;
            this.mainView = mainView;
        }

        @Override
        public void run() {
            System.out.println("Получен новый запрос!");            
            
            try {
                InputStream is = clientSocket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String input;
                
                String from = null;
                String date = null;
                String message = "";
                
                String firstLine = br.readLine();
                
                // обрабатываем отчет
                if(firstLine.startsWith("REPORT")){                    
                    System.out.println("Получен новый отчет");
                    
                    // время когда был выслан отчет
                    final String dateTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                    String[] lines = {"",""};
                    int count = 0;
                    
                    while((input = br.readLine()) != null){
                        if(input.equals(";"))
                            count++;
                        else
                            lines[count] += input + "\n";
                    }
                    
                    // IP адрес от куда был выслан отчет
                    final String from_ = lines[0];
                    
                    // Содержимое отчета
                    final String message_ = lines[1];

                    // в отдельном потоке (по завершению всех остальных потоков)
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run(){
                            Report newReport = new Report(from_, dateTime, message_);
                            // записываем полученный отчет в БД
                            SQLiteDB.addReport(newReport);
                            // обновляем графический интерфейс
                            mainView.reportsView.setListItems(SQLiteDB.getAllReports());
                            // открываем уведомление для админа о том что получен новый отчет
                            new Alert(AlertType.INFORMATION, "Получен новый отчет").showAndWait();
                        }
                    });
                    
                }
                // обрабатываем информацию о ПК
                else if(firstLine.startsWith("IP")){
                    
                    // IP адрем ПК с которого была отправлена информация о комплектующих
                    final String pcIP = firstLine.replaceAll("IP : ", "");
                    
                    System.out.println("Получена информация о ПК");
                    
                    String[] lines = {"","","","","","","","","",""};
                    int count = 0;
                    // обрабатываем информацию
                    while((input = br.readLine()) != null){                        
                        if(input.equals(";"))
                            count++;
                        else
                            lines[count] += input + "\n";
                    }
                    System.out.println("Count = " + count);
                    
                    final PCInfo pcInfo = new PCInfo(pcIP,lines[1],lines[2],lines[3],lines[4],lines[5],lines[6],lines[7],lines[8],lines[9]);
                    
                    // в отдельном потоке (по завершению всех остальных потоков)
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run(){
                            // проверяем существует ли информация о данном ПК в БД
                            if(SQLiteDB.checkPC(pcIP)){
                                System.out.println("Найден новый ПК. Идет запись комплектующих в БД");
                                // записываем инфо о ПК в БД
                                SQLiteDB.addPCInfo(pcInfo);
                                // обновляем графический интерфейс
                                mainView.setListItems(SQLiteDB.getAllPCInfos());
                            }
                            else{
                                System.out.println("Данные о данном ПК уже в БД");
                            }
                        }                     
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally{
                try{
                    clientSocket.close();
                }catch (IOException ex){
                    System.out.println("Ошибка : " + ex.getMessage());
                }
            }
        }        
    }
}
