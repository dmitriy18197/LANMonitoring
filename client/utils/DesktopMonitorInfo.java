/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lan_monitoringclient.utils;

import com.profesorfalken.jpowershell.PowerShell;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DmitryB
 */
public class DesktopMonitorInfo {
    
    private static final String command = "Get-WmiObject Win32_DesktopMonitor | Select-Object MonitorManufacturer";
    private static final String command2 = "Get-WmiObject Win32_DesktopMonitor | Format-Table ScreenWidth,ScreenHeight";
    
    
    
    public List<String> listOfMonitors = new ArrayList<>();
    public static String info;
    
    public DesktopMonitorInfo(){
        PowerShell powerShell = PowerShell.openSession();
        String output1 = powerShell.executeCommand(command).getCommandOutput().trim();
        String output2 = powerShell.executeCommand(command2).getCommandOutput().trim();
        
        info = output1 + "\n\n" + output2;
    }
    
    @Override
    public String toString(){
        return info;
    }
    
}
