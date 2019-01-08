package com.xtshop.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author river
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface Cart {
        /**
         * 购物车被选中状态
         */
        int CHECKED = 1;
        /**
         * 没有被选中状态
         */
        int UN_CHECKED = 2;

        String LIMIT_NUM_FAFL = "LIMIT_NUM_FAFL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface Role {
        /**
         * 普通用户
         */
        int ROLE_CUSTOMER = 0;
        /**
         * 管理员
         */
        int ROLE_ADMIN = 1;
    }

    /**
     * 商品是否是在售状态
     */
    public enum ProductStatusEnum {
        ON_SALE(1, "在售");
        private String value;
        private int code;

        /**
         * 构造器
         */
        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum OrderStatusEnum {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已支付"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单已完成"),
        ORDER_CLOSE(60, "订单关闭");

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.code == code) {
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举!");
        }
    }

    /**
     * 支付宝支付的返回参数
     */
    public interface AlipayCallback {
        String TARDE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TARDE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    /**
     * 支付方式_支付宝
     */
    public enum PayPlatformEnum {
        ALIPAY(1, "支付宝");

        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 支付方式
     */
    public enum PaymentTypeEnum {
        ONLINE_PAY(1, "在线支付");

        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static PaymentTypeEnum codeOf(int code) {
            for (PaymentTypeEnum paymentTypeEnum : values()) {
                if (paymentTypeEnum.code == code) {
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举!");
        }
    }

}
