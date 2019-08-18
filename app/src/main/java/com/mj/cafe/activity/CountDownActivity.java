package com.mj.cafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.USBPrinting;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.BizcContant;
import com.mj.cafe.MyApp;
import com.mj.cafe.R;
import com.mj.cafe.bean.FinishActivityEvent;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.bean.OrderBean;
import com.mj.cafe.bean.OrderStateEntity;
import com.mj.cafe.bean.PayTypeBean;
import com.mj.cafe.bean.PrintEntity;
import com.mj.cafe.bean.PrintGoodsEntity;
import com.mj.cafe.retorfit.HttpMjResult;
import com.mj.cafe.retorfit.RetrofitSerciveFactory;
import com.mj.cafe.retorfit.rxjava.BaseSubscriber;
import com.mj.cafe.retorfit.rxjava.HttpMjEntityFun;
import com.mj.cafe.retorfit.rxjava.RxUtil;
import com.mj.cafe.utils.ActivityUtil;
import com.mj.cafe.utils.PrintUtils;
import com.mj.cafe.utils.SharedPreferencesUtil;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import rx.Subscription;

import static com.mj.cafe.bean.LangTypeBean.CN;
import static com.mj.cafe.bean.LangTypeBean.EN;
import static com.mj.cafe.bean.LangTypeBean.KO;

public class CountDownActivity extends BaseActivity {
    static CountDownActivity mActivity;
    @BindView(R.id.CountdownView)
    CountdownView mCountdownView;
    @BindView(R.id.TvTips)
    TextView TvTips;
    @BindView(R.id.IvZhongWen)
    ImageView IvZhongWen;
    @BindView(R.id.IvHanYu)
    ImageView IvHanYu;
    @BindView(R.id.IvYingYu)
    ImageView IvYingYu;
    private PayTypeBean mPayType;
    private OrderBean mOrderBean;
    private PrintEntity mPrintEntity;
    private boolean isSuccess;
    Subscription disposable;
    private int httpNum = 0;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            httpNum++;
            httpGetOrderStatus();
            return true;
        }
    });
    @OnClick({R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.IvZhongWen:
                postLangLiveData(new LangTypeBean(CN));
                break;
            case R.id.IvHanYu:
                postLangLiveData(new LangTypeBean(KO));
                break;
            case R.id.IvYingYu:
                postLangLiveData(new LangTypeBean(EN));
                break;

        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down_check);
        ButterKnife.bind(this);
        mPayType = getIntent().getParcelableExtra("type");
        mOrderBean = getIntent().getParcelableExtra("order");
        if (mOrderBean == null) {
//            showToastMsg("CountDownActivity +mOrderBean为空");
            return;
        }
        mCountdownView.start(15 * 1000);
        mCountdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                //？？？刷新接口？
                if (!isSuccess) {
                    RxBus.get().post(new FinishActivityEvent());
                    //测试打印小票
//                    showToastMsg("倒计时完");
                    if (disposable != null) {
                        disposable.unsubscribe();
                    }
                    handler.removeMessages(0);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("order", mOrderBean);
                    bundle.putParcelable("type", mPayType);
//                    showToastMsg(mOrderBean.getPayMoney());
//                    showToastMsg(mOrderBean.getTaxMoney());
                    ActivityUtil.next(CountDownActivity.this, PayFailedAcitivty.class, bundle, false);
                }
            }
        });
        setLangView((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE, new LangTypeBean(LangTypeBean.DEFAULT)));
        httpGetOrderStatus();
