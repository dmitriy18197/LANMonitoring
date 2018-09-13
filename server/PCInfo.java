/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lan_monitoring;

/**
 * Класс для хранения информации о комплектующих ПК
 * @author DmitryB
 */
public class PCInfo {
    
    public String ipAddress, os, motherBoard, cpu, memory, hdd, gpu, monitor, audio, bios;
    
    public PCInfo(String ip, String os, String mb, String cpu, 
            String ram, String hdd, String gpu, String monitor, String audio, String bios){
        
        this.ipAddress = ip;
        this.os = os.trim();
        this.motherBoard = mb.trim();
        this.cpu = cpu;
        this.memory = ram;
        this.hdd = hdd;
        this.gpu = gpu;
        this.monitor = monitor;
        this.audio = audio;
        this.bios = bios;
    }
    
    
    // TODO
    @Override
    public String toString(){
        return "IP : " + ipAddress + "\n\n" + 
                "Операционная система : \n" + os.toString() + "\n\n" + 
                "Материнская плата : \n" + motherBoard.toString() + "\n\n" + 
                "CPU : \n" + cpu.toString() + "\n\n" + 
                "ОЗУ : \n" + memory.toString() + "\n\n" + 
                "HDD : \n" + hdd.toString() + "\n\n" + 
                "Видеокарты : \n" + gpu.toString() + "\n\n" + 
                "Монитор : \n" + monitor.toString() + "\n\n" + 
                "Аудио : \n" + audio.toString() + "\n\n" + 
                "BIOS : \n" + bios.toString() + "\n"; 
    }
    
}
