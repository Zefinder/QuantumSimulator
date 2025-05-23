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
			for (int j = 0; j < bColumns; j++) {
				Complex c = Complex.ofCartesian(0, 0);
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
		/*
		 * For Kronecker product, if A is a m x n matrix and B is a p x q matrix, the
		 * resulting matrix will have size mp x nq
		 */
		Complex[][] otherValues = other.values;

		int aRows = values.length;
		int aColumns = column;
		int bRows = otherValues.length;
		int bColumns = other.column;

		Complex[][] newValues = new Complex[aRows * bRows][aColumns * bColumns];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < column; j++) {
				Complex coeff = values[i][j];
				ComplexMatrix tmpMatrix = other.timesConstant(coeff);
				Complex[][] tmpValues = tmpMatrix.values;

				for (int k = 0; k < bRows; k++) {
					for (int l = 0; l < bColumns; l++) {
						newValues[k + i * bRows][l + j * bColumns] = tmpValues[k][l];
					}
				}
			}
		}

		ComplexMatrix newMatrix = new ComplexMatrix(aColumns * bColumns, newValues);
		return newMatrix;
	}

	public ComplexMatrix add(ComplexMatrix other) throws MatrixProductException {
		Complex[][] otherValues = other.values;

		int aRows = values.length;
		int aColumns = column;
		int bRows = otherValues.length;
		int bColumns = other.column;

		if (aRows != bRows || aColumns != bColumns) {
			throw new MatrixProductException("The number of rows and columns of both matrices must be the same");
		}

		Complex[][] newValues = new Complex[aRows][aColumns];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < column; j++) {
				newValues[i][j] = values[i][j].add(otherValues[i][j]);
			}
		}
		
		ComplexMatrix newMatrix = new ComplexMatrix(aColumns, newValues);
		return newMatrix;
	}
	
	public ComplexMatrix sub(ComplexMatrix other) throws MatrixProductException {
		Complex[][] otherValues = other.values;

		int aRows = values.length;
		int aColumns = column;
		int bRows = otherValues.length;
		int bColumns = other.column;

		if (aRows != bRows || aColumns != bColumns) {
			throw new MatrixProductException("The number of rows and columns of both matrices must be the same");
		}

		Complex[][] newValues = new Complex[aRows][aColumns];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < column; j++) {
				newValues[i][j] = values[i][j].subtract(otherValues[i][j]);
			}
		}
		
		ComplexMatrix newMatrix = new ComplexMatrix(aColumns, newValues);
		return newMatrix;
	}

	public ComplexMatrix conjugateTranspose() {
		Complex[][] newValues = new Complex[column][values.length];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < column; j++) {
				newValues[j][i] = values[i][j].conj();
			}
		}

		ComplexMatrix newMatrix = new ComplexMatrix(values.length, newValues);
		return newMatrix;
	}

	/**
	 * Returns the value in the matrix, or 0 if out of bounds
	 */
	public Complex get(int i, int j) {
		if (i < 0 || i >= values.length || j < 0 || j >= column) {
			return Complex.ZERO;
		}

		return values[i][j];
	}

	public int getColumns() {
		return column;
	}

	public int getRows() {
		return values.length;
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
				Complex a = values[i][j];
				Complex b = otherValues[i][j];
				if (!a.equals(b)) {
					return false;
				}
			}
		}

		return true;
	}

}
