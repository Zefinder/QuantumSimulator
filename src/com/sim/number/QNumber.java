package com.sim.number;

import java.util.Random;

public class QNumber {

	private final Random generator;
	private final int bitNumber;
	private ComplexMatrix coefficientVector;

	public QNumber(int bitNumber, Complex... coefficients) {
		generator = new Random();
		this.bitNumber = bitNumber;
		
		int fillNumber = (1 << bitNumber);
		ComplexMatrix.Builder builder = new ComplexMatrix.Builder();
		
		// If no coefficients, then everything on state 0
		if (coefficients.length == 0) {
			fillNumber -= 1;
			builder.putComplex(Complex.ONE);
		} else {
			fillNumber -= coefficients.length;
			if (fillNumber < 0) {
				fillNumber = 0;
			}

			for (Complex coefficient : coefficients) {
				builder.putComplex(coefficient);
			}
		}
		
		// May cause inconsistencies... anyways
		for (int i = 0; i < fillNumber; i++) {
			builder.putComplex(Complex.ZERO);
		}

		// Coefficient vector is a column vector
		coefficientVector = builder.build(1);
	}
	
	public QNumber(int bitNumber, ComplexMatrix coefficients) {
		this(bitNumber, matrixToArray(coefficients));
	}

	private static Complex[] matrixToArray(ComplexMatrix matrix) {
		int rows = matrix.getRows();
		Complex[] coefficients = new Complex[rows];
		for (int i = 0; i < rows; i++) {
			coefficients[i] = matrix.get(i, 0);
		}
		
		return coefficients;
	}
	
	public int check() {
		int result = -1;
		int row = coefficientVector.getRows();
		double random = generator.nextDouble();
		double cumulativeProba = 0;

		// Search for number
		for (int i = 0; i < row; i++) {
			Complex c = coefficientVector.get(i, 0);
			cumulativeProba += c.norm();

			if (random < cumulativeProba) {
				result = i;
				break;
			}
		}

		// If nothing found, then it is the last number and a double precision
		// problem...
		if (result == -1) {
			result = row - 1;
		}

		// Regenerate the coefficient vector with probability 1 for result.
		ComplexMatrix.Builder builder = new ComplexMatrix.Builder();
		for (int i = 0; i < row; i++) {
			builder.putComplex(i == result ? Complex.ONE : Complex.ZERO);
		}
		coefficientVector = builder.build(1);

		return result;
	}

	/**
	 * This has no meaning when used in an algorithm...
	 */
	public ComplexMatrix getCoefficientVector() {
		return coefficientVector;
	}
	
	public int getBitNumber() {
		return bitNumber;
	}

	@Override
	public String toString() {
		return coefficientVector.toString();
	}

}
