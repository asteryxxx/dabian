package net.sppan.base.pachong;

import net.sppan.base.utils.SimpleDateutils;

import java.text.ParseException;

public class testSimpleDate {
    public static class Testsim extends   Thread {
        public void run() {
            while(true){
                try {
                    this.join(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println(this.getName()+":"+ SimpleDateutils.parse("2019-04-11 06:03:30"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    public static void main(String[] args){
        for(int i = 0; i < 3; i++){
            new Testsim().start();
        }
    }
}
