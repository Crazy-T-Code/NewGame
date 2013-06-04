package de.tuberlin.swt.prog2.threads.pipes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class PipeDemo {
	
		public static void main(String[] args) {
			//TODO: create and connect piped streams
			
			//TODO: create sender and receiver thread
			
			//TODO: interrupt sender after 10 seconds, wait for receiver to finish.
		}
	}

/******************** internal class Sender *************************/

	 class Sender extends Thread {
		
		DataOutputStream out;
		
		public Sender(PipedOutputStream out){
			this.out = new DataOutputStream(out);
		}

		@Override
		public void run() {
			
			int sleepytime;
			while(true) {
				try {
					sleepytime = (int)(Math.random()*1000);
					sleep(sleepytime);
					out.writeInt(sleepytime);
					out.flush();
				} catch (Exception e) {
					System.err.println(this.getName() +": "+e.getMessage());
				}
			}
		}
	}
	
/******************** \internal class Sender *************************/
	
/********************* internal class Receiver ***********************/
	 class Receiver extends Thread {

		DataInputStream in;

		public Receiver(PipedInputStream in){
			this.in = new DataInputStream(in);
		}

		@Override
		public void run() {
			int i;
			while(true){
				try{
					i = in.readInt();
						System.out.println(this.getName() +": "+ "Sender slept for "+ i+"ms");

				}catch(IOException e){
					System.err.println(this.getName() +": "+e.getMessage());
				}

			}
			
		}
	}

/********************* \internal class Receiver ***********************/

