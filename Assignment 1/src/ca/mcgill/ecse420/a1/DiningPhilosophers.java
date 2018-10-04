package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {
	
	public static void main(String[] args) {

		int numberOfPhilosophers = 5;
		Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
		Object[] chopsticks = new Object[numberOfPhilosophers];

		// Create object for each chopstick
		for (int i = 0; i < numberOfPhilosophers; i++) {
			Object chopstick = new Object();
			chopsticks[i] = chopstick;
		}

		// Create Philosophers
		for (int i = 0; i < numberOfPhilosophers; i++) {
			Object leftChopstick = chopsticks[i];

			Object rightChopstick;
			if (i+1 < numberOfPhilosophers) {
				rightChopstick = chopsticks[(i + 1)];
			} else {
				rightChopstick = chopsticks[1];
			}


			// Deadlock occurs when all philosophers are holding one chopstick
			// If last philospher picks up right instead of left first, deadlock avoided

			if(i < numberOfPhilosophers -1){
				philosophers[i] = new Philosopher(leftChopstick, rightChopstick);
			} else {
				philosophers[i] = new Philosopher(rightChopstick, leftChopstick);
			}

			Thread t = new Thread(philosophers[i], "Philosopher " + (i + 1));
			t.start();
		}

	}

	public static class Philosopher implements Runnable {

		private Object leftChopstick;
		private Object rightChopstick;

		public Philosopher(Object leftChopstick, Object rightChopstick) {
			this.leftChopstick = leftChopstick;
			this.rightChopstick = rightChopstick;
		}

		@Override
		public void run() {
			try {
				while(true) {

					// Thinking
					think();

					// Get left chopstick
					synchronized (leftChopstick) { // Sync ensures only one thread accesses object at once

						// Wait time between left pick up and right pick up
						Thread.sleep(((int) (Math.random() * 100)));

						//Get right chopstick
						synchronized (rightChopstick) { // Can only attempt to pick up right if already has left (Deadlock avoidance)
							// Can eat
							eat();
						}
					}

					// Puts down chopsticks as sync ends
					think();

					// Thinking at end ensures time for other threads to pick up (avoids starvation)
					// Starvation could be explicitly avoided by placing chopstick requests in priority queue
					// Not necessary for requests of 2 resources and low number of consumers
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		private void think() throws InterruptedException {
			System.out.println(Thread.currentThread().getName() + " is Thinking");
			Thread.sleep(((int) (Math.random() * 100)));
		}

		private void eat() throws InterruptedException {
			System.out.println(Thread.currentThread().getName() + " is Eating");
			Thread.sleep(((int) (Math.random() * 100)));
		}

	}

}
