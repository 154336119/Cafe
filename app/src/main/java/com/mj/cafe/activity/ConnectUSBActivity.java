package com.mj.cafe.activity;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lvrenyang.io.USBPrinting;
import com.lvrenyang.io.IOCallBack;
import com.lvrenyang.io.Pos;
import com.mj.cafe.R;
import com.mj.cafe.utils.PrintUtils;
import com.orhanobut.logger.Logger;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class ConnectUSBActivity extends Activity implements OnClickListener, IOCallBack {

    private LinearLayout linearlayoutdevices;

    Button btnDisconnect, btnPrint;
    static ConnectUSBActivity mActivity;

    ExecutorService es = Executors.newScheduledThreadPool(30);
    Pos mPos = new Pos();
    USBPrinting mUsb = new USBPrinting();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectusb);

        mActivity = this;

        linearlayoutdevices = (LinearLayout) findViewById(R.id.linearlayoutdevices);

        btnDisconnect = (Button) findViewById(R.id.buttonDisconnect);
        btnPrint = (Button) findViewById(R.id.buttonPrint);
        btnDisconnect.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnDisconnect.setEnabled(false);
        btnPrint.setEnabled(false);

        mPos.Set(mUsb);
        mUsb.SetCallBack(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            probe();
        } else {
            finish();
        }

