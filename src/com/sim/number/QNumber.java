package com.sim.number;

public class QNumber {

	private final QBit[] qbits;
	
	public QNumber(int n) {
		qbits = new QBit[n];
		for (int i = 0; i < n; i++) {
			qbits[i] = new QBit();
		}
	}
	
}
