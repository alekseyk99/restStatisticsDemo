package com.alekseyk99.springboot;


/** 
 * Stores transaction data
 * 
 * @author Aleksei Korchak
 *
 */
public class Transaction {
	
	private double amount;
	private long timestamp;
	
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public double getAmount() {
		return amount;
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	@Override
	public String toString() {
		return "Transaction [amount=" + amount + ", timestamp=" + timestamp + "]";
	}
}
