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
public class BiosInfo {
    
    private static final String command = "Get-WmiObject Win32_bios";
    public String info;
    
    public BiosInfo(){
        PowerShell powerShell = PowerShell.openSession();
        String output = powerShell.executeCommand(command).getCommandOutput();
        output = output.trim();
        info = output;
    }
    
    @Override
    public String toString(){
        return "BIOS:\n" + info;
    }
    
}
