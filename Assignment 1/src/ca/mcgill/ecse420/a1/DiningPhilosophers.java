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

			philosophers[i] = new Philosopher(leftChopstick, rightChopstick);
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
					synchronized (leftChopstick) {
						//Get right chopstick
						synchronized (rightChopstick) {
							// Can eat
							eat();
						}
					}

					// Puts down chopsticks as sync ends
					think();
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
