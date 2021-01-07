package com.moralok.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 时间轮
 * @author moralok
 * @since 2021/1/7 1:50 下午
 */
@SuppressWarnings("unchecked")
public class RingBufferWheel {

    private Logger logger = LoggerFactory.getLogger(RingBufferWheel.class);

    /**
     * default ring buffer size
     */
    private static final int STATIC_RING_SIZE = 64;

    private Object[] ringBuffer;

    private int bufferSize;

    /**
     * business thread pool
     */
    private ExecutorService executorService;

    private AtomicInteger size = new AtomicInteger(0);

    /***
     * task stop sign
     */
    private volatile boolean stop = false;

    /**
     * task start sign
     */
    private volatile AtomicBoolean start = new AtomicBoolean(false);

    /**
     * total tick times
     */
    private AtomicInteger tick = new AtomicInteger();

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private AtomicInteger taskId = new AtomicInteger();
    private Map<Integer, Task> taskMap = new ConcurrentHashMap<>(16);

    /**
     * Create a new delay task ring buffer by default size
     *
     * @param executorService the business thread pool
     */
    public RingBufferWheel(ExecutorService executorService) {
        this.executorService = executorService;
        this.bufferSize = STATIC_RING_SIZE;
        this.ringBuffer = new Object[bufferSize];
    }


    /**
     * Create a new delay task ring buffer by custom buffer size
     *
     * @param executorService the business thread pool
     * @param bufferSize      custom buffer size
     */
    public RingBufferWheel(ExecutorService executorService, int bufferSize) {
        this(executorService);

        if (!powerOf2(bufferSize)) {
            throw new RuntimeException("bufferSize=[" + bufferSize + "] must be a power of 2");
        }
        this.bufferSize = bufferSize;
        this.ringBuffer = new Object[bufferSize];
    }

    /**
     * Add a task into the ring buffer(thread safe)
     *
     * @param task business task extends {@link Task}
     */
    public int addTask(Task task) {
        int key = task.getKey();
        int id;

        lock.lock();
        try {
            int index = mod(key, bufferSize);
            task.setIndex(index);
            Set<Task> tasks = get(index);

            int cycleNum = cycleNum(key, bufferSize);
            task.setCycleNum(cycleNum);
            if (tasks != null) {
                tasks.add(task);
            } else {
                Set<Task> sets = new HashSet<>();
                sets.add(task);
                put(key, sets);
            }
            id = taskId.incrementAndGet();
            task.setTaskId(id);
            taskMap.put(id, task);
            size.getAndIncrement();
        } finally {
            lock.unlock();
        }

        start();

        return id;
    }


    /**
     * Cancel task by taskId
     * @param id unique id through {@link #addTask(Task)}
     * @return
     */
    public boolean cancel(int id) {

        boolean flag = false;
        Set<Task> tempTask = new HashSet<>();

        lock.lock();
        try {
            Task task = taskMap.get(id);
            if (task == null) {
                return false;
            }

            Set<Task> tasks = get(task.getIndex());
            for (Task tk : tasks) {
                if (tk.getKey() == task.getKey() && tk.getCycleNum() == task.getCycleNum()) {
                    size.getAndDecrement();
                    flag = true;
                    taskMap.remove(id);
                } else {
                    tempTask.add(tk);
                }

            }
            //update origin data
            ringBuffer[task.getIndex()] = tempTask;
        } finally {
            lock.unlock();
        }

        return flag;
    }

    /**
     * Thread safe
     *
     * @return the size of ring buffer
     */
    public int taskSize() {
        return size.get();
    }

    /**
     * Same with method {@link #taskSize}
     * @return
     */
    public int taskMapSize(){
        return taskMap.size();
    }

    /**
     * Start background thread to consumer wheel timer, it will always run until you call method {@link #stop}
     */
    public void start() {
        if (!start.get()) {

            if (start.compareAndSet(start.get(), true)) {
                logger.info("Delay task is starting");
                ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
                singleThreadExecutor.execute(new TriggerJob());
                // Thread job = new Thread(new TriggerJob());
                // job.setName("consumer RingBuffer thread");
                // job.start();
                start.set(true);
            }

        }
    }

