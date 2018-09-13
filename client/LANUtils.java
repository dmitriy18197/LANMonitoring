/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lan_monitoringclient;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Класс для передачи информации по локальной сети
 * @author User
 */
public class LANUtils {
    
    public static String admin;
    private static int port = 6666;
    
    public static String getLocalIP() throws UnknownHostException{
        return InetAddress.getLocalHost().getHostAddress();
    }
    
    public static boolean checkAdminIP(String ip){
        Socket socket = null;
        
        boolean retVal = false;
        try {
            socket = new Socket(ip, port);
            retVal = true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {            
            try {
                socket.close();
            } catch(Exception e) { }
        }
        return retVal;
    }
    
    public static void sendReport(String msg){
        Socket socket = null;
        try {
            String pcName = PCInfo.getPCName();
            socket = new Socket(admin, port);
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write("REPORT:\n");
            bw.write(pcName + "\n;\n");
            bw.write(msg);
            bw.flush();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        finally{
            try{
                socket.close();
            } catch (Exception ex) {
                
            }
        }        
    }
    
    public static void sendPCInfo() throws IOException{
        Socket socket = new Socket(admin, port);
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        PCInfo pcInfo = new PCInfo();
        bw.write(pcInfo.toString());
        bw.flush();

        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Не удалось закрыть соединение");
        }
        
    }
}
