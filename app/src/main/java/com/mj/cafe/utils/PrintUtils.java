package com.mj.cafe.utils;

import com.orhanobut.logger.Logger;

public class PrintUtils {

    public static String addSpc(String text,String money){
        int test = 0;
        int len = 576 - sort(text) - sort(money);
        int spcLen = len/12;
        StringBuffer sb = new StringBuffer();
        sb.append(text);
        for(int i =0;i<spcLen;i++){
            sb.append(" ");
        }
        sb.append(money);
        return sb.toString();
    }

    public static int sort(String str){
        int len = 0;
        char charArray[] = str.toCharArray();//利用toCharArray方法转换
        for(int i=0;i<charArray.length;i++){
            String s = String.valueOf(charArray[i]);
            if(s.equals(",")
                    || s.equals(" ")
                    ||s.equals(":")|| s.equals("1") || s.equals("2") || s.equals("3") || s.equals("4") || s.equals("5") || s.equals("6")
                    || s.equals("7") || s.equals("8") || s.equals("9") || s.equals("0")
                    || s.equals("a") || s.equals("b") || s.equals("c") || s.equals("d") || s.equals("e") || s.equals("f") || s.equals("g")
                    || s.equals("h") || s.equals("i") || s.equals("j") || s.equals("k") || s.equals("l") || s.equals("m") || s.equals("n")
                    || s.equals("o") || s.equals("p") || s.equals("q") || s.equals("r") || s.equals("s") || s.equals("t") || s.equals("u")
                    || s.equals("v") || s.equals("w") || s.equals("x") || s.equals("y") || s.equals("z")
                    || s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") || s.equals("E") || s.equals("F") || s.equals("G")
                    || s.equals("H") || s.equals("I") || s.equals("J") || s.equals("K") || s.equals("L") || s.equals("M") || s.equals("N")
                    || s.equals("O") || s.equals("P") || s.equals("Q") || s.equals("R") || s.equals("S") || s.equals("T") || s.equals("U")
                    || s.equals("V") || s.equals("W") || s.equals("X") || s.equals("Y") || s.equals("Z")){
                len  = len + 12;
            }else{
                len  = len + 24;
            }

        }
        return len;
    }
}
