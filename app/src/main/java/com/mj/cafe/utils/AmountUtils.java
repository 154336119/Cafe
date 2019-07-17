package com.mj.cafe.utils;

import java.math.BigDecimal;

public class AmountUtils {
    /**
     * 金额为分的格式
     */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";



    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     */
    public static BigDecimal toBigyuan(String amount) {
        return BigDecimal.valueOf(Double.valueOf(amount));
    }


    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static BigDecimal fen2Yuan(String amount) {
        if (!amount.matches(CURRENCY_FEN_REGEX)) {
            throw new RuntimeException("金额格式错误|"+amount);
        }
        return BigDecimal.valueOf(Double.valueOf(amount)).divide(new BigDecimal(100));
    }

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static BigDecimal fen2Yuan(int amount) {
        if (!String.valueOf(amount).matches(CURRENCY_FEN_REGEX)) {
            throw new RuntimeException("金额格式错误|"+amount);
        }
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100));
    }

    public static BigDecimal fen2Yuan(Long amount) {
        if (!String.valueOf(amount).matches(CURRENCY_FEN_REGEX)) {
            throw new RuntimeException("金额格式错误|" + amount);
        }
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).setScale(2);
    }
}
