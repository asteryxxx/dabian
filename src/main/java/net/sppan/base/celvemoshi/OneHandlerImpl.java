package net.sppan.base.celvemoshi;

public class OneHandlerImpl implements Handler {
    public String getType() {
        return "one";
    }

    @Override
    public void execute() {
        System.out.println("shuchu -- one");
    }
}
class TwoHandlerImpl implements Handler {
    public String getType() {
        return "two";
    }

    @Override
    public void execute() {
        System.out.println("shuchu -- two");
    }
}