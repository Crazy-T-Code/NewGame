package de.tuberlin.swt.prog2.threads.basics;


public class QuickSort extends Thread {
	
	private float[] toSort;
	private int lower;
	private int upper;
	
	public QuickSort(float[] toSort, int pivot, int length) {
		this.toSort = toSort;
		this.lower = lower;
		this.upper = upper;
	}

	// TODO: constructor, fields, run()
	
/****************************************\Quicksort***********************************************************/
	/*
	 * does always return the time it took to sort the array
	 */
	public static void quickSortMultithreaded(float[] toSort){
		int pivot = partition(toSort, toSort[toSort.length/2], 0, toSort.length -1);
		
		QuickSort q1 = new QuickSort(toSort, 0, pivot-1);
		QuickSort q2 = new QuickSort(toSort, pivot, toSort.length -1);	
		
		q1.start();
		q2.start();
		
		try {
			q1.join();
			q2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void run(){
		qSort(toSort,lower, upper);
		
	}
	
	/*
	 * method for single threaded sorting
	 */
	public static void quickSortSequential(float[] toSort){
		qSort(toSort, 0, toSort.length-1);
	}
	
	
	/*
	 * actual recursive sorting algorithm used by the threads
	 */
	private static void qSort(float[] toSort, int lower, int upper){
		int left;
		float pivot = toSort[lower + (upper-lower) / 2];
		
		left = partition(toSort, pivot, lower, upper);
		
		if (lower < left-1){
			qSort(toSort, lower, left-1);
		}
			
		if (left < upper){
			qSort(toSort, left, upper);
		}
	}
	

	/**
	 * Partitions the values between indices left and right in given float
	 * array according to the given pivot value. Returns the index of the pivot
	 * element (last index of the lower sub array).
	 */
	private static int partition(float[] toSort, float pivot, int left, int right){
	    
		while (left <= right) {
			while (toSort[left] < pivot) {
				left++;
			}
			while (toSort[right] > pivot) {
				right--;
			}
			if (left <= right) {
				swap(toSort, left, right);
				left++;
				right--;
			}
		}
		
		return left;	
	}
	
	private static void swap(float[] array, int i, int j){
		float tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}

 /****************************************\Quicksort***********************************************************/


}
