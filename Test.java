Semaphore

import java.time.LocalTime;
import java.util.concurrent.Semaphore;
public class Main{
    public static void main(String[] args) {
        Semaphore semaphore=new Semaphore(2);
        new Thread(new MyThread(semaphore,"张三")).start();
        new Thread(new MyThread(semaphore,"李四")).start();
        new Thread(new MyThread(semaphore,"王五")).start();
        new Thread(new MyThread(semaphore,"黄流")).start();
    }
}

class MyThread implements Runnable{
    Semaphore semaphore;
    String name;
    LocalTime timetime;
    public MyThread(Semaphore semaphore,String name){
        this.semaphore=semaphore;
        this.name=name;
    }
    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println(name+"正在洗手"+LocalTime.now());
            Thread.sleep(1000);
            System.out.println(name+"洗手完毕"+LocalTime.now());
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


CountDownLatch
public class Main{
    public static void main(String[] args) {
        CountDownLatch countDownLatch=new CountDownLatch(3);
        new Thread(new MyThread(countDownLatch,"张三")).start();
        new Thread(new MyThread(countDownLatch,"李四")).start();
        new Thread(new MyThread(countDownLatch,"王五")).start();

        new Thread(()->{
            System.out.println("正在等人就餐");
            try {
                countDownLatch.await();
                System.out.println("人来全了，开始上菜");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

class MyThread implements Runnable{
    CountDownLatch countDownLatch;
    String name;
    LocalTime timetime;
    public MyThread(CountDownLatch countDownLatch,String name){
        this.countDownLatch=countDownLatch;
        this.name=name;
    }
    @Override
    public void run() {
        try {
            System.out.println(name+"正在路上"+LocalTime.now());
            Thread.sleep(1000);
            System.out.println(name+"已经来了"+LocalTime.now());
            countDownLatch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



打印ABC

Lock+Condition
public class Main{
    private static volatile int flag;
    public static void main(String[] args) throws InterruptedException {

        Lock lock=new ReentrantLock();
        Condition conditionA=lock.newCondition();
        Condition conditionB=lock.newCondition();
        Condition conditionC=lock.newCondition();
        new Thread(()->{
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while(flag%3!=0){
                        conditionA.await();
                    }
                    flag++;
                    System.out.println("A" + i);
                    conditionB.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }).start();


        new Thread(()->{
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while(flag%3!=1){
                        conditionB.await();
                    }
                    flag++;
                    System.out.println("B" + i);
                    conditionC.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }).start();


        new Thread(()->{
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while(flag%3!=2){
                        conditionC.await();
                    }
                    flag++;
                    System.out.println("C" + i);
                    conditionA.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }).start();
    }
}



Semaphore

public class Main{
    
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphoreA=new Semaphore(1);
        Semaphore semaphoreB=new Semaphore(0);
        Semaphore semaphoreC=new Semaphore(0);
        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    semaphoreA.acquire();
                    System.out.println("A"+i);
                    semaphoreB.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    semaphoreB.acquire();
                    System.out.println("B"+i);
                    semaphoreC.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    semaphoreC.acquire();
                    System.out.println("C"+i);
                    semaphoreA.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}



Lock

public class Main{
    private static volatile int flag;
    public static void main(String[] args) throws InterruptedException {
        Lock lock=new ReentrantLock();
        new Thread(()->{
            for(int i=0;i<10;){
                lock.lock();
                try {
                    if(flag%3==0){
                        System.out.println("A"+i);
                        flag++;
                        i++;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }).start();


        new Thread(()->{
            for(int i=0;i<10;){
                lock.lock();
                try {
                    if(flag%3==1){
                        System.out.println("B"+i);
                        flag++;
                        i++;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }).start();


        new Thread(()->{
            for(int i=0;i<10;){
                lock.lock();
                try {
                    if(flag%3==2){
                        System.out.println("C"+i);
                        flag++;
                        i++;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }).start();
    }
}






