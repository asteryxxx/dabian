package net.sppan.base.celvemoshi;

public interface Strategy {//策略接口
    public Double calRecharge(Double charge ,RechargeTypeEnum type );
}
class EBankStrategy implements Strategy{
    public Double calRecharge(Double charge, RechargeTypeEnum type) {
        return charge*0.85;
    }
}//策略实现类
class BusiAcctStrategy implements Strategy{
    public Double calRecharge(Double charge, RechargeTypeEnum type) {
        // TODO Auto-generated method stub
        return charge*0.90;
    }
}
 class MobileStrategy implements Strategy {
    @Override
    public Double calRecharge(Double charge, RechargeTypeEnum type) {
        return charge+charge*0.01;
    }
}
