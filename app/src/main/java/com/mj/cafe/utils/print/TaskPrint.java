package com.mj.cafe.utils.print;

import com.lvrenyang.io.Pos;
import com.mj.cafe.bean.PrintEntity;
import com.mj.cafe.bean.PrintGoodsEntity;
import com.mj.cafe.utils.PrintUtils;

import java.io.UnsupportedEncodingException;

public class TaskPrint implements Runnable {
    Pos pos = null;
    PrintEntity printEntity = null;


    public TaskPrint(Pos pos, PrintEntity printEntity) {
        this.pos = pos;
        this.printEntity = printEntity;
    }

    private void changeChinese() {
        byte[] korea = new byte[]{12, 90, 00};
        pos.GetIO().Write(korea, 0, 3);
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

    public void printGoods() {
        if (printEntity.getGoodsList() != null && printEntity.getGoodsList().size() > 0) {
            for (PrintGoodsEntity entity : printEntity.getGoodsList()) {
                PrintGBKStr(PrintUtils.addSpc(entity.getName(), entity.getNum() + "") + "\n", 0, 0, 0, 0, 0);//
            }
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        boolean bPrintResult = false;
        byte[] status = new byte[1];
//        changeChinese();
        if (pos.POS_QueryStatus(status, 2000, 2)) {
            pos.POS_FeedLine();
            pos.POS_S_Align(1);//居中对齐
                PrintGBKStr("[点单号: " + printEntity.getMeal_code() + "]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
            PrintGBKStr("[点餐密码: " + printEntity.getMeal_pwd() + "]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
            pos.POS_FeedLine();
            pos.POS_S_Align(0);//左对齐
            ////点餐内容
            PrintGBKStr(" [点单时间] " + printEntity.getCreate_time() + "\r\n", 0, 0, 0, 0, 0);//1倍大
            PrintGBKStr("-----------------------------------------------\n", 0, 0, 0, 0, 0);//
            PrintGBKStr(PrintUtils.addSpc("商品名", "数量") + "\n", 0, 0, 0, 0, 0);//
            printGoods();
            pos.POS_S_Align(0);//左对齐
            PrintGBKStr("-----------------------------------------------\n", 0, 0, 0, 0, 0);
            PrintGBKStr(printEntity.getStore_name() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr(printEntity.getLicense_number() + printEntity.getContact() + "Tel." + printEntity.getTel() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr(printEntity.getAddress() + "\n", 0, 0, 0, 0, 0);
            pos.POS_FeedLine();
            PrintGBKStr("[发票发行日]" + printEntity.getInvoice_date() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("[发票号码]" + printEntity.getInvoice_code() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("[销售明细] -------------------------------------\n", 0, 0, 0, 0, 0);
            PrintGBKStr(PrintUtils.addSpc("销 售 明 细:", printEntity.getTotal_money()) + "\n", 0, 0, 0, 0, 0);//
            PrintGBKStr(PrintUtils.addSpc("附  加   税:", printEntity.getTax_money()) + "\n", 0, 0, 0, 0, 0);//
            PrintGBKStr(PrintUtils.addSpc("总       计:", printEntity.getPay_money()) + "\n", 0, 0, 0, 0, 0);//
            pos.POS_S_Align(0);//左对齐
            PrintGBKStr("[结算明细] -------------------------------------\n", 0, 0, 0, 0, 0);
            PrintGBKStr(PrintUtils.addSpc(printEntity.getPay_type()+":", printEntity.getPay_money()) + "\n", 0, 0, 0, 0, 0);//
            pos.POS_S_Align(0);//左对齐
            PrintGBKStr("-----------------------------------------------\n", 0, 0, 0, 0, 0);
            PrintGBKStr("卡 片 号 码: " + printEntity.getCard_number() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("卡 片 公 司: " + printEntity.getCard_company() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("分 期 月 数: " + printEntity.getStage_month() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("批 准 号 码:" + printEntity.getApproval_number() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("交 易 时 间:" + printEntity.getCreate_time() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("-----------------------------------------------\n", 0, 0, 0, 0, 0);
            PrintGBKStr("点 餐 机 ID:" + printEntity.getDevice_no() + "]\n", 0, 0, 0, 0, 0);
            PrintGBKStr("会 员 卡 号:" + printEntity.getVip_card() + "\n", 0, 0, 0, 0, 0);
            PrintGBKStr("累 计 积 分:" + printEntity.getIntegral() + "\n", 0, 0, 0, 0, 0);
            pos.POS_FeedLine();
            pos.POS_S_Align(1);//居中
            PrintGBKStr("*****感谢您的使用*****\n", 0, 0, 0, 0, 0);
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
//                }
//
//            });
//        } else {
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                }
//            });
//        }

    }

}
