package com.sim.number;

import java.util.ArrayList;
import java.util.List;

import com.sim.exception.MatrixProductException;

public class ComplexMatrix {

	private final int column;
	private final Complex[][] values;

	private ComplexMatrix(int column, Complex[][] values) {
		this.column = column;
		this.values = new Complex[values.length][column];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < column; j++) {
				this.values[i][j] = values[i][j];
			}
		}
	}

	public ComplexMatrix timesConstant(Complex constant) {
		ComplexMatrix matrix = new ComplexMatrix(column, values);
		Complex[][] newValues = matrix.values;

		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < column; j++) {
				newValues[i][j] = newValues[i][j].multiply(constant);
			}
		}

		return matrix;
	}

	public ComplexMatrix product(ComplexMatrix other) throws MatrixProductException {
		/*
		 * For product of A and B, A's number of column must be equal to B's number of
		 * rows. The result is a matrix with A's number of line and B's number of column
		 */
		Complex[][] otherValues = other.values;

		int aRows = values.length;
		int aColumns = column;
		int bRows = otherValues.length;
		int bColumns = other.column;

		if (aColumns != bRows) {
			throw new MatrixProductException("The number of columns of the left matrix (this) must be equal "
					+ "to the number of the number of columns of the right matrix (other)");
		}

		Complex[][] newValues = new Complex[aRows][bColumns];
		for (int i = 0; i < aRows; i++) {
			Complex c = Complex.ofCartesian(0, 0);
			for (int j = 0; j < bColumns; j++) {
				for (int k = 0; k < aColumns; k++) {
					c = c.add(values[i][k].multiply(otherValues[k][j]));
				}
				newValues[i][j] = c;
			}
		}

		ComplexMatrix newMatrix = new ComplexMatrix(bColumns, newValues);
		return newMatrix;
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
				len += rem;
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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ComplexMatrix))
			return false;

		ComplexMatrix other = (ComplexMatrix) obj;
		Complex[][] otherValues = other.values;
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < column; j++) {
				if (!values[i][j].equals(otherValues[i][j])) {
					return false;
				}
			}
		}

		return true;
	}

}
