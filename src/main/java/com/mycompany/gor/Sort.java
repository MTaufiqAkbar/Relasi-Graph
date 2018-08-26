/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gor;

/**
 *
 * @author MTaufiqAkbar
 */
public class Sort implements Comparable<Sort>{
    
    int idx, val;
    
    Sort(int idx, int val){
        this.idx=idx;
        this.val=val;
    }
    
    public int compareTo(Sort s){
        return this.val - s.val;
    }


    
}
