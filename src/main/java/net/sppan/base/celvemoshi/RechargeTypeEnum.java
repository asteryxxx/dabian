package net.sppan.base.celvemoshi;

public enum RechargeTypeEnum {
    E_BANK(1, "网银"),
    BUSI_ACCOUNTS(2, "商户账号"),
    MOBILE(3,"手机卡充值"),
    CARD_RECHARGE(4,"充值卡");
    private int value;
    private String description;
    private RechargeTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }
    public int value() {
        return value;
    }
    public String description() {
        return description;
    }

    public static RechargeTypeEnum valueOf(int value) {
        for(RechargeTypeEnum type : RechargeTypeEnum.values()) {
            //传value的key,遍历枚举的所有value,得到大写字母整个枚举对象
            if(type.value() == value) {
                return type;//然后你可以输出type.description
            }
        }
        return null;
    }
    public static void main(String[] args){
        RechargeTypeEnum aa = valueOf(4);
        System.out.println(aa.description);
    }
}
