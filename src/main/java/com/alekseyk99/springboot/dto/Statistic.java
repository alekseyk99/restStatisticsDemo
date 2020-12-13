package com.alekseyk99.springboot.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** 
 * Stores statistic data
 * 
 */
public class Statistic {
	
	private final double sum, avg, max, min;
	private final long count;

	@JsonCreator
	public Statistic(
	        @JsonProperty("sum") double sum, 
	        @JsonProperty("avg") double avg, 
	        @JsonProperty("max") double max, 
	        @JsonProperty("min") double min, 
	        @JsonProperty("count") long count) {
		this.sum = sum;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.count = count;
	}
	
	public double getSum() {
		return sum;
	}

	public double getAvg() {
		return avg;
	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}

	public long getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "Statistic [sum=" + sum + ", avg=" + avg + ", max=" + max + ", min=" + min + ", count=" + count + "]";
	}
	
}
