/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lan_monitoringclient.utils;

import com.profesorfalken.jpowershell.PowerShell;

/**
 *
 * @author DmitryB
 */
public class OSInfo {
    
    private static final String command = "Get-WmiObject Win32_OperatingSystem | select ";
    private static final String name = "Name";
    private static final String psComputerName = "PSComputerName";
    private static final String buildNumber = "BuildNumber";
    private static final String buildType = "BuildType";
    private static final String caption = "Caption";
    private static final String manufacturer = "Manufacturer";
    private static final String serialNumber = "SerialNumber";
    private static final String versoin = "Version";
    public String info = "";
    
    public OSInfo(){
        PowerShell powerShell = PowerShell.openSession();
        
        String cmd = command+name;        
        String output = powerShell.executeCommand(cmd).getCommandOutput();
        this.info += output.trim() + "\n\n";
        
        cmd = command+psComputerName;        
        output = powerShell.executeCommand(cmd).getCommandOutput();
        this.info += output.trim() + "\n\n";
        
        cmd = command+buildNumber;        
        output = powerShell.executeCommand(cmd).getCommandOutput();
        this.info += output.trim() + "\n\n";
        
        cmd = command+buildType;        
        output = powerShell.executeCommand(cmd).getCommandOutput();
        this.info += output.trim() + "\n\n";
        
        cmd = command+caption;        
        output = powerShell.executeCommand(cmd).getCommandOutput();
        this.info += output.trim();
        
    }
}
