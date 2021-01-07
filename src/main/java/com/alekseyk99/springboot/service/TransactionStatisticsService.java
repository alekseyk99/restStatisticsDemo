package com.alekseyk99.springboot.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import com.alekseyk99.springboot.dto.Statistic;
import com.alekseyk99.springboot.dto.Transaction;

/** 
 * Keeps track of transactions and calculates general statistics
 * 
 * @author Aleksei Korchak
 *
 */
@Repository
@Scope("singleton")
public class TransactionStatisticsService {

    private static final Logger logger = LogManager.getLogger(TransactionStatisticsService.class);

	// size of time period to keep data
	private static final int CAPACITY = 60 ; // 1 minute
	// size of an interval (time slot) in milliseconds
	private static final long INTERVAL_SIZE = 1000L ; // 1 second

	private final Entry[] entries  = new Entry[CAPACITY];
	
   public TransactionStatisticsService() {
        for (int i=0; i< entries.length; i++){
            entries[i]=new Entry();
        }
    }

	/**
	 * Keeps statistic of particular time slot 
	 * 
	 */
	private class Entry {
	    
		private long slotTime; // = timestamp/INTERVAL_SIZE
		private double sum;
		private double min;
		private double max;
		private long count;
		
		public Entry() {}
		
		public Entry(long slotTime, double sum, double min, double max, long count) {
			this.sum = sum;
			this.min = min;
			this.max = max;
			this.count = count;
			this.slotTime = slotTime;
		}
		/**
		 * Updates statistic if time is right
		 * 
		 * @param time Transaction time
		 * @param amount Amount of transaction
		 */
		synchronized void add(long time, double amount) {
			if (slotTime == time) { // slot holds data for that time, update
				logger.debug("Same time"); 
				sum += amount;
				count++;
				if (min > amount) {
					min = amount;
				}
				if (max < amount) {
					max = amount;
				}
			} else if (slotTime < time) { // added time is newer than existing, replace
				logger.debug("old data"); 
				slotTime = time;
				sum = amount;
				count = 1;
				min = amount;
				max = amount;
			}  // else: added time is older than existing, skip

		}
		
		/**
		 * process only new data
		 * 
		 * @param startTimestampNormal
		 * @return synchronized copy
		 */
		synchronized Entry get() {
			return new Entry(slotTime, sum, min, max, count);
		}

	}
	
	/** 
	 * Adds transaction information to statistics
	 * 
	 * @param transaction
	 */
	public void addTransaction(Transaction transaction) {
		
		// Normalized transactionTimestamp
		long transactionSlotTime = transaction.getTimestamp()/INTERVAL_SIZE;
		// index in entries to keep data
		int index = (int)(transactionSlotTime % CAPACITY);
		
		logger.debug("addTransaction transactionSlotTime={} index={}", transactionSlotTime,index); 
		entries[index].add(transactionSlotTime, transaction.getAmount());
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
		for (int i=0; i< entries.length; i++) {
			final Entry entry = entries[i].get();
			if (entry.slotTime > startTimestampNormal) {
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
