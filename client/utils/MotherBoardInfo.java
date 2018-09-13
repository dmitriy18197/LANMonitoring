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
public class MotherBoardInfo {
    
    private static final String command = "Get-WmiObject Win32_BaseBoard";
    
    public String info;
    
    public MotherBoardInfo(){
        PowerShell powerShell = PowerShell.openSession();
        String output = powerShell.executeCommand(command).getCommandOutput();
        output = output.trim();
        this.info = output;        
    }
    
    @Override
    public String toString(){
        return "Материнская плата:\n" + info;
    }
    
}
