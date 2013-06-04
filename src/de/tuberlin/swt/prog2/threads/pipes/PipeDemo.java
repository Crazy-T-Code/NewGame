package de.tuberlin.swt.prog2.threads.pipes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class PipeDemo {

	public static void main(String[] args) {
		try {
			// TODO: create and connect piped streams
			PipedOutputStream pipeOut = new PipedOutputStream();
			PipedInputStream pipeIn = new PipedInputStream(pipeOut);

			Sender sender = new Sender(pipeOut);
			sender.start();
			Receiver receiver = new Receiver(pipeIn);
			receiver.start();

			// TODO: interrupt sender after 10 seconds, wait for receiver to
			Thread.sleep(10000);

			sender.interrupt();
			// finish.

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

/******************** internal class Sender *************************/

class Sender extends Thread {

	DataOutputStream out;

	public Sender(PipedOutputStream out) {
		this.out = new DataOutputStream(out);
	}

	@Override
	public void run() {
		boolean stopped = false;

		int sleepytime;
		while (!stopped && !this.isInterrupted()) {
			try {
				sleepytime = (int) (Math.random() * 1000);
				System.out.println(getName() + ": I sleep for " + sleepytime
						+ "ms now");
				sleep(sleepytime);
				out.writeInt(sleepytime);
				out.flush();
			} catch (Exception e) {
				System.err.println(this.getName() + ": " + e.getMessage());
				stopped = true;
			}
		}

		try {
			out.writeInt(-1);
			out.close();
		} catch (IOException e) {
			System.err.println(this.getName() + ": " + e.getMessage());
		}
	}
}

/******************** \internal class Sender *************************/

/********************* internal class Receiver ***********************/
class Receiver extends Thread {

	DataInputStream in;

	public Receiver(PipedInputStream in) {
		this.in = new DataInputStream(in);
	}

	@Override
	public void run() {
		int i;
		while (!isInterrupted()) {
			try {
				i = in.readInt();
				if (i == -1) {
					interrupt();
					System.out.println(this.getName()
							+ ": Sender signalled to stop. Terminating");
				} else
					System.out.println(this.getName() + ": "
							+ "Sender slept for " + i + "ms");

			} catch (IOException e) {
				System.err.println(this.getName() + ": " + e.getMessage());
			}

		}

	}
}

/********************* \internal class Receiver ***********************/

