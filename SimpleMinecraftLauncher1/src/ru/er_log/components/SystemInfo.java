/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.er_log.components;

/**
 *
 * @author GoodPC
 */
public class SystemInfo {
    
    public static boolean SystemInfo(){
        
        String os = System.getProperty("os.name").toLowerCase();
        
        return (os.indexOf( "mac" ) >= 0);
    }
    
    public static boolean SystemUnix(){
        
        String os = System.getProperty("os.name").toLowerCase();
        
        return (os.indexOf( "unix" ) >= 0);
    }    
}    

