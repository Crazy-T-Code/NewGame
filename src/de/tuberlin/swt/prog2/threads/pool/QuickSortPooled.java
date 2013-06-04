package de.tuberlin.swt.prog2.threads.pool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class QuickSortPooled {

    ThreadPoolExecutor pool;
    AtomicInteger sorted;

    public QuickSortPooled() {
    }

    int partition(int[] toSort, int left, int right, float pivot) {
        while (left < right) {
            while (toSort[left] < pivot) {
                left++;
            }
            while (toSort[right] > pivot) {
                right--;
            }
            if ((toSort[left] > pivot) || (toSort[right] < pivot)) {
                swap(toSort, left, right);
            } else {
                if (left < right) {
                    left++;
                }
            }
        }
        return left;
    }

    void swap(int[] data, int i, int j) {
        int x = data[i];
        data[i] = data[j];
        data[j] = x;
    }

    private void quickSortWorker(final int[] data, final int left, final int right, final Thread toInterrupt) {
    }

    public void quickSort(final int[] data) {
        pool = new ThreadPoolExecutor(0, 20, 10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    	// pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        //pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        //pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1000);
        sorted = new AtomicInteger(0);
        
        return;
    }
}
