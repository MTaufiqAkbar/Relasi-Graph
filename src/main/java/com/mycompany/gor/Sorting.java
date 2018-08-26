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
public class Sorting implements Comparable<Sorting> {

    int idx;
    int val;

    Sorting(int idx, int val) {
        this.idx = idx;
        this.val = val;
    }

    public int compareTo(Sorting c) {
        return this.val - c.val;
    }
}
