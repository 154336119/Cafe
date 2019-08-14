package com.mj.cafe.utils.print;

import com.lvrenyang.io.Pos;
import com.mj.cafe.bean.PrintEntity;
import com.mj.cafe.bean.PrintGoodsEntity;
import com.mj.cafe.utils.PrintUtils;

import java.io.UnsupportedEncodingException;

public class TaskEnPrint implements Runnable {
    Pos pos = null;
    PrintEntity printEntity = null;

    public TaskEnPrint(Pos pos, PrintEntity printEntity) {
        this.pos = pos;
        this.printEntity = printEntity;
    }

    public void printGoods() {
        if (printEntity.getGoodsList() != null && printEntity.getGoodsList().size() > 0) {
            for (PrintGoodsEntity entity : printEntity.getGoodsList()) {
                PrintGBKStr(PrintUtils.addSpc(entity.getName_ko(), entity.getNum() + "") + "\n", 0, 0, 0, 0, 0);//
            }
        }
    }

    public int PrintGBKStr(String StrUtf8, int nOrgx, int nWidthTimes, int nHeightTimes, int nFontType, int nFontStyle) {
        //原字符串函数里有乱码，仅用来设置格式
        pos.POS_S_TextOut("\r\n", nOrgx, nWidthTimes, nHeightTimes, nFontType, nFontStyle);
        //自己将UTF-8字符串转成GBK码，并调用底层函数发送字节
        String t = StrUtf8;
        try {
            int LenGBK;
            String utf8 = new String(t.getBytes("UTF-8"));
            String unicode = new String(utf8.getBytes(), "UTF-8");
            byte[] Bytegbk = unicode.getBytes("GBK");
            LenGBK = unicode.getBytes("GBK").length;
            return pos.GetIO().Write(Bytegbk, 0, LenGBK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return pos.GetIO().Write(StrUtf8.getBytes(), 0, StrUtf8.length());
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        boolean bPrintResult = false;
        byte[] status = new byte[1];
        if (pos.POS_QueryStatus(status, 2000, 2)) {
            pos.POS_FeedLine();
            pos.POS_S_Align(1);//居中对齐
            PrintGBKStr("[Order No:" + printEntity.getMeal_code() + "]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线0, 0);//2倍大，有下划线
            PrintGBKStr("[PW:" + printEntity.getMeal_pwd() + "]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线0);//2倍大，有下划线
            pos.POS_FeedLine();
            pos.POS_S_Align(0);//左对齐

            ////点餐内容
            PrintGBKStr(" [Order Time]" + printEntity.getCreate_time() + "\r\n", 0, 0, 0, 0, 0);//1倍大
            PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);//
            printGoods();
            pos.POS_S_Align(0);//左对齐
            PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
            PrintGBKStr(printEntity.getLicense_number() + printEntity.getContact() + "Tel." + printEntity.getTel() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr(printEntity.getAddress() + "\n", 0, 0, 0, 0, 0);
            pos.POS_FeedLine();
            PrintGBKStr("[Date of receipt]" + printEntity.getInvoice_date() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("[Number of receipt]" + printEntity.getInvoice_code() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("[Sales Details] --------------------------------\n", 0, 0, 0, 0, 0);
            PrintGBKStr(PrintUtils.addSpc("Sales Details:", printEntity.getTotal_money()) + "\n", 0, 0, 0, 0, 0);//
            PrintGBKStr(PrintUtils.addSpc("Tax:", printEntity.getTax_money()) + "\n", 0, 0, 0, 0, 0);//
            PrintGBKStr(PrintUtils.addSpc("Total:", printEntity.getPay_money()) + "\n", 0, 0, 0, 0, 0);//
            pos.POS_S_Align(0);//左对齐
            PrintGBKStr("[Payment Details] ------------------------------\n", 0, 0, 0, 0, 0);
            PrintGBKStr(PrintUtils.addSpc("Credit card:", "4,000") + "\n", 0, 0, 0, 0, 0);//
            pos.POS_S_Align(0);//左对齐
            PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
            PrintGBKStr("Card # : " + printEntity.getCard_number() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("Card com.:" + printEntity.getCard_company() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("instalments: " + printEntity.getStage_month() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("Approval #: " + printEntity.getApproval_number() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("Transaction Time: 2019-07-16 13:33:32\n", 0, 0, 0, 0, 0);
            PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
            PrintGBKStr("Koisk ID:" + printEntity.getDevice_no() + "]\n", 0, 0, 0, 0, 0);
            PrintGBKStr("Membership #:" + printEntity.getVip_card() + "]\n", 0, 0, 0, 0, 0);
            PrintGBKStr("Point        : " + printEntity.getIntegral() + "]\n", 0, 0, 0, 0, 0);
            pos.POS_FeedLine();
            pos.POS_S_Align(1);//居中
            PrintGBKStr("*****Thank you*****\n", 0, 0, 0, 0, 0);
            //	pos.POS_S_SetQRcode("https://appsto.re/cn/2KF_bb.i", 8, 0, 3);
            pos.POS_FeedLine();
            pos.POS_FeedLine();
            pos.POS_FeedLine();
            pos.POS_CutPaper();
            bPrintResult = pos.POS_QueryStatus(status, 2000, 2);
        }
//        final boolean bIsOpened = pos.GetIO().IsOpened();
//        if (bPrintResult) {
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    // TODO Auto-generated method stub
//                }
//
//            });
//        } else {
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    // TODO Auto-generated method stub
//                }
//
//            });
//        }
    }
}