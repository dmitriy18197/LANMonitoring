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
public class AudioInfo {
    
    private static final String command = "Get-WmiObject win32_sounddevice | Format-Table Manufacturer";
    private static final String command1 = "Get-WmiObject win32_sounddevice | Format-Table Name";
    public String info;
    
    public AudioInfo(){
        PowerShell powerShell = PowerShell.openSession();
        String output = powerShell.executeCommand(command).getCommandOutput().trim();
        String output1 = powerShell.executeCommand(command1).getCommandOutput().trim();
        
        info = output + "\n\n" + output1;
    }
    
    @Override
    public String toString(){
        return "Sound Devices:\n" + info;
    }
    
}
