package com.mj.cafe.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Pattern;

public class EtInputFilters implements InputFilter {

    /**
     * 限制输入的最大值
     */
    public static final int TYPE_MAXNUMBER = 1;

    /**
     * 限制输入最大长度
     */
    public static final int TYPE_MAXLENGTH = 2;

    /**
     * 限制输入小数位数
     */
    public static final int TYPE_DECIMAL = 3;

    /**
     * 限制输入最小整数
     */
    public static final int TYPE_MINNUMBER = 4;

    /**
     * 限制输入手机号
     */
    public static final int TYPE_PHONENUMBER = 5;

    private Pattern mPattern;
    private double mMaxNum; //最大数值
    private double mMin;
    private int mMaxLength; //最大长度

    private int mType = 0;

    public EtInputFilters(int type) {
        this.mType = type;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        switch (mType) {
            case TYPE_MAXNUMBER:
                return filterMaxNum(source, start, end, dest, dstart, dend);
            case TYPE_MAXLENGTH:
                return filterMaxLength(source, start, end, dest, dstart, dend);
            case TYPE_DECIMAL:
                return filterDecimal(source, dest, dstart, dend);
            case TYPE_MINNUMBER:
                return filterMinnum(source, dest, dstart);
            case TYPE_PHONENUMBER:
                return filterPhoneNum(source, dest, dstart);
        }
        return source;
    }

    /**
     * 最大值的限制
     * @param min 允许的最小值
     * @param maxNum 允许的最大值
     * @param numOfDecimals 允许的小数位
     */
    public EtInputFilters setMaxNum(int min, double maxNum, int numOfDecimals) {
        this.mMaxNum = maxNum;
        this.mMin = min;
        this.mPattern = Pattern.compile("^" + (min < 0 ? "-?" : "")
                + "[0-9]*\\.?[0-9]" + (numOfDecimals > 0 ? ("{0," + numOfDecimals + "}$") : "*"));
        return this;
    }

    /**
     * 过滤最大值
     */
    private CharSequence filterMaxNum(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (source.equals(".")) {
            if (dstart == 0 || !(dest.charAt(dstart - 1) >= '0' && dest.charAt(dstart - 1) <= '9') || dest.charAt(0) == '0') {
                return "";
            }
        }
        if (source.equals("0") && (dest.toString()).contains(".") && dstart == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(dest);
        builder.delete(dstart, dend);
        builder.insert(dstart, source);
        if (!mPattern.matcher(builder.toString()).matches()) {
            return "";
        }

        if (!TextUtils.isEmpty(builder)) {
            double num = Double.parseDouble(builder.toString());
            if (num > mMaxNum) {
                return "";
            }
            if (num <mMin ) {
                return "";
            }
        }
        return source;
    }


    /**
     * 设置最大长度
     * @param maxLength 最大长度
     */
    public EtInputFilters setMaxNum(int maxLength) {
        this.mMaxLength = maxLength;
        return this;
    }

    /**
     * 过滤最大长度
     */
    private CharSequence filterMaxLength(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int keep = mMaxLength - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep);
        }
    }


    /**
     * 设置可输入小数位数
     * @param decimal 允许的小数位
     */
    public EtInputFilters setDecimal(int decimal) {
        this.mPattern = Pattern.compile("^[0-9]*\\.?[0-9]"
                + (decimal > 0 ? ("{0," + decimal + "}$") : "*"));
        return this;
    }

    /**
     * 过滤小数
     */
    private CharSequence filterDecimal(CharSequence source, Spanned dest, int dstart, int dend) {
        if (source.equals(".")) {
            if (dstart == 0 || !(dest.charAt(dstart - 1) >= '0' && dest.charAt(dstart - 1) <= '9') || dest.charAt(0) == '0') {
                return "";
            }
        }
        if (source.equals("0") && (dest.toString()).contains(".") && dstart == 0) { //防止在369.369的最前面输入0变成0369.369这种不合法的形式
            return "";
        }
        StringBuilder builder = new StringBuilder(dest);
        builder.delete(dstart, dend);
        builder.insert(dstart, source);
        if (!mPattern.matcher(builder.toString()).matches()) {
            return "";
        }

        return source;
    }

    /**
     * 设置只能输入整数，限制最小整数
     * @param minnum 最小整数
     */
    public EtInputFilters setMinnumber(int minnum) {
        this.mPattern = Pattern.compile("^" + (minnum < 0 ? "-?" : "") + "[0-9]*$");
        return this;
    }

    /**
     * 过滤整数
     */
    private CharSequence filterMinnum(CharSequence source, Spanned dest, int dstart) {
        StringBuilder builder = new StringBuilder(dest);
        builder.insert(dstart, source);
        if (!mPattern.matcher(builder.toString()).matches()) {
            return "";
        }
        return source;
    }

    /**
     * 设置只能输入手机号
     * @return
     */
    public EtInputFilters setPhone() {
        this.mPattern = Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(1[57]))\\d{8}$");
        return this;
    }

    /**
     * 过滤手机号
     */
    private CharSequence filterPhoneNum(CharSequence source, Spanned dest, int dstart) {
        StringBuilder builder = new StringBuilder(dest);
        builder.insert(dstart, source);
        int length = builder.length();
        if (length == 1) {
            if (builder.charAt(0) == '1') {
                return source;
            } else {
                return "";
            }
        }

        if (length > 0 && length <= 11) {
            if (mPattern.matcher(builder.toString()).matches()) {
                return source;
            } else {
                return "";
            }
        }
        return "";
    }

}