    /**
     * Stop consumer ring buffer thread
     *
     * @param force True will force close consumer thread and discard all pending tasks
     *              otherwise the consumer thread waits for all tasks to completes before closing.
     */
    public void stop(boolean force) {
        if (force) {
            logger.info("Delay task is forced stop");
            stop = true;
            executorService.shutdownNow();
        } else {
            logger.info("Delay task is stopping");
            if (taskSize() > 0) {
                lock.lock();
                try {
                    condition.await();
                    stop = true;
                } catch (InterruptedException e) {
                    logger.error("InterruptedException", e);
                } finally {
                    lock.unlock();
                }
            }
            executorService.shutdown();
        }


    }

    private Set<Task> get(int index) {
        return (Set<Task>) ringBuffer[index];
    }

    private void put(int key, Set<Task> tasks) {
        int index = mod(key, bufferSize);
        ringBuffer[index] = tasks;
    }

    /**
     * Remove and get task list.
     * @param key
     * @return task list
     */
    private Set<Task> remove(int key) {
        Set<Task> tempTask = new HashSet<>();
        Set<Task> result = new HashSet<>();

        Set<Task> tasks = (Set<Task>) ringBuffer[key];
        if (tasks == null) {
            return result;
        }

        for (Task task : tasks) {
            if (task.getCycleNum() == 0) {
                result.add(task);

                size2Notify();
            } else {
                // decrement 1 cycle number and update origin data
                task.setCycleNum(task.getCycleNum() - 1);
                tempTask.add(task);
            }
            // remove task, and free the memory.
            taskMap.remove(task.getTaskId());
        }

        //update origin data
        ringBuffer[key] = tempTask;

        return result;
    }

    private void size2Notify() {
        lock.lock();
        try {
            size.getAndDecrement();
            if (size.get() == 0) {
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    private boolean powerOf2(int target) {
        if (target < 0) {
            return false;
        }
        int value = target & (target - 1);
        if (value != 0) {
            return false;
        }

        return true;
    }

    private int mod(int target, int mod) {
        // equals target % mod
        // 可能加入的时候已经过了很多秒
        target = target + tick.get();
        return target & (mod - 1);
    }

    private int cycleNum(int target, int mod) {
        //equals target/mod
        return target >> Integer.bitCount(mod - 1);
    }

    public abstract static class Task implements Runnable {

        private int index;

        private int cycleNum;

        private int key;

        /**
         * The unique ID of the task
         */
        private int taskId ;

        @Override
        public void run() {
        }

        public int getKey() {
            return key;
        }

        /**
         *
         * @param key Delay time(seconds)
         */
        public void setKey(int key) {
            this.key = key;
        }

        public int getCycleNum() {
            return cycleNum;
        }

        private void setCycleNum(int cycleNum) {
            this.cycleNum = cycleNum;
        }

        public int getIndex() {
            return index;
        }

        private void setIndex(int index) {
            this.index = index;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }

    private class TriggerJob implements Runnable {

        @Override
        public void run() {
            int index = 0;
            while (!stop) {
                try {
                    // 移除指定index处到期的任务，如果移除速度够慢，循环时间够短，会冲突吧？
                    Set<Task> tasks = remove(index);
                    logger.info(Thread.currentThread().getName() + " 第" + tick + "次移除任务，共获得" + tasks.size());
                    // 提交给线程池
                    for (Task task : tasks) {
                        executorService.submit(task);
                    }

                    if (++index > bufferSize - 1) {
                        index = 0;
                    }

                    // Total tick number of records
                    tick.incrementAndGet();
                    // 还是需要一个线程轮询
                    TimeUnit.SECONDS.sleep(1);

                } catch (Exception e) {
                    logger.error("Exception", e);
                }

            }

            logger.info(Thread.currentThread().getName() + " Delay task has stopped");
        }
    }

}
