package de.tuberlin.swt.prog2.threads.pool;

public class QuickSortTest {

	public static void main(String[] args) {
		int[] data = new int[1000000];
		
		// create some random data
		for (int i = 0; i < data.length; i++) {
			data[i] = (int) (Math.random() * 10000);
		}

        QuickSortPooled sorter = new QuickSortPooled();
        long start = System.currentTimeMillis();
        sorter.quickSort(data);
        long end = System.currentTimeMillis();
        System.out.println("Time " + (end - start) + " ms");
        sorter.pool.shutdown();
	}
}
