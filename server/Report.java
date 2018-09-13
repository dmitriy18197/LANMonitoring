/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lan_monitoring;

/**
 * Класс для хранения информации об отчетах
 * @author User
 */
public class Report {
    
    public String from;
    public String date;
    public String reportContent;
    
    public Report(String from, String date, String content){
        this.from = from;
        this.date = date;
        this.reportContent = content;
    }
    
    @Override
    public String toString(){
        return String.format("From : %s ;\n Message : \n", this.from, this.reportContent);
    }
    
}
