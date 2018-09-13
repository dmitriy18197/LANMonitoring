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
public class CPUInfo {
    
    private static final String command = "Get-WmiObject Win32_Processor | select Manufacturer";
    private static final String command1 = "Get-WmiObject Win32_Processor | select Name";
    private static final String command2 = "Get-WmiObject Win32_Processor | select NumberOfCores";
    private static final String command3 = "Get-WmiObject Win32_Processor | select DeviceID";
    public String info;
    
    public CPUInfo(){
        PowerShell powerShell = PowerShell.openSession();
        String output1 = powerShell.executeCommand(command).getCommandOutput().trim();
        String output2 = powerShell.executeCommand(command1).getCommandOutput().trim();
        String output3 = powerShell.executeCommand(command2).getCommandOutput().trim();
        String output4 = powerShell.executeCommand(command3).getCommandOutput().trim();
        this.info = output1 + "\n\n" + output2 + "\n\n"+ output3 + "\n\n"+ output4;
    }
    
    @Override
    public String toString(){
        return "CPU:\n" + info;
    }
    
}
