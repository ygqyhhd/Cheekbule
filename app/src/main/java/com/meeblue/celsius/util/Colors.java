package com.meeblue.celsius.util;

public class Colors {
    public static int toHexColor(int red, int green, int blue){
        if(red>255) red = 255;
        if(green>255) green = 255;
        if(blue>255) blue = 255;
        if(red<0) red = 0;
        if(green<0) green = 0;
        if(blue<0) blue = 0;
        String r = checkColorValue(red);
        String g = checkColorValue(green);
        String b = checkColorValue(blue);
        String str =r+g+b+"";
        if(str.length()<7) str = "#000000";
        return Integer.valueOf(str);
    }
    private static String checkColorValue(int value){
        String str = "";
        if(value<16){
            str ="0" + Integer.toHexString(value);
            return str;
        }
        return Integer.toHexString(value);
    }
}
