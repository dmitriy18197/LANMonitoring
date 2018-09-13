/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lan_monitoringclient;

import com.mycompany.lan_monitoringclient.utils.AudioInfo;
import com.mycompany.lan_monitoringclient.utils.BiosInfo;
import com.mycompany.lan_monitoringclient.utils.CPUInfo;
import com.mycompany.lan_monitoringclient.utils.DesktopMonitorInfo;
import com.mycompany.lan_monitoringclient.utils.GPUInfo;
import com.mycompany.lan_monitoringclient.utils.HDDsInfo;
import com.mycompany.lan_monitoringclient.utils.MemoryInfo;
import com.mycompany.lan_monitoringclient.utils.MotherBoardInfo;
import com.mycompany.lan_monitoringclient.utils.OSInfo;
import com.profesorfalken.jpowershell.PowerShell;

/**
 * Класс для хранения информации о комплектующих ПК
 * @author User
 */
public class PCInfo {
    
    // информация о комплектующих ПК
    public String pcName;
    public OSInfo os;
    public MotherBoardInfo motherBoard;
    public CPUInfo cpu;
    public MemoryInfo memory;
    public HDDsInfo hdd;
    public GPUInfo gpu;
    public DesktopMonitorInfo monitor;
    public AudioInfo audio;
    public BiosInfo bios;
    
    public PCInfo(){
        this.pcName = getPCName();
        os = new OSInfo();
        motherBoard = new MotherBoardInfo();
        cpu = new CPUInfo();
        memory = new MemoryInfo();
        hdd = new HDDsInfo();
        gpu = new GPUInfo();
        monitor = new DesktopMonitorInfo();
        audio = new AudioInfo();
        bios = new BiosInfo();        
    }
    
    @Override
    public String toString(){
        return "IP : " + pcName.trim() + "\n;\n" + 
                os.info.trim() + "\n;\n" + 
                motherBoard.info.trim() + "\n;\n" + 
                cpu.info.trim() + "\n;\n" + 
                memory.info.trim() + "\n;\n" + 
                hdd.toString() + "\n;\n" + 
                gpu.toString() + "\n;\n" + 
                monitor.toString() + "\n;\n" + 
                audio.info.trim() + "\n;\n" + 
                bios.info.trim() + "\n"; 
    }
    
    public static String getPCName(){
        PowerShell powerShell = PowerShell.openSession();
        String command = "Get-WmiObject Win32_OperatingSystem | select CSName";
        String output = powerShell.executeCommand(command).getCommandOutput();
        output = output.replaceAll("CSName", "");
        output = output.replaceAll("-", "");
        output = output.trim();
        return output;
    }
}
