import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadInOrderDemo {
    public static final int CORE_THREAD_SIZE = 3;

    public static final int MAX_THREAD_SIZE = 10;

    public static void main(String[] args) {
        new ThreadInOrderDemo();
    }

    /**
     * Totally 10 threads, but only 3 of them can be executed at a time.
     */
    private void testSemaphore() {
        final Semaphore sem = new Semaphore(CORE_THREAD_SIZE);
        Executor exe = new ThreadPoolExecutor(CORE_THREAD_SIZE, MAX_THREAD_SIZE, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));

        for (int i = 0; i < MAX_THREAD_SIZE; i++) {
            final int threadName = i;
            exe.execute(() -> {
                try {
                    sem.acquire();
                    System.out.println(threadName);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    sem.release();
                }
            });
        }
    }

    private void printInOrder() {
        final CountDownLatch latch = new CountDownLatch(CORE_THREAD_SIZE);
        final Semaphore semaphoreA = new Semaphore(1);
        final Semaphore semaphoreB = new Semaphore(0);
        final Semaphore semaphoreC = new Semaphore(0);

        ThreadPoolExecutor exe = new ThreadPoolExecutor(CORE_THREAD_SIZE, MAX_THREAD_SIZE, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));
        //TaskA
        exe.execute(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    semaphoreA.acquire();
                    System.out.println("A:" + i);
                    Thread.sleep(1000);
                    semaphoreB.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        //TaskB
        exe.execute(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    semaphoreB.acquire();
                    System.out.println("B:" + i);
                    Thread.sleep(1000);
                    semaphoreC.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        //TaskC
        exe.execute(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    semaphoreC.acquire();
                    System.out.println("C:" + i);
                    Thread.sleep(1000);
                    semaphoreA.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        // Wait for all thread task done.
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //At the end kill the thread pool.
        exe.shutdown();

    }

    public ThreadInOrderDemo() {
        printInOrder();
    }

}
