/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author MTaufiqAkbar
 */
public class Keyauth {

    static List<String> keywords = new ArrayList<>();
//    static String s3 = "Abv";

    public static void main(String[] args) {

    }
    
    public static boolean hasKey(String key) {
        return keywords.stream().filter(k -> key.contains(k)).collect(Collectors.toList()).size() > 0;
    }

}
