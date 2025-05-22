package com.sim.number;

import java.text.DecimalFormat;
import java.util.Random;

public class QBit {

	private static final DecimalFormat df = new DecimalFormat("0.0###");

	private final Random generator;

	/** Probability for 0 state */
	private Complex zeroStateValue;

	/** Probability for 1 state */
	private Complex oneStateValue;

	private Complex[] vector;

	public QBit(Complex zeroStateValue, Complex oneStateValue) {
		this.zeroStateValue = zeroStateValue;
		this.oneStateValue = oneStateValue;
		this.vector = new Complex[] { zeroStateValue, oneStateValue };
		generator = new Random();
	}

	/**
	 * Initializes s a qbit to zero
	 */
	public QBit() {
		this(Complex.ONE, Complex.ZERO);
	}

	public Complex getZeroStateValue() {
		return zeroStateValue;
	}

	/**
	 * Returns the actual zero state probability (ONLY FOR TESTING!)
	 */
	public double getZeroStateProbability() {
		return zeroStateValue.norm();
	}

	public Complex getOneStateValue() {
		return oneStateValue;
	}

	/**
	 * Return the actual one state probability
	 */
	public double getOneStateProbability() {
		return oneStateValue.norm();
	}

	public Complex[] getVector() {
		return vector;
	}

	/**
	 * Checks the qbit value. Checking its value will entirely define its state. For
	 * instance, let z having a 70% to get a 0 and a 30% to get a 1. Checking z's
	 * value gives 1. This will make its probability to get a 0 to 0% and to get a 1
	 * to 100%
	 */
	public int check() {
		int res;
		if (generator.nextDouble() <= getZeroStateProbability()) {
			res = 0;
			zeroStateValue = Complex.ONE;
			oneStateValue = Complex.ZERO;
		} else {
			res = 1;
			zeroStateValue = Complex.ZERO;
			oneStateValue = Complex.ONE;
		}

		return res;
	}

	@Override
	public String toString() {
		return "[0: %s (%s),1: %s (%s)]".formatted(zeroStateValue.toString(), df.format(getZeroStateProbability()),
				oneStateValue.toString(), df.format(getOneStateProbability()));
	}

}
