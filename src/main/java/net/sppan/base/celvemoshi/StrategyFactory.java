package net.sppan.base.celvemoshi;

import java.util.HashMap;
import java.util.Map;

public class StrategyFactory {
    private static StrategyFactory factory = new StrategyFactory();
    private StrategyFactory(){
    }//这里用了饿汉式（静态常量） +工厂模式 ，把数字作为key，value是策略的实现类
    private static Map strategyMap = new HashMap<>();
    static{//key是数字，value是策略实现类
        strategyMap.put(RechargeTypeEnum.E_BANK.value(), new EBankStrategy());
        strategyMap.put(RechargeTypeEnum.BUSI_ACCOUNTS.value(), new BusiAcctStrategy());
        strategyMap.put(RechargeTypeEnum.MOBILE.value(), new MobileStrategy());
    }
    public Strategy creator(Integer type){
        return (Strategy) strategyMap.get(type);
    }
    public static StrategyFactory getInstance(){
        return factory;
    }
}
