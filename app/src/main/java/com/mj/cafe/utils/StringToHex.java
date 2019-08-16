package com.mj.cafe.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class StringToHex {

    /**
     * 字符串转换为16进制字符串
     *
     * @param s
     * @return
     */
    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 16进制字符串转换为字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    /**
     * byte数组转16进制字符串
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }



    /**
     * java-16进制转字符串或者ASCII码
     *4F 4F 4F 4E - 》 OOON  （check响应）
     *
     * @param hex
     * @return
     */
    public static String convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        // 564e3a322d302e312e34 split into two characters 56, 4e, 3a...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            // grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            // convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            // convert the decimal to character
            sb.append((char) decimal);
            temp.append(decimal);
        }
        // System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * 字符串或者ASCII码  -   16进制
     * OOON  （check响应）  —》 4F 4F 4F 4E
     *
     * @param
     * @return
     */
    public static String convertStringToHex(String str) {

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }


    /**
     * 异或操作
     * @param data
     * @return
     */
    public static byte getXor(byte[] data){

        byte temp = data[0];
        for(int i=1;i<data.length;i++){
            temp^=data[i];
        }
        return temp;
    }
    /**
     * 16进制字符串转 byte[] _  10进制数组
     * "4F4F4F4F" ->  [0X4F,0X4F,0X4F,0X4F]
     * @return
     */

    public static byte[] hexStringToBytes10(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();  //
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * 16进制字符串转 byte[] 16进制数组
     * "4F4F4F4F" ->  [0X4F,0X4F,0X4F,0X4F]
     * @return
     */
    public static byte[] hexStringToBytes16(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 要对应起来，高4位在前面
            // 高4位对应的数值 X 16 + 低4位对应的数值
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
    protected static int toDigit(final char ch, final int index) {
        final int digit = Character.digit(ch, 16);
        return digit;
    }

    private static byte charToByte(char c) {  //
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 10进制转16进制
     * 29 ->1D
     * @param n
     * @return
     */
    public static String intToHex(Integer n) {
        StringBuffer s = new StringBuffer();
        String a;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(n != 0){
            s = s.append(b[n%16]);
            n = n/16;
        }
        a = s.reverse().toString();
        return a;
    }

    /**
     * 开头去0，加星号
     * @return
     */
    public static String createBankCardCode(String num){
        String newStr = num.replaceFirst("^0*", "");
        newStr = newStr + "**********";
        return newStr;
    }
}