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
public class MemoryInfo {
    
    private static final String command = "Get-WmiObject Win32_PhysicalMemory | Format-Table BankLabel";
    private static final String command2 = "Get-WmiObject Win32_PhysicalMemory | Format-Table @{Name=\"Capacity, GB\"; Expression={$_.Capacity/1GB}}";
    private static final String command3 = "Get-WmiObject Win32_PhysicalMemory | Format-Table Manufacturer";
    
    public String info;
    
    public MemoryInfo(){
        PowerShell powerShell = PowerShell.openSession();
        String output1 = powerShell.executeCommand(command).getCommandOutput();
        String output2 = powerShell.executeCommand(command2).getCommandOutput();
        String output3 = powerShell.executeCommand(command3).getCommandOutput();
        
        output1 = output1.trim();
        output2 = output2.trim();
        output3 = output3.trim();
        
        info = output1 + "\n\n" + output2 + "\n\n" + output3;
    }
    
    @Override
    public String toString(){
        return "Memory:\n" + info;
    }
    
}
