package telran.multithreading;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class SenderReceiverAppl {
	// for HW #44 (ConsumerReceiver should not be updated)
	// Provide functionality of dispatching
	// Even messages must be processed by receiver threads with even id
	// Odd messages must be processed by receiver threads with odd id
	// Hints two message boxes: one for even messages and other for odd messages

	private static final int N_MESSAGES = 2000;
	private static final int N_RECEIVERS = 10;

	public static void main(String[] args) throws InterruptedException {		
		BlockingQueue<String> oddMessageBox = new LinkedBlockingQueue<String>();
		BlockingQueue<String> evenMessageBox = new LinkedBlockingQueue<String>();
		ProducerSender sender = startSender(oddMessageBox, evenMessageBox, N_MESSAGES);
		ConsumerReceiver[] receivers = startReceivers(oddMessageBox, evenMessageBox, N_RECEIVERS);
		sender.join();
		stopReceivers(receivers);
		displayResult();
	}

	private static void displayResult() {
		System.out.printf("counter of processed messsages is %d\n", ConsumerReceiver.getMessagesCounter());
	}

	private static void stopReceivers(ConsumerReceiver[] receivers) throws InterruptedException {
		for (ConsumerReceiver receiver : receivers) {
			receiver.interrupt();
			receiver.join();
		}
	}

	private static ConsumerReceiver[] startReceivers(BlockingQueue<String> evenMessageBox,
			BlockingQueue<String> oddMessageBox, int nReceivers) {
		ConsumerReceiver[] receivers = IntStream.range(0, nReceivers).mapToObj(i -> {
			ConsumerReceiver receiver = new ConsumerReceiver();					
			// Even ID receivers consume even messages; odd ID receivers consume odd messages			
			receiver.setMessageBox(receiver.threadId() % 2 != 0 ? evenMessageBox : oddMessageBox);
			return receiver;
		}).toArray(ConsumerReceiver[]::new);
		Arrays.stream(receivers).forEach(ConsumerReceiver::start);		
		return receivers;
	}

	private static ProducerSender startSender(BlockingQueue<String> oddMessageBox, BlockingQueue<String> evenMessageBox,
			int nMessages) {
		ProducerSender sender = new ProducerSender(oddMessageBox, evenMessageBox, nMessages);
		sender.start();
		return sender;
	}
	
}