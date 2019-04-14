package net.sppan.base.bingfa;

import java.util.concurrent.*;

public class LinkedBlockingQueueTest1 {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueueTest1 test1=new LinkedBlockingQueueTest1();
        ExecutorService service = Executors.newCachedThreadPool();
        Basket bast = new Basket();
        Producer p1=new Producer("生产者01",bast);
        Producer p2=new Producer("生产者02",bast);
        Consumer p3=new Consumer("消费者01",bast);
        service.submit(p1);        service.submit(p2);
        service.submit(p3);
        Thread.sleep(5000);
        service.shutdown();
    }
}
class Basket{
    //篮子，能容纳3个苹果
    BlockingQueue<String> basket=new LinkedBlockingQueue<>();
    //产生苹果，放进篮子
    public void produce() throws InterruptedException {
        basket.put("an apple");
        //放进去一个苹果，如果满了，等有位置
    }
    //消费苹果，从篮子取走
    public String consume() throws InterruptedException {
        //take方法取出一个苹果，若basket为空，等到有苹果为止（获取并移出头部
        return basket.take();
    }
}
//生成苹果的
class Producer implements Runnable{
    String instance;
    Basket basket;

    public Producer(String instance,Basket basket){
        this.instance=instance;
        this.basket=basket;
    }
    public void run() {
        try {
            while (true){
                System.out.println(instance+"生成苹果");
                basket.produce();
                //休眠300ms
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            System.out.println("Producer  Interrupted");
        }
    }
}
class Consumer  implements Runnable{
    String instance;
    Basket basket;

    public Consumer (String instance,Basket basket){
        this.instance=instance;
        this.basket=basket;
    }
    public void run() {
        try {
            while (true){
                System.out.println(instance+"消费苹果"+basket.consume());
                //休眠300ms
                Thread.sleep(150);
            }
        } catch (InterruptedException e) {
            System.out.println("Consumer  Interrupted");
        }
    }
}