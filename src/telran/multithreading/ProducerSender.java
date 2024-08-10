package telran.multithreading;

import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class ProducerSender extends Thread {
	//HW #44 definition
	//dispatching functionality
	//two message boxes
	//even messages should be put to even message box
	//odd messages should be put to odd message box
	private BlockingQueue<String> oddMessageBox;
	private BlockingQueue<String> evenMessageBox;
	private int nMessages;
	
	public ProducerSender(BlockingQueue<String> oddMessageBox, 
			BlockingQueue<String> evenMessageBox, int nMessages) {
		this.oddMessageBox = oddMessageBox;
		this.evenMessageBox = evenMessageBox;
		this.nMessages = nMessages;
	}
	public void run() {
		IntStream.rangeClosed(1, nMessages)
			.forEach(i -> {				
					try {
		                BlockingQueue<String> targetBox = (i % 2 != 0) ? oddMessageBox : evenMessageBox;
		                targetBox.put("message" + i);
					} catch (InterruptedException e) {
						//no interrupt logics
					}			
			});
	}
	
}
