package com.sim.number;

import java.util.ArrayList;
import java.util.List;

import com.sim.exception.MatrixInitException;

public class ComplexMatrix {

	private final int column;
	private final Complex[][] values;

	private ComplexMatrix(int column, Complex[][] values) {
		this.column = column;
		this.values = values;
	}

	public ComplexMatrix(int column, Complex... values) throws MatrixInitException {
		int len = values.length;
		if (len % column == 0) {
			throw new MatrixInitException("All rows must have column values!");
		}

		this.column = column;
		int row = len / column;
		this.values = new Complex[row][column];
	}

	public ComplexMatrix timesConstant(Complex constant) {
		return null;
	}

	public ComplexMatrix product(ComplexMatrix other) {
		return null;
	}

	public ComplexMatrix kronecker(ComplexMatrix other) {
		return null;
	}

	/**
	 * Builder for complex matrices. When building, if the number of values is not a
	 * multiple of the column, zeros will be added to complete the last row.
	 */
	public static class Builder {

		private List<Complex> values;

		public Builder() {
			values = new ArrayList<Complex>();
		}

		public Builder putCartesian(double real, double imaginary) {
			values.add(Complex.ofCartesian(real, imaginary));
			return this;
		}

		public Builder putPolar(double rho, double theta) {
			values.add(Complex.ofPolar(rho, theta));
			return this;
		}

		public Builder putCis(double x) {
			values.add(Complex.ofCis(x));
			return this;
		}
		
		public Builder putComplex(Complex c) {
			values.add(c);
			return this;
		}

		public ComplexMatrix build(int column) {
			int len = values.size();
			if (len % column != 0) {
				// Add remaining len % column;
				int rem = column - (len % column);
				
				for (int i = 0; i < rem; i++) {
					values.add(Complex.ofCartesian(0, 0));
				}
			}

			int row = len / column;
			Complex[][] matrix = new Complex[row][column];

			for (int i = 0; i < row; i++) {
				for (int j = 0; j < column; j++) {
					matrix[i][j] = values.get(i * column + j);
				}
			}

			return new ComplexMatrix(column, matrix);
		}
	}
	
	@Override
	public String toString() {
		String res = "";
		
		for (Complex[] row : values) {
			res += "[";
			
			for (Complex value : row) {
				res += value.toString() + ", ";
			}
			
			res = res.substring(0, res.length() - 2) + "]\n";
		}
		
		res = res.substring(0, res.length() - 1);
		return res;
	}

}
