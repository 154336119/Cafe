package com.mj.cafe.utils.print;

import android.widget.Toast;

import com.lvrenyang.io.Pos;
import com.mj.cafe.bean.PrintEntity;
import com.mj.cafe.bean.PrintGoodsEntity;
import com.mj.cafe.utils.PrintUtils;

import java.io.UnsupportedEncodingException;

public class TaskKoPrint implements Runnable {
    PrintEntity printEntity = null;
    Pos pos = null;

    public TaskKoPrint(Pos pos, PrintEntity printEntity) {
        this.pos = pos;
        this.printEntity = printEntity;
    }

    public void printGoods() {
        if (printEntity.getGoodsList() != null && printEntity.getGoodsList().size() > 0) {
            for (PrintGoodsEntity entity : printEntity.getGoodsList()) {
                PrintKSCStr(PrintUtils.addSpc(entity.getName(), entity.getNum() + "") + "\n", 0, 0, 0, 0, 0);//
            }
        }
    }

    public int PrintKSCStr(String StrUtf8, int nOrgx, int nWidthTimes, int nHeightTimes, int nFontType, int nFontStyle) {
        //原字符串函数里有乱码，仅用来设置格式
        byte[] korea = new byte[]{(byte) 0x12, (byte) 0x90, (byte) 0x01};
        pos.GetIO().Write(korea, 0, 3);
        pos.POS_S_TextOut(StrUtf8, nOrgx, nWidthTimes, nHeightTimes, nFontType, nFontStyle);
        //自己将UTF-8字符串转成KSC5601码，并调用底层函数发送字节
        String t = StrUtf8;
        try {
            int LenGBK;
            String utf8 = new String(t.getBytes("UTF-8"));
            String unicode = new String(utf8.getBytes(), "UTF-8");
            byte[] Bytegbk = unicode.getBytes("KSC5601");
            LenGBK = unicode.getBytes("KSC5601").length;
            return pos.GetIO().Write(Bytegbk, 0, LenGBK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return pos.GetIO().Write(StrUtf8.getBytes(), 0, StrUtf8.length());
        }
    }

    private void changeKro() {
        byte[] korea = new byte[]{12, 90, 01};
        pos.GetIO().Write(korea, 0, 3);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        boolean bPrintResult = false;
        byte[] status = new byte[1];
        changeKro();
        if (pos.POS_QueryStatus(status, 2000, 2)) {
            pos.POS_FeedLine();
            pos.POS_S_Align(1);//居中对齐

            PrintKSCStr("[주문번호:" + printEntity.getMeal_code() + "]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
            PrintKSCStr("[비밀번호:" + printEntity.getMeal_pwd() + "]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
            pos.POS_FeedLine();
            pos.POS_S_Align(0);//左对齐
            ////点餐内容
            PrintKSCStr(" [주문일시] " + printEntity.getCreate_time() + "\r\n", 0, 0, 0, 0, 0);//1倍大
            PrintGBKStr("-----------------------------------------------\n", 0, 0, 0, 0, 0);
            PrintKSCStr(PrintUtils.addSpc("상품명", "수량") + "\n", 0, 0, 0, 0, 0);//
            printGoods();
            PrintGBKStr("-----------------------------------------------\n", 0, 0, 0, 0, 0);
            PrintKSCStr(printEntity.getStore_name() + "\n", 0, 0, 0, 0, 0);
            PrintKSCStr(printEntity.getLicense_number() + printEntity.getContact() + "Tel." + printEntity.getTel() + "\n", 0, 0, 0, 0, 0);
            PrintKSCStr(printEntity.getAddress() + "\n", 0, 0, 0, 0, 0);
            pos.POS_FeedLine();
            PrintKSCStr("[영수증 발행일]" + printEntity.getInvoice_date() + "\n", 0, 0, 0, 0, 0);
            PrintKSCStr("[영수증 번호] " + printEntity.getInvoice_code() + "\n", 0, 0, 0, 0, 0);
            PrintKSCStr("[판매내역] --------------------------------\n", 0, 0, 0, 0, 0);
            PrintKSCStr(PrintUtils.addSpc("판 매 내 역:", printEntity.getTotal_money()) + "\n", 0, 0, 0, 0, 0);//0);//
            PrintKSCStr(PrintUtils.addSpc("부  가   세:", printEntity.getTax_money()) + "\n", 0, 0, 0, 0, 0);//0);//
            PrintKSCStr(PrintUtils.addSpc("합       계:", printEntity.getPay_money()) + "\n", 0, 0, 0, 0, 0);//
            PrintKSCStr("[결제내역] -----------------------------------\n", 0, 0, 0, 0, 0);
            PrintKSCStr(PrintUtils.addSpc(printEntity.getPay_type()+":", printEntity.getPay_money()) + "\n", 0, 0, 0, 0, 0);//
            PrintGBKStr("-----------------------------------------------\n", 0, 0, 0, 0, 0);
            PrintKSCStr("카 드 번 호: " + printEntity.getCard_number() + "\n", 0, 0, 0, 0, 0);
            PrintKSCStr("매 입 사 명:" + printEntity.getCard_company() + "\n", 0, 0, 0, 0, 0);
            PrintKSCStr("할 부 개 월:" + printEntity.getStage_month() + "\n", 0, 0, 0, 0, 0);
            PrintKSCStr("승 인 번 호:" + printEntity.getApproval_number() + "\n", 0, 0, 0, 0, 0);
            PrintKSCStr("거 래 일 시:" + printEntity.getCreate_time() + "\n", 0, 0, 0, 0, 0);
            PrintKSCStr("-----------------------------------------------\n", 0, 0, 0, 0, 0);
            PrintKSCStr("주문기기 ID:" + printEntity.getDevice_no() + "]\n", 0, 0, 0, 0, 0);
            PrintKSCStr("회 원 번 호:" + printEntity.getVip_card() + "\n", 0, 0, 0, 0, 0);
            PrintKSCStr("적립 포인트:" + printEntity.getIntegral() + "\n", 0, 0, 0, 0, 0);
            pos.POS_FeedLine();
            pos.POS_S_Align(1);//居中
            PrintKSCStr("*****감사합니다*****\n", 0, 0, 0, 0, 0);
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
//                    Toast.makeText(mActivity.getApplicationContext(), "打印成功", Toast.LENGTH_SHORT).show();
//                }
//
//            });
//        } else {
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    // TODO Auto-generated method stub
//                    Toast.makeText(mActivity.getApplicationContext(), "打印失败", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
    }

    public int PrintGBKStr(String StrUtf8, int nOrgx, int nWidthTimes, int nHeightTimes, int nFontType, int nFontStyle) {
        //原字符串函数里有乱码，仅用来设置格式
        byte[] korea = new byte[]{(byte) 0x12, (byte) 0x90, (byte) 0x00};
        pos.GetIO().Write(korea, 0, 3);
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
}