package com.mj.cafe.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.mj.cafe.utils.StringToHex.bytesToHexString;
import static com.mj.cafe.utils.StringToHex.getXor;

public class PortUtils {

    //请求Job_code——设备确认
    public static final String JOB_CODE_CHECK = "41";
    //请求Job_code——交易确认
    public static final String JOB_CODE_CONFIRME = "42";
    //返回Job_code——设备确认
    public static final String JOB_RESPONSE_CODE_CHECK = "61";
    //返回Job_code——交易确认
    public static final String JOB_RESPONSE_CODE_CONFIRME = "62";

//    //返回Job_code——交易确认
//    public static final String JOB_RESPONSE_CODE_CONFIRME = "62";

    //返回码
    public static final String RESPONSE_SUCCESS= "00";
    //未登录 Terminal ID
    public static final String RESPONSE_01= "01";
    //传送日时错误
    public static final String RESPONSE_02= "02";
    //未定义的Job Code
    public static final String RESPONSE_03= "03";
    //Data Length 错误
    public static final String RESPONSE_04= "04";
    //ETX 错误
    public static final String RESPONSE_05= "05";
    //BCC 错误
    public static final String RESPONSE_06= "06";

    //起始标志
    public static final String stx =  "02";
    //terminaloID
    public static final String terminaloID = "00000000000000000000000000000000";
    //默认请求数据长度
    public static final String def_date_code = "0000";//两位
    //结束标识
    public static final String etx = "03";//结束标识
    //异或值 STX ~ ETX
    public static String bbc = null;
    /**
     * 设备检查
     * HEADER FORMAT : STX[1] + Terminal ID[16] + DateTime[14] + JobCode[1] + Response Code[1] + Data Length[2]
     * @return
     */
    public static byte[] check(){
        StringBuffer sb = new StringBuffer();
        sb.append(stx);
        sb.append(terminaloID);
        sb.append(StringToHex.convertStringToHex(getStringDateLong1()));
        sb.append(JOB_CODE_CHECK);
        sb.append(RESPONSE_SUCCESS);
        sb.append(def_date_code);
        sb.append(etx);
        bbc = getXor(StringToHex.hexStringToBytes10(sb.toString()))+"";
        bbc = StringToHex.intToHex(Integer.parseInt(bbc));
        sb.append(bbc);
        return  StringToHex.hexStringToBytes16(sb.toString());
    }

    /**
     * 交易确认
     * HEADER FORMAT : STX[1] + Terminal ID[16] + DateTime[14] + JobCode[1] + Response Code[1] + Data Length[2]
     * @return
     */
    public static byte[] confirm(String money){
        StringBuffer sb = new StringBuffer();
        sb.append(stx);
        sb.append(terminaloID);
        sb.append(StringToHex.convertStringToHex(getStringDateLong1()));
        sb.append(JOB_CODE_CONFIRME);
        sb.append("1E"); //30个字节（金额）
        sb.append(createData(money));
        sb.append(def_date_code);
        sb.append(etx);
        bbc = getXor(StringToHex.hexStringToBytes10(sb.toString()))+"";
        bbc = StringToHex.intToHex(Integer.parseInt(bbc));
        sb.append(bbc);
        return  StringToHex.hexStringToBytes16(sb.toString());
    }

    public static String createData(String money){
        //金额拼接
        //头部01
        //金额部分
        //尾部 30303030303030303030303030303030303031 （19个字节）
        String str = "01"+StringToHex.convertStringToHex(String.format("%010d", Integer.valueOf(money)))
                +"30303030303030303030303030303030303031";
        return str;
    }
    public static void checkResponse(byte[] data){
        String jobCode = null;
        String response_code =null;
        String hexStr = bytesToHexString(data);
        if(hexStr.length()>3){
            jobCode = hexStr.substring(62,64);
            response_code = hexStr.substring(64,66);
            if(response_code.equals(RESPONSE_SUCCESS)){
                if(jobCode.equals(JOB_RESPONSE_CODE_CHECK)){
                    //设备确认解析
                    String hexState = hexStr.substring(hexStr.length()-8,hexStr.length()-4);
                    Log.d("==================",hexState);
                    String state = StringToHex.convertHexToString(hexState);
                    if("OOOO".equals(state)){
                        //检查成功
                    }else{
                        //检查失败
                        Logger.d(state);
                    }

                }else if(jobCode.equals(JOB_RESPONSE_CODE_CONFIRME)){
                    //结算确认解析

                }
            }else{
//                //错误码
//                showToastMsg("Response_error:"+response_code);
            }
        }else{
            //插卡后的反应
            if(StringToHex.convertHexToString(hexStr).equals("@")){
                //执行结算确认
            }
        }


    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd HH:mm
     */
    public static String getStringDateLong1() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String dateString = formatter.format(currentTime);
        return dateString;
    }
}
