package de.tuberlin.swt.prog2.threads.basics;


public class QuickSort extends Thread {
	// TODO: constructor, fields, run()
	
/****************************************\Quicksort***********************************************************/
	/*
	 * does always return the time it took to sort the array
	 */
	public static void quickSortMultithreaded(float[] toSort){
		// TODO: -- prepare multithreaded execution
		//	 -- create two Threads, assign them intervals of the float-array
		//	 -- wait for the threads to terminate execution.
				
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
