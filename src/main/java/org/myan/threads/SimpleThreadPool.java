package org.myan.threads;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by myan on 2017/8/21.
 * Intellij IDEA
 */
public final class SimpleThreadPool {
    private static int worker_size = 5;
    private final List<Runnable> taskQueue = Collections.synchronizedList(new LinkedList<>());
    private static SimpleThreadPool instance;
    private ThreadWorker[] workers;

    private SimpleThreadPool() {
        this(worker_size);
    }

    private SimpleThreadPool(int workerSize) {
        SimpleThreadPool.worker_size = workerSize;
        workers = new ThreadWorker[workerSize];
        for (int i = 0; i < workerSize; i++) {
            workers[i] = new ThreadWorker();
            workers[i].start();
        }
    }

    public static SimpleThreadPool getInstance() {
        return getInstance(SimpleThreadPool.worker_size);
    }

    public static SimpleThreadPool getInstance(int workerSize) {
        if (workerSize <= 0)
            workerSize = worker_size;
        if (instance == null)
            instance = new SimpleThreadPool(workerSize);
        return instance;
    }

    public void execute(Runnable task) {
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notify();
        }
    }

    public void batchExecute(Runnable[] tasks) {
        synchronized (taskQueue) {
            Collections.addAll(taskQueue, tasks);
            taskQueue.notifyAll();
        }
    }

    public synchronized void destroy() {
        while (!taskQueue.isEmpty()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < workers.length; i++) {
            workers[i].stopWorking();
            workers[i] = null;
        }
        instance = null;
        taskQueue.clear();
    }

    private class ThreadWorker extends Thread {
        private boolean isRunning;

        ThreadWorker() {
            this.isRunning = true;
        }

        @Override
        public void run() {
            Runnable r = null;
            while (isRunning) {
                synchronized (taskQueue) {
                    while (taskQueue.isEmpty()) {
                        try {
                            taskQueue.wait(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //now we should get the task and run it.
                    if (!taskQueue.isEmpty())
                        r = taskQueue.remove(0);
                }
                if (r != null) {
                    r.run();
                }
                r = null;
            }
        }

        void stopWorking() {
            this.isRunning = false;
        }
    }
}
