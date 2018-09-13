/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lan_monitoringclient.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DmitryB
 */
public class HDDsInfo {
    
    public int numOfPartitions;
    public List<String> names = new ArrayList<String>();
    public List<String> sizes = new ArrayList<String>();
    
    public HDDsInfo(){
        int partitions = 0;
        File[] hdds = File.listRoots();
        if(hdds != null && hdds.length > 0){
            for(File hdd : hdds){
                String path = hdd.getPath();
                long size = hdd.getTotalSpace();
                names.add(path);
                sizes.add(formatToGb(size));
                partitions++;
            }
        }
        numOfPartitions = partitions;
    }
    
    private static String formatToGb(long value){
        int gb = (int) Math.ceil(value/Math.pow(1024, 3));
        return String.valueOf(gb);
    }
    
    @Override
    public String toString(){
        String str = "";
        for(int i=0 ; i < numOfPartitions ; i++){
            str+= String.format("%s: %s Гб.\n", names.get(i), sizes.get(i));
        }
        return str;
    }
    
}
