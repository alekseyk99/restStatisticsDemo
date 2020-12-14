package com.alekseyk99.springboot.service;

import static org.junit.Assert.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import com.alekseyk99.springboot.dto.Statistic;
import com.alekseyk99.springboot.dto.Transaction;

public class ServiceTest {
	
	private static final double DELTA = 1e-15;

	@Test
	public void testStatistic() throws InterruptedException {
		TransactionStatisticsService service = new TransactionStatisticsService();
		long time = System.currentTimeMillis();
		Statistic statistic = service.getStatistic();
		assertEquals("Step1 Sum", 0, statistic.getSum(), DELTA);
		assertEquals("Step1 Min", 0, statistic.getMin(), DELTA);
		assertEquals("Step1 Max", 0, statistic.getMax(), DELTA);
		assertEquals("Step1 Avg", 0, statistic.getAvg(), DELTA);
		assertEquals("Step1 Count", 0, statistic.getCount());
		
		Transaction transaction = new Transaction(10, time-59000);
		service.addTransaction(transaction);
		statistic = service.getStatistic();
		assertEquals("Step2 Sum", 10, statistic.getSum(), DELTA);
		assertEquals("Step2 Min", 10, statistic.getMin(), DELTA);
		assertEquals("Step2 Max", 10, statistic.getMax(), DELTA);
		assertEquals("Step2 Avg", 10, statistic.getAvg(), DELTA);
		assertEquals("Step2 Count", 1, statistic.getCount());
		
		transaction = new Transaction(20, time-58000);
		service.addTransaction(transaction);
		statistic = service.getStatistic();
		assertEquals("Step3 Sum", 30, statistic.getSum(), DELTA);
		assertEquals("Step3 Min", 10, statistic.getMin(), DELTA);
		assertEquals("Step3 Max", 20, statistic.getMax(), DELTA);
		assertEquals("Step3 Avg", 15, statistic.getAvg(), DELTA);
		assertEquals("Step3 Count", 2, statistic.getCount());
		
		Thread.sleep(1000);
		
		statistic = service.getStatistic();
		assertEquals("Step4 Sum", 20, statistic.getSum(), DELTA);
		assertEquals("Step4 Min", 20, statistic.getMin(), DELTA);
		assertEquals("Step4 Max", 20, statistic.getMax(), DELTA);
		assertEquals("Step4 Avg", 20, statistic.getAvg(), DELTA);
		assertEquals("Step4 Count", 1, statistic.getCount());
		
		Thread.sleep(1000);
		
		statistic = service.getStatistic();
		assertEquals("Step5 Sum", 0, statistic.getSum(), DELTA);
		assertEquals("Step5 Min", 0, statistic.getMin(), DELTA);
		assertEquals("Step5 Max", 0, statistic.getMax(), DELTA);
		assertEquals("Step5 Avg", 0, statistic.getAvg(), DELTA);
		assertEquals("Step5 Count", 0, statistic.getCount());
		
	}

	@Test
	public void testMultiThread() throws InterruptedException {

        TransactionStatisticsService service = new TransactionStatisticsService();
        
        long startMillis = System.currentTimeMillis();
    	int numThreads = 100;
    	int numIter = 100;
        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < numThreads; i++) {
            threadPool.submit(new Runnable() {
                public void run() {
                    for (int i=0; i < numIter; i++) {
                    Transaction transaction = new Transaction(10, System.currentTimeMillis());
                    service.addTransaction(transaction);
                    }
                }
            });
        }
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.SECONDS);
        long totalMillis = System.currentTimeMillis() - startMillis;
        Statistic statistic = service.getStatistic();
        
        assertEquals("Step5 Sum", 100000, statistic.getSum(), DELTA);
        assertEquals("Step5 Min", 10, statistic.getMin(), DELTA);
        assertEquals("Step5 Max", 10, statistic.getMax(), DELTA);
        assertEquals("Step5 Avg", 10, statistic.getAvg(), DELTA);
        assertEquals("Step5 Count", 10000, statistic.getCount());
        
        System.out.println(String.format("totalMillis=%s", totalMillis)); 

    }

}