//        httpGetPrintData();
    }


    public static class TaskPrint implements Runnable {
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
            changeChinese();
            if (pos.POS_QueryStatus(status, 2000, 2)) {
                pos.POS_FeedLine();
                pos.POS_S_Align(1);//居中对齐
                PrintGBKStr("[点单号: " + printEntity.getMeal_code() + "]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
                PrintGBKStr("[点餐密码: " + printEntity.getMeal_pwd() + "]\r\n", 0, 1, 1, 0, 0);//2倍大，有下划线
                pos.POS_FeedLine();
                pos.POS_S_Align(0);//左对齐
                ////点餐内容
                PrintGBKStr(" [点单时间] " + printEntity.getCreate_time() + "\r\n", 0, 0, 0, 0, 0);//1倍大
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);//
                PrintGBKStr(PrintUtils.addSpc("商品名", "数量") + "\n", 0, 0, 0, 0, 0);//
                printGoods();
                pos.POS_S_Align(0);//左对齐
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintGBKStr("Smart Green Cafe\n", 0, 0, 0, 0, 0x08);
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
                PrintGBKStr(PrintUtils.addSpc("信 用 卡 片:", "4,000") + "\n", 0, 0, 0, 0, 0);//
                pos.POS_S_Align(0);//左对齐
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintGBKStr("卡 片 号 码: 6251-20**-****-****\n", 0, 0, 0, 0, 0);
                PrintGBKStr("卡 片 公 司: BC发卡公司\n", 0, 0, 0, 0, 0);
                PrintGBKStr("分 期 月 数: 00\n", 0, 0, 0, 0, 0);
                PrintGBKStr("批 准 号 码: 33593898\n", 0, 0, 0, 0, 0);
                PrintGBKStr("交 易 时 间: 2019-07-16 13:33:32\n", 0, 0, 0, 0, 0);
                PrintGBKStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintGBKStr("点 餐 机 ID:" + printEntity.getDevice_no() + "]\n", 0, 0, 0, 0, 0);
                PrintGBKStr("会 员 卡 号:" + printEntity.getVip_card() + "]\n", 0, 0, 0, 0, 0);
                PrintGBKStr("累 计 积 分:" + printEntity.getIntegral() + "]\n", 0, 0, 0, 0, 0);
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
                    }

                });
            } else {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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

    public static class TaskEnPrint implements Runnable {
        Pos pos = null;
        PrintEntity printEntity = null;

        public TaskEnPrint(Pos pos, PrintEntity printEntity) {
            this.pos = pos;
            this.printEntity = printEntity;
        }

        public void printGoods() {
            if (printEntity.getGoodsList() != null && printEntity.getGoodsList().size() > 0) {
                for (PrintGoodsEntity entity : printEntity.getGoodsList()) {
                    PrintGBKStr(PrintUtils.addSpc(entity.getName(), entity.getNum() + "") + "\n", 0, 0, 0, 0, 0);//
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
            final boolean bIsOpened = pos.GetIO().IsOpened();
//            if (bPrintResult) {
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                    }
//
//                });
//            } else {
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                    }
//
//                });
//            }
        }
    }

    public static class TaskKoPrint implements Runnable {
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
                pos.POS_S_TextOut("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);//
                PrintKSCStr(PrintUtils.addSpc("상품명", "수량") + "\n", 0, 0, 0, 0, 0);//
                printGoods();
                PrintKSCStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintKSCStr("스마트그린카페\n", 0, 0, 0, 0, 0x08);
                PrintKSCStr(printEntity.getLicense_number() + printEntity.getContact() + "Tel." + printEntity.getTel() + "\n", 0, 0, 0, 0, 0);
                PrintKSCStr(printEntity.getAddress() + "\n", 0, 0, 0, 0, 0);
                pos.POS_FeedLine();
                PrintKSCStr("[영수증 발행일]" + printEntity.getInvoice_date() + "\n", 0, 0, 0, 0, 0);
                PrintKSCStr("[영수증 번호] " + printEntity.getInvoice_code() + "\n", 0, 0, 0, 0, 0);
                PrintKSCStr("[판매내역] --------------------------------\n", 0, 0, 0, 0, 0);
                PrintKSCStr(PrintUtils.addSpc("판 매 내 역:", printEntity.getTotal_money()) + "\n", 0, 0, 0, 0, 0);//0);//
                PrintKSCStr(PrintUtils.addSpc("부  가   세:", printEntity.getTax_money()) + "\n", 0, 0, 0, 0, 0);//0);//
                PrintKSCStr(PrintUtils.addSpc("합       계:", printEntity.getPay_money()) + "\n", 0, 0, 0, 0, 0);//
                PrintKSCStr("[결제내역] ------------------------------\n", 0, 0, 0, 0, 0);
                PrintKSCStr(PrintUtils.addSpc("신 용 카 드:", "4,000") + "\n", 0, 0, 0, 0, 0);//
                PrintKSCStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintKSCStr("카 드 번 호: 6251-20**-****-****\n", 0, 0, 0, 0, 0);
                PrintKSCStr("매 입 사 명: BC 카드사\n", 0, 0, 0, 0, 0);
                PrintKSCStr("할 부 개 월: 00\n", 0, 0, 0, 0, 0);
                PrintKSCStr("승 인 번 호: 33593898\n", 0, 0, 0, 0, 0);
                PrintKSCStr("거 래 일 시: 2019-07-16 13:33:32\n", 0, 0, 0, 0, 0);
                PrintKSCStr("-----------------------------------------------\r\n", 0, 0, 0, 0, 0);
                PrintKSCStr("주문기기 ID:" + printEntity.getDevice_no() + "]\n", 0, 0, 0, 0, 0);
                PrintKSCStr("회 원 번 호:" + printEntity.getVip_card() + "]\n", 0, 0, 0, 0, 0);
                PrintKSCStr("적립 포인트:" + printEntity.getIntegral() + "]\n", 0, 0, 0, 0, 0);
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
//            if (bPrintResult) {
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        Toast.makeText(mActivity.getApplicationContext(), "打印成功", Toast.LENGTH_SHORT).show();
//                    }
//
//                });
//            } else {
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        Toast.makeText(mActivity.getApplicationContext(), "打印失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
        }
    }


    //http - 小票打印数据
    private void httpGetPrintData() {
//        showToastMsg("获取——小票打印数据");
        RetrofitSerciveFactory.provideComService().getPrintData(mOrderBean.getOrderCode(), "")
                .compose(RxUtil.<HttpMjResult<PrintEntity>>applySchedulersForRetrofit())
                .map(new HttpMjEntityFun<PrintEntity>())
                .subscribe(new BaseSubscriber<PrintEntity>(this) {
                    @Override
                    public void onNext(PrintEntity entity) {
                        super.onNext(entity);
//                        showToastMsg("获取——成功");
                        mPrintEntity = entity;
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMsg("获取——失败");
                        super.onError(e);
                    }
                });
    }

    //http - 查询订单状态
    private void httpGetOrderStatus() {
        disposable = RetrofitSerciveFactory.provideComService().getOrderStatus(mOrderBean.getOrderCode())
                .compose(RxUtil.<HttpMjResult<OrderStateEntity>>applySchedulersForRetrofit())
                .map(new HttpMjEntityFun<OrderStateEntity>())
                .subscribe(new BaseSubscriber<OrderStateEntity>(this) {
                    @Override
                    public void onNext(OrderStateEntity entity) {
                        Bundle bundle = new Bundle();
                        if (entity != null && entity.getState() != null) {
                            if (entity.getState() == 1) {
                                //支付成功
//                                showToastMsg("支付成功");
                                handler.sendEmptyMessageDelayed(0, 2000);
                            } else if (entity.getState() == 5) {
                                //下单失败(不可制作)
//                                showToastMsg("下单失败");
                                RxBus.get().post(new FinishActivityEvent());
                                disposable.unsubscribe();
                                handler.removeMessages(0);
                                bundle.putParcelable("type", mPayType);
                                bundle.putParcelable("order", mOrderBean);
                                ActivityUtil.next(CountDownActivity.this, PayFailedAcitivty.class, bundle, false);
                            } else if (entity.getState() == 2) {
//                                showToastMsg("下单成功");
                                //下单成功
                                isSuccess = true;
                                //打印小票
//                                printTiket((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE, new LangTypeBean(LangTypeBean.DEFAULT)),mPrintEntity);
                                bundle.putString("meal_code", entity.getMeal_code());
                                bundle.putString("order_code", mOrderBean.getOrderCode());
                                disposable.unsubscribe();
                                handler.removeMessages(0);
                                RxBus.get().post(new FinishActivityEvent());
                                ActivityUtil.next(CountDownActivity.this, PaySuccessAcitivty.class, bundle, true);
                            }
                        }
                    }

                    @Override
                    public void onStart() {
//                        super.onStart();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.unsubscribe();
        }
        mCountdownView.stop();
    }

    @Override
    public void setLangView(LangTypeBean langTypeBean) {
        switch (langTypeBean.getType()) {
            case CN:
                TvTips.setText(getString(R.string.cn_Please_wait_a_minute_for_data_request));
                break;
            case EN:
                TvTips.setText(getString(R.string.en_Please_wait_a_minute_for_data_request));
                break;
            case KO:
                TvTips.setText(getString(R.string.ko_Please_wait_a_minute_for_data_request));
                break;
        }
    }

    //打印小票
    private void printTiket(LangTypeBean langTypeBean, PrintEntity printEntity) {
        if (printEntity == null) {
            showToastMsg("error_printEntity_null");
            return;
        }
        switch (langTypeBean.getType()) {
            case CN:
                new Thread(new TaskPrint(MyApp.getInstance().getPos(), printEntity)).start();
                break;
            case EN:
                new Thread(new TaskEnPrint(MyApp.getInstance().getPos(), printEntity)).start();
                break;
            case KO:
                new Thread(new TaskKoPrint(MyApp.getInstance().getPos(), printEntity)).start();
                break;
        }
    }

    @Override
    protected boolean rxBusRegist() {
        return true;
    }

    @Subscribe
    public void onFinishEvent(FinishActivityEvent event) {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null) {
            disposable.unsubscribe();
        }
    }
}
