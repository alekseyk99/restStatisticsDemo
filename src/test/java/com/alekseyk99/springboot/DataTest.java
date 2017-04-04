package com.alekseyk99.springboot;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class DataTest {
	
	private static final double DELTA = 1e-15;

	@Test
	public void testData() throws InterruptedException {
		Data data = new Data();
		Statistic statistic = data.getStatistic();
		assertEquals("sum ", 0.0, statistic.getSum(), DELTA);
		assertEquals("min ", 0.0, statistic.getMin(), DELTA);
		assertEquals("max ", 0.0, statistic.getMax(), DELTA);
		assertEquals("avg ", 0.0, statistic.getAvg(), DELTA);
		assertEquals("count ", 0, statistic.getCount());
		
		Transaction transaction = new Transaction();
		transaction.setAmount(1.1);
		transaction.setTimestamp(System.currentTimeMillis());
		data.addTransaction(transaction);
		statistic = data.getStatistic();
		assertEquals("sum ", 1.1, statistic.getSum(), DELTA);
		assertEquals("min ", 1.1, statistic.getMin(), DELTA);
		assertEquals("max ", 1.1, statistic.getMax(), DELTA);
		assertEquals("avg ", 1.1, statistic.getAvg(), DELTA);
		assertEquals("count ", 1, statistic.getCount());
		
		transaction.setAmount(1.1);
		transaction.setTimestamp(System.currentTimeMillis());
		data.addTransaction(transaction);
		statistic = data.getStatistic();
		assertEquals("sum ", 2.2, statistic.getSum(), DELTA);
		assertEquals("min ", 1.1, statistic.getMin(), DELTA);
		assertEquals("max ", 1.1, statistic.getMax(), DELTA);
		assertEquals("avg ", 1.1, statistic.getAvg(), DELTA);
		assertEquals("count ", 2, statistic.getCount());
		
		transaction.setAmount(4.4);
		transaction.setTimestamp(System.currentTimeMillis());
		data.addTransaction(transaction);
		statistic = data.getStatistic();
		assertEquals("sum ", 6.6, statistic.getSum(), DELTA);
		assertEquals("min ", 1.1, statistic.getMin(), DELTA);
		assertEquals("max ", 4.4, statistic.getMax(), DELTA);
		assertEquals("avg ", 2.2, statistic.getAvg(), DELTA);
		assertEquals("count ", 3, statistic.getCount());
	
		Thread.sleep(30000);
		
		transaction.setAmount(1.1);
		transaction.setTimestamp(System.currentTimeMillis());
		data.addTransaction(transaction);
		
		Thread.sleep(35000);
		
		statistic = data.getStatistic();
		assertEquals("sum ", 1.1, statistic.getSum(), DELTA);
		assertEquals("min ", 1.1, statistic.getMin(), DELTA);
		assertEquals("max ", 1.1, statistic.getMax(), DELTA);
		assertEquals("avg ", 1.1, statistic.getAvg(), DELTA);
		assertEquals("count ", 1, statistic.getCount());
		
	}

}