//        Logger.d("판 매 내 역:_"+"판 매 내 역:".length());
//        Logger.d("판매내역:_"+"판매내역:".length());
//        Logger.d("합       계:_"+"합       계:".length());
//        Logger.d("합계:_","합계:".length());
    }


    public String addSpc(String text,String money){
        int test = 0;
        int len = 576 - sor1t(text) - sor1t(money);
        int spcLen = len/12;
        StringBuffer sb = new StringBuffer();
        sb.append(text);
        for(int i =0;i<spcLen;i++){
            sb.append(" ");
            test = test+ 1;
        }
        sb.append(money);
//            int spcLen = 24 - text.length() - money.length();
//            StringBuffer sb = new StringBuffer();
//            sb.append(text);
//            for(int i =0;i<spcLen;i++){
//                sb.append("  ");
//            }
//            sb.append(money);
        Logger.d("=============test:"+test);
        return sb.toString();
    }

    private int sor1t(String str){
        int len = 0;
        char charArray[] = str.toCharArray();//利用toCharArray方法转换
        for(int i=0;i<charArray.length;i++){
            String s = String.valueOf(charArray[i]);
            if(s.equals(",")
                    || s.equals(" ")
                    ||s.equals(":")|| s.equals("1") || s.equals("2") || s.equals("3") || s.equals("4") || s.equals("5") || s.equals("6")
                    || s.equals("7") || s.equals("8") || s.equals("9") || s.equals("0")){
                len  = len + 12;
            }else{
                len  = len + 24;
            }

        }
        return len;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnDisconnect.performClick();
    }

    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {

            case R.id.buttonDisconnect:
                es.submit(new TaskClose(mUsb));
                break;

            case R.id.buttonPrint:
                btnPrint.setEnabled(false);
//                es.submit(new TaskPrint(mPos));
                es.submit(new TaskEnPrint(mPos));
//                es.submit(new TaskKoPrint(mPos));
                break;

            default:
                break;

        }

    }

    private void probe() {
        linearlayoutdevices.removeAllViews();
        final UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        if (deviceList.size() > 0) {
            // 初始化选择对话框布局，并添加按钮和事件
            while (deviceIterator.hasNext()) { // 这里是if不是while，说明我只想支持一种device
                final UsbDevice device = deviceIterator.next();
                Toast.makeText(this, "" + device.getDeviceId() + device.getDeviceName() + device.toString(), Toast.LENGTH_LONG).show();
                Button btDevice = new Button(
                        linearlayoutdevices.getContext());
                btDevice.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                btDevice.setGravity(android.view.Gravity.CENTER_VERTICAL
                        | Gravity.LEFT);
                btDevice.setText(String.format(" VID:%04X PID:%04X",
                        device.getVendorId(), device.getProductId()));
                btDevice.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        PendingIntent mPermissionIntent = PendingIntent
                                .getBroadcast(
                                        ConnectUSBActivity.this,
                                        0,
                                        new Intent(
                                                ConnectUSBActivity.this
                                                        .getApplicationInfo().packageName),
                                        0);
                        if (!mUsbManager.hasPermission(device)) {
                            mUsbManager.requestPermission(device,
                                    mPermissionIntent);
                            Toast.makeText(getApplicationContext(),
                                    "没有权限", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            Toast.makeText(mActivity, "Connecting...", Toast.LENGTH_SHORT).show();
                            linearlayoutdevices.setEnabled(false);
                            for (int i = 0; i < linearlayoutdevices.getChildCount(); ++i) {
                                Button btn = (Button) linearlayoutdevices.getChildAt(i);
                                btn.setEnabled(false);
                            }
                            btnDisconnect.setEnabled(false);
                            btnPrint.setEnabled(false);
                            es.submit(new TaskOpen(mUsb, mUsbManager, device));
                        }
                    }
                });
                linearlayoutdevices.addView(btDevice);
            }
        }
    }


    public static class TaskOpen implements Runnable {
        USBPrinting usb = null;
        UsbManager usbManager = null;
        UsbDevice usbDevice = null;

        public TaskOpen(USBPrinting usb, UsbManager usbManager, UsbDevice usbDevice) {
            this.usb = usb;
            this.usbManager = usbManager;
            this.usbDevice = usbDevice;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            usb.Open(usbManager, usbDevice);
        }
    }

    static int dwWriteIndex = 1;

    public static class TaskPrint implements Runnable {
        Pos pos = null;

        public TaskPrint(Pos pos) {
            this.pos = pos;
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

                PrintGBKStr("[点单号: 0001]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
                PrintGBKStr("[点餐密码: 9527]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
                pos.POS_FeedLine();
                pos.POS_S_Align(0);//左对齐

                ////点餐内容
                PrintGBKStr(" [点单时间] 2019-07-18[二] 13:33:43\r\n", 0, 0, 0, 0, 0);//1倍大
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("商品名","数量")+"\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("美式咖啡","1")+"\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("雪顶咖啡","1")+"\n", 0, 0, 0, 0, 0);//
                pos.POS_S_Align(0);//左对齐
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Smart Green Cafe\n", 0, 0, 0, 0, 0x08);
                PrintGBKStr("282-34-00719 李东载 Tel. 010-2505-8844\n", 0, 0, 0, 0, 0);
                PrintGBKStr("大田广域市儒城区Guryongdaljeon路396号1层(屯谷洞)\n", 0, 0, 0, 0, 0);
                pos.POS_FeedLine();
                PrintGBKStr("[发票发行日] 2019-07-16 13:33;32\n", 0, 0, 0, 0, 0);
                PrintGBKStr("[发票号码] 190716133321523-0000\n", 0, 0, 0, 0, 0);
                PrintGBKStr("[销售明细] -------------------------------------\n", 0, 0, 0, 0, 0);
                PrintGBKStr(PrintUtils.addSpc("销 售 明 细:","3,636")+"\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("附  加   税:","364")+"\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("总       计:","4,000")+"\n", 0, 0, 0, 0, 0);//
                pos.POS_S_Align(0);//左对齐
                PrintGBKStr("[结算明细] -------------------------------------\n", 0, 0, 0, 0, 0);
                PrintGBKStr(PrintUtils.addSpc("信 用 卡 片:","4,000")+"\n", 0, 0, 0, 0, 0);//
                pos.POS_S_Align(0);//左对齐
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintGBKStr("卡 片 号 码: 6251-20**-****-****\n", 0, 0, 0, 0, 0);
                PrintGBKStr("卡 片 公 司: BC发卡公司\n", 0, 0, 0, 0, 0);
                PrintGBKStr("分 期 月 数: 00\n", 0, 0, 0, 0, 0);
                PrintGBKStr("批 准 号 码: 33593898\n", 0, 0, 0, 0, 0);
                PrintGBKStr("交 易 时 间: 2019-07-16 13:33:32\n", 0, 0, 0, 0, 0);
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintGBKStr("点 餐 机 ID: 7142962002 – Kiosk01]\n", 0, 0, 0, 0, 0);
                PrintGBKStr("会 员 卡 号: 010-2505-8844\n", 0, 0, 0, 0, 0);
                PrintGBKStr("累 计 积 分: 4000\n", 0, 0, 0, 0, 0);
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
            final boolean bIsOpened = pos.GetIO().IsOpened();
            if (bPrintResult) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(mActivity.getApplicationContext(), "打印成功", Toast.LENGTH_SHORT).show();
                        mActivity.btnPrint.setEnabled(bIsOpened);
                    }

                });
            } else {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(mActivity.getApplicationContext(), "打印失败", Toast.LENGTH_SHORT).show();
                        mActivity.btnPrint.setEnabled(bIsOpened);
                    }

                });
            }

        }

    }

    public static class TaskClose implements Runnable {
        USBPrinting usb = null;

        public TaskClose(USBPrinting usb) {
            this.usb = usb;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            usb.Close();
        }

    }

    @Override
    public void OnOpen() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                btnDisconnect.setEnabled(true);
                btnPrint.setEnabled(true);
                linearlayoutdevices.setEnabled(false);
                for (int i = 0; i < linearlayoutdevices.getChildCount(); ++i) {
                    Button btn = (Button) linearlayoutdevices.getChildAt(i);
                    btn.setEnabled(false);
                }
                Toast.makeText(mActivity, "Connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnOpenFailed() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                btnDisconnect.setEnabled(false);
                btnPrint.setEnabled(false);
                linearlayoutdevices.setEnabled(true);
                for (int i = 0; i < linearlayoutdevices.getChildCount(); ++i) {
                    Button btn = (Button) linearlayoutdevices.getChildAt(i);
                    btn.setEnabled(true);
                }
                Toast.makeText(mActivity, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnClose() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnDisconnect.setEnabled(false);
                btnPrint.setEnabled(false);
                linearlayoutdevices.setEnabled(true);
                for (int i = 0; i < linearlayoutdevices.getChildCount(); ++i) {
                    Button btn = (Button) linearlayoutdevices.getChildAt(i);
                    btn.setEnabled(true);
                }
                probe(); // 如果因为打印机关机导致Close。那么这里需要重新枚举一下。
            }
        });
    }

    @Override
    public void OnMessage(final String msg) {
        // TODO Auto-generated method stub

    }


    public static class TaskEnPrint implements Runnable {
        Pos pos = null;

        public TaskEnPrint(Pos pos) {
            this.pos = pos;
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

                PrintGBKStr("[Order No:0001]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
                PrintGBKStr("[PW:0001]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
                pos.POS_FeedLine();
                pos.POS_S_Align(0);//左对齐

                ////点餐内容
                PrintGBKStr(" [Order Time] 2019-07-18[Tue] 13:33:43\r\n", 0, 0, 0, 0, 0);//1倍大
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("Product","Count")+"\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("Americano","1")+"\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("Ice Americano","1")+"\n", 0, 0, 0, 0, 0);//
                pos.POS_S_Align(0);//左对齐
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Smart Green Cafe\n", 0, 0, 0, 0, 0x08);
                PrintGBKStr("282-34-00719 DongJae Lee Tel. 010-2505-8844\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Floor 1, No.396, Guryongdaljeon-ro,( Dungok-dong) Yuseong-gu, Daejeon)\n", 0, 0, 0, 0, 0);
                pos.POS_FeedLine();
                PrintGBKStr("[Date of receipt] 2019-07-16 13:33;32\n", 0, 0, 0, 0, 0);
                PrintGBKStr("[Number of receipt] 190716133321523-0000\n", 0, 0, 0, 0, 0);
                PrintGBKStr("[Sales Details] --------------------------------\n", 0, 0, 0, 0, 0);
                PrintGBKStr(PrintUtils.addSpc("Sales Details:","3,636")+"\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("Tax:","364")+"\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("Total:","4,000")+"\n", 0, 0, 0, 0, 0);//
                pos.POS_S_Align(0);//左对齐
                PrintGBKStr("[Payment Details] ------------------------------\n", 0, 0, 0, 0, 0);
                PrintGBKStr(PrintUtils.addSpc("Credit card:","4,000")+"\n", 0, 0, 0, 0, 0);//
                pos.POS_S_Align(0);//左对齐
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Card #: 6251-20**-****-****\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Card com.: BC card\n", 0, 0, 0, 0, 0);
                PrintGBKStr("instalments: 00\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Approval #: 33593898\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Transaction Time: 2019-07-16 13:33:32\n", 0, 0, 0, 0, 0);
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Koisk ID: 7142962002 – Kiosk01]\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Membership #: 010-2505-8844\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Point        : 4000\n", 0, 0, 0, 0, 0);
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
            final boolean bIsOpened = pos.GetIO().IsOpened();
            if (bPrintResult) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(mActivity.getApplicationContext(), "打印成功", Toast.LENGTH_SHORT).show();
                        mActivity.btnPrint.setEnabled(bIsOpened);
                    }

                });
            } else {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(mActivity.getApplicationContext(), "打印失败", Toast.LENGTH_SHORT).show();
                        mActivity.btnPrint.setEnabled(bIsOpened);
                    }

                });
            }
        }
    }

    public static class TaskKoPrint implements Runnable {
        Pos pos = null;

        public TaskKoPrint(Pos pos) {
            this.pos = pos;
        }

        public int PrintKSCStr(String StrUtf8, int nOrgx, int nWidthTimes, int nHeightTimes, int nFontType, int nFontStyle) {
            //原字符串函数里有乱码，仅用来设置格式
            byte[] korea = new byte[]{(byte)0x12,(byte)0x90,(byte)0x01};
            pos.GetIO().Write(korea,0,3);
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

        private void changeKro(){
            byte[] korea = new byte[]{12,90,01};
            pos.GetIO().Write(korea,0,3);
        }

        public  static String addSpc(String text,String money){
            int test = 0;
            int len = 576 - sort(text) - sort(money);
            int spcLen = len/12;
            StringBuffer sb = new StringBuffer();
            sb.append(text);
            for(int i =0;i<spcLen;i++){
                sb.append(" ");
                test = test+ 1;
            }
            sb.append(money);
//            int spcLen = 24 - text.length() - money.length();
//            StringBuffer sb = new StringBuffer();
//            sb.append(text);
//            for(int i =0;i<spcLen;i++){
//                sb.append("  ");
//            }
//            sb.append(money);
            Logger.d("=============test:"+test);
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
                        || s.equals("7") || s.equals("8") || s.equals("9") || s.equals("0")){
                    len  = len + 12;
                }else{
                    len  = len + 24;
                }

            }
            return len;
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

                PrintKSCStr("[주문번호:0001]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
                PrintKSCStr("[비밀번호:0001]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
                pos.POS_FeedLine();
                pos.POS_S_Align(0);//左对齐

                ////点餐内容
                PrintKSCStr(" [주문일시] 2019-07-18[화] 13:33:43\r\n", 0, 0, 0, 0, 0);//1倍大
                pos.POS_S_TextOut("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);//
                PrintKSCStr(PrintUtils.addSpc("상품명","수량")+"\n", 0, 0, 0, 0, 0);//
                PrintKSCStr(PrintUtils.addSpc("아메리카노","1")+"\n", 0, 0, 0, 0, 0);//
                PrintKSCStr(PrintUtils.addSpc("아이스 아메리카노","1")+"\n", 0, 0, 0, 0, 0);//
                PrintKSCStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintKSCStr("스마트그린카페\n", 0, 0, 0, 0, 0x08);
                PrintKSCStr("282-34-00719 이동재 Tel. 010-2505-8844\n", 0, 0, 0, 0, 0);
                PrintKSCStr("대전광역시 유성구 구룡달전로 396, 1층(둔곡동)\n", 0, 0, 0, 0, 0);
                pos.POS_FeedLine();
                PrintKSCStr("[영수증 발행일] 2019-07-16 13:33;32\n", 0, 0, 0, 0, 0);
                PrintKSCStr("[영수증 번호] 190716133321523-0000\n", 0, 0, 0, 0, 0);
                PrintKSCStr("[판매내역] --------------------------------\n", 0, 0, 0, 0, 0);
                PrintKSCStr(PrintUtils.addSpc("판 매 내 역:","3,636")+"\n", 0, 0, 0, 0, 0);//
                PrintKSCStr(PrintUtils.addSpc("부  가   세:","364")+"\n", 0, 0, 0, 0, 0);//
                PrintKSCStr(PrintUtils.addSpc("합       계:","4,000")+"\n", 0, 0, 0, 0, 0);//
                PrintKSCStr("[결제내역] ------------------------------\n", 0, 0, 0, 0, 0);
                PrintKSCStr(PrintUtils.addSpc("신 용 카 드:","4,000")+"\n", 0, 0, 0, 0, 0);//
                PrintKSCStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintKSCStr("카 드 번 호: 6251-20**-****-****\n", 0, 0, 0, 0, 0);
                PrintKSCStr("매 입 사 명: BC 카드사\n", 0, 0, 0, 0, 0);
                PrintKSCStr("할 부 개 월: 00\n", 0, 0, 0, 0, 0);
                PrintKSCStr("승 인 번 호: 33593898\n", 0, 0, 0, 0, 0);
                PrintKSCStr("거 래 일 시: 2019-07-16 13:33:32\n", 0, 0, 0, 0, 0);
                PrintKSCStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintKSCStr("주문기기 ID: 7142962002 – Kiosk01]\n", 0, 0, 0, 0, 0);
                PrintKSCStr("회 원 번 호: 010-2505-8844\n", 0, 0, 0, 0, 0);
                PrintKSCStr("적립 포인트: 4000\n", 0, 0, 0, 0, 0);
                pos.POS_FeedLine();
                pos.POS_S_Align(1);//居中
                pos.POS_S_TextOut("*****감사합니다*****\n", 0, 0, 0, 0, 0);
                //	pos.POS_S_SetQRcode("https://appsto.re/cn/2KF_bb.i", 8, 0, 3);
                pos.POS_FeedLine();
                pos.POS_FeedLine();
                pos.POS_FeedLine();
                pos.POS_CutPaper();
                bPrintResult = pos.POS_QueryStatus(status, 2000, 2);
            }
            final boolean bIsOpened = pos.GetIO().IsOpened();
            if (bPrintResult) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(mActivity.getApplicationContext(), "打印成功", Toast.LENGTH_SHORT).show();
                        mActivity.btnPrint.setEnabled(bIsOpened);
                    }

                });
            } else {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(mActivity.getApplicationContext(), "打印失败", Toast.LENGTH_SHORT).show();
                        mActivity.btnPrint.setEnabled(bIsOpened);
                    }
                });
            }
        }
    }
    private int sort(String str){
        int len = 0;
        char charArray[] = str.toCharArray();//利用toCharArray方法转换
        for(int i=0;i<charArray.length;i++){
            if(String.valueOf(charArray[i]).equals(",")
                    || String.valueOf(charArray[i]).equals(" ")
                    || String.valueOf(charArray[i]).equals(":")){
                len  = len + 12;
            }else{
                len  = len + 24;
            }

        }
        return len;
    }

}