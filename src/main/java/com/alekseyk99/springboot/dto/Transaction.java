package com.alekseyk99.springboot.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** 
 * Stores transaction data
 * 
 */
public class Transaction {
	
    @DecimalMin(value="0.001", message = "Amount can't be 0")
	private final double amount;
    @Min(value=1, message = "Timestamp can't be 0")
	private final long timestamp;
	
    @JsonCreator
	public Transaction(@JsonProperty("amount") double amount, @JsonProperty("timestamp") long timestamp) {
        this.amount = amount;
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
