package net.sppan.base.celvemoshi;

import java.util.ArrayList;
import java.util.List;

public class MainHandler {
    public static void main(String[] args){
         List<Handler> handlerList=new ArrayList<>();
        Handler one = new OneHandlerImpl();
        Handler two = new TwoHandlerImpl();
         handlerList.add(one);
        handlerList.add(two);
        for(Handler handler:handlerList){
            if("two".equals(handler.getType())){
                handler.execute();
            }
        }
    }
}
