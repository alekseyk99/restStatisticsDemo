package com.alekseyk99.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("singleton")
public class Data {

	static final int CAPACITY = 60 ;
	static final long DIVISOR = 1000L ;
	final Bucket[] buckets  = new Bucket[CAPACITY];
	private static final Logger logger = LoggerFactory.getLogger(Data.class);
	
	public Data() {
		for (int i=0; i< buckets.length; i++){
			buckets[i]=new Bucket();
		}
	}

	public void addTransaction(Transaction transaction) {
		
		long transactionTimestampNormal = transaction.getTimestamp()/DIVISOR;
		int index = (int)(transactionTimestampNormal % CAPACITY);
		Bucket bucket;
		
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
		
		long startTimestamp = System.currentTimeMillis()/DIVISOR - CAPACITY;
		logger.debug("startTimestamp {}", startTimestamp); 
		for (int i=0; i< buckets.length; i++){ // for array its faster then foreach
			double tmpSum,tmpMin,tmpMax;
			long tmpCount, tmpTimestamp;
			Bucket bucket = buckets[i];
			synchronized(bucket) {
				tmpTimestamp = bucket.timestamp;
				tmpSum = bucket.sum;
				tmpMin = bucket.min;
				tmpMax = bucket.max;
				tmpCount = bucket.count;
			}
			if (tmpTimestamp > startTimestamp) { // skip old data
				logger.debug("find data"); 

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
		Statistic statistic = new Statistic();
		statistic.setSum(sum);
		statistic.setCount(count);
		statistic.setMin((count==0)?0:min);
		statistic.setMax(max);
		statistic.setAvg((count==0)?0:sum/count);
		return statistic;
	}
}
