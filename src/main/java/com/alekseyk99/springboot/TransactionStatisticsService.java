package com.alekseyk99.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/** 
 * Keeps track of transactions and calculates general statistics
 * 
 * @author Aleksei Korchak
 *
 */
@Repository
@Scope("singleton")
public class TransactionStatisticsService {

	// size of time period to keep data
	static final int CAPACITY = 60 ; // 1 minute
	// size of an interval (time slot) in milliseconds
	static final long INTERVAL_SIZE = 1000L ; // 1 second
	private static final Logger logger = LoggerFactory.getLogger(TransactionStatisticsService.class);

	final Entry[] entries  = new Entry[CAPACITY];
	
	/**
	 * Keeps statistic of particular time slot 
	 * 
	 */
	static class Entry {
		private long time;
		private double sum;
		private double min;
		private double max;
		private long count;
		
		public Entry() {}
		
		public Entry(long time, double sum, double min, double max, long count) {
			this.sum = sum;
			this.min = min;
			this.max = max;
			this.count = count;
			this.time = time;
		}
		/**
		 * Updates statistic if time is right
		 * 
		 * @param time Transaction time
		 * @param amount Amount of transaction
		 */
		synchronized void add(long time, double amount) {
			if (this.time == time) {
				// already holds data for that time
				logger.debug("Same time"); 
				sum += amount;
				count++;
				if (min > amount) {
					min = amount;
				}
				if (max < amount) {
					max = amount;
				}
			} else if (this.time < time) {
				// old data, replace
				logger.debug("old data"); 
				this.time = time;
				sum = amount;
				count =1;
				min = amount;
				max = amount;
			}  // else param time is too old
		}
		
		synchronized Entry get() {
			return new Entry(time, sum, min, max, count);
		}

		public long getTime() {
			return time;
		}

		public double getSum() {
			return sum;
		}

		public double getMin() {
			return min;
		}

		public double getMax() {
			return max;
		}

		public long getCount() {
			return count;
		}
	}
	
	// constructor
	public TransactionStatisticsService() {
		for (int i=0; i< entries.length; i++){
			entries[i]=new Entry();
		}
	}

	/** 
	 * Adds transaction information to statistics
	 * 
	 * @param transaction
	 */
	public void addTransaction(Transaction transaction) {
		
		// Normalized transactionTimestamp
		long transactionTimestampNormal = transaction.getTimestamp()/INTERVAL_SIZE;
		// index in entries to keep data
		int index = (int)(transactionTimestampNormal % CAPACITY);
		
		logger.debug("addTransaction transactionTimestampNormal={} index={}", transactionTimestampNormal,index); 
		entries[index].add(transactionTimestampNormal, transaction.getAmount());
	}
	
	/**
	 * Calculates general statistics
	 * 
	 * @return Statistic
	 */
	public Statistic getStatistic() {
		double sum=0, max=0, min=Double.MAX_VALUE;
		long count=0;
		
		// Normalized start of an interval 
		long startTimestampNormal = System.currentTimeMillis()/INTERVAL_SIZE - CAPACITY;
		logger.debug("getStatistic startTimestampNormal {}", startTimestampNormal); 
		for (int i=0; i< entries.length; i++){ // for arrays its faster then foreach
			final Entry entry = entries[i].get();
			if (entry.time > startTimestampNormal) { // process only new data and skip old one
				sum+=entry.sum;
				count+=entry.count;
				if (min > entry.min) {
					min = entry.min;
				}
				if (max < entry.max) {
					max = entry.max;
				}
			}
		}

		return new Statistic(sum,(count==0)?0:sum/count,max,(count==0)?0:min,count);
	}
}
