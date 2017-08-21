package org.myan.threads;

/**
 * Created by myan on 2017/8/21.
 * Intellij IDEA
 */
public class Test {
    public static void main(String[] args) {
        SimpleThreadPool pool = SimpleThreadPool.getInstance();
        pool.execute(new Task());
        pool.execute(new Task());
        pool.execute(new Task());
    }

    static class Task implements Runnable{
        private static volatile int id = 1;

        @Override
        public void run() {
            System.out.println(String.format("Thread %d started.", this.id++ ));
        }
    }
}
