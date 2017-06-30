package com.alekseyk99.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("singleton")
public class Service {
	
	static class Entry {
		double sum;
		double min;
		double max;
		long count;
		long timestamp;
	}
	
	// size of time interval to keep data
	// for 60 and INTERVAL_SIZE = 1000L keep data for the last minute
	static final int CAPACITY = 60 ;
	
	// size of an interval in milliseconds (for 1000 = 1 sec)
	static final long INTERVAL_SIZE = 1000L ;
	final Entry[] buckets  = new Entry[CAPACITY];
	private static final Logger logger = LoggerFactory.getLogger(Service.class);
	
	public Service() {
		for (int i=0; i< buckets.length; i++){
			buckets[i]=new Entry();
		}
	}

	public void addTransaction(Transaction transaction) {
		
		// Normalized transactionTimestamp
		long transactionTimestampNormal = transaction.getTimestamp()/INTERVAL_SIZE;

		// index in bucket to keep data
		int index = (int)(transactionTimestampNormal % CAPACITY);
		Entry bucket;
		
		logger.debug("transactionTimestampNormal {}", transactionTimestampNormal); 
		logger.debug("index {}", index); 

		bucket = buckets[index];
		synchronized(bucket) {
			if (bucket.timestamp == transactionTimestampNormal) {
				// already holds data for that timestamp
				logger.debug("Same timestamp"); 
				bucket.sum += transaction.getAmount();
				bucket.count++;
				if (bucket.min > transaction.getAmount()) {
					bucket.min = transaction.getAmount();
				}
				if (bucket.max < transaction.getAmount()) {
					bucket.max = transaction.getAmount();
				}
			} else if (bucket.timestamp < transactionTimestampNormal) { // or we can compare with System.currentTimeMillis()/DIVISOR - CAPACITY
				// old data, replace
				logger.debug("old data"); 
				bucket.timestamp = transactionTimestampNormal;
				bucket.sum = transaction.getAmount();
				bucket.count =1;
				bucket.min = transaction.getAmount();
				bucket.max = transaction.getAmount();
			}  
		}
	}
	
	public Statistic getStatistic() {
		double sum=0, max=0, min=Double.MAX_VALUE;
		long count=0;
		
		// Normalized start of an interval 
		long startTimestampNormal = System.currentTimeMillis()/INTERVAL_SIZE - CAPACITY;
		logger.debug("startTimestampNormal {}", startTimestampNormal); 
		for (int i=0; i< buckets.length; i++){ // for arrays its faster then foreach
			double tmpSum,tmpMin,tmpMax;
			long tmpCount, tmpTimestamp;
			Entry bucket = buckets[i];
			synchronized(bucket) {
				tmpTimestamp = bucket.timestamp;
				tmpSum = bucket.sum;
				tmpMin = bucket.min;
				tmpMax = bucket.max;
				tmpCount = bucket.count;
			}
			if (tmpTimestamp > startTimestampNormal) { // process only new data and skip old one
				logger.debug("Found data"); 

				sum+=tmpSum;
				count+=tmpCount;
				if (min > tmpMin) {
					min = tmpMin;
				}
				if (max < tmpMax) {
					max = tmpMax;
				}
			}
		}

		return new Statistic(sum,(count==0)?0:sum/count,max,(count==0)?0:min,count);
	}
}
