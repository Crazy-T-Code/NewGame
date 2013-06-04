package de.tuberlin.swt.prog2.threads.basics;

import java.util.Arrays;

public class QuickSortBenchmark {
	/************************************* Utilities********************************************************/
	public static boolean isSorted(float[] array){
		float currentMax = Float.MIN_VALUE;
		
		for(int i = 0; i < array.length; i++){
			if(currentMax > array[i]){
				return false;
			}
			currentMax = array[i];
		}
		
		return true;
		
	}	
	
	public static void printFloatArray(float[] array, int noLines){
		
		if(array.length <= noLines){
			System.out.println("Vals 1-" + array.length + "\n");
			for(int i = 0; i<array.length; i++){
				System.out.println(array[i]);
			}
		}else{
			int cols = 1 + array.length/noLines;
			String line="";
			for(int i = 0; i<cols; i++){
				line+=stringPad17("Vals "+(i*noLines+1)+"-"+Math.min(array.length,((i+1)*noLines)));
			}
			System.out.println(line +"\n");
			
			for(int i = 0; i<noLines; i++){
				line = "";
				for(int j = i;j< array.length;j=j+noLines){
					line+=stringPad17(""+array[j]);
				}
				System.out.println(line);
			}
		}
		System.out.println();
	}
	
	private static String stringPad17(String toPad){
		return String.format("%1$-" + 17 + "s", toPad);
	}
/************************************\ Utilities********************************************************/	
		
	public static void main(String[] args)  {

		System.out.println("*** initializing ***");
		int length = 9999999;//
		float[] arr1 = new float[length];
		for(int i = 0; i< arr1.length; i++){
			arr1[i] = (float)Math.random();
		}	
		float[] arr2 = Arrays.copyOf(arr1, arr1.length);
		
		System.out.println("***     done     ***");
		System.out.println("********************");
		System.out.println();
		System.out.println("*** benchmarking ***");
		System.out.println("#floats: " + length);
		
		long time1 = System.currentTimeMillis();
		QuickSort.quickSortSequential(arr1);
		time1 = System.currentTimeMillis() - time1;
		
		long time2 = System.currentTimeMillis();
		QuickSort.quickSortMultithreaded(arr2);
		time2 = System.currentTimeMillis() - time2;

		// printFloatArray(arr1,50);
		// printFloatArray(arr2,50);
		System.out.println("Array 1 sorted: "+isSorted(arr1));
		System.out.println("Array 2 sorted: "+isSorted(arr2));

		System.out.println("Singlethreaded Quicksort:   " + time1 + "ms");
		System.out.println("Multithreaded Quicksort:    " + time2 + "ms");
		System.out.println("***     done     ***");
			
	}
}
