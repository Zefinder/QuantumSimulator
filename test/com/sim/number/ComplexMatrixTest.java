package com.sim.number;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import com.sim.exception.MatrixProductException;

class ComplexMatrixTest {

	@Test
	void testMatrixBuildInsufficient() {
		Complex zero = Complex.ZERO;
		ComplexMatrix trueMatrix = new ComplexMatrix.Builder().putComplex(zero).putComplex(zero).putComplex(zero)
				.putComplex(zero).build(2);
		ComplexMatrix zeroMatrix = new ComplexMatrix.Builder().putComplex(zero).putComplex(zero).putComplex(zero)
				.build(2);

		assertEquals(trueMatrix, zeroMatrix);
	}

	@Test
	void testMatrixMultiplicationZero() {
		Complex zero = Complex.ZERO;

		// Matrix filled with zero
		ComplexMatrix trueMatrix = new ComplexMatrix.Builder().putComplex(zero).putComplex(zero).putComplex(zero)
				.putComplex(zero).build(2);

		ComplexMatrix baseMatrix = new ComplexMatrix.Builder().putCartesian(1, 0).putCartesian(1, 3)
				.putCartesian(100, -6).putCartesian(7, 5).build(2);
		ComplexMatrix testMatrix = baseMatrix.timesConstant(zero);

		assertEquals(trueMatrix, testMatrix);
	}

	@Test
	void testMatrixMultiplicationOne() {
		Complex one = Complex.ONE;

		ComplexMatrix trueMatrix = new ComplexMatrix.Builder().putCartesian(1, 0).putCartesian(1, 3)
				.putCartesian(100, -6).putCartesian(7, 5).build(2);
		ComplexMatrix testMatrix = trueMatrix.timesConstant(one);

		assertEquals(trueMatrix, testMatrix);
	}

	@Test
	void testMatrixMultiplicationI() {
		Complex i = Complex.I;
		Complex a = Complex.ofCartesian(1, 0);
		Complex b = Complex.ofCartesian(1, 3);
		Complex c = Complex.ofCartesian(100, -6);
		Complex d = Complex.ofCartesian(7, 5);

		ComplexMatrix trueMatrix = new ComplexMatrix.Builder().putComplex(a.multiply(i)).putComplex(b.multiply(i))
				.putComplex(c.multiply(i)).putComplex(d.multiply(i)).build(2);

		ComplexMatrix baseMatrix = new ComplexMatrix.Builder().putComplex(a).putComplex(b).putComplex(c).putComplex(d)
				.build(2);
		ComplexMatrix testMatrix = baseMatrix.timesConstant(i);

		assertEquals(trueMatrix, testMatrix);
	}

	@Test
	void testMatrixProductInvalid() {
		Complex zero = Complex.ZERO;

		// 2x3 times 2x3 is invalid
		ComplexMatrix matrix = new ComplexMatrix.Builder().putComplex(zero).putComplex(zero).putComplex(zero)
				.putComplex(zero).build(3);
		assertThrows(MatrixProductException.class, () -> matrix.product(matrix));
	}

	@Test
	void testMatrixProduct() throws MatrixProductException {
		ComplexMatrix trueMatrix = new ComplexMatrix.Builder().putCartesian(5, 2).putCartesian(12, -6).build(1);

		ComplexMatrix A = new ComplexMatrix.Builder().putCartesian(1, 0).putCartesian(0, 1).putCartesian(3, 0)
				.putCartesian(2, -1).putCartesian(0, 0).putCartesian(0, 4).build(3);
		ComplexMatrix B = new ComplexMatrix.Builder().putCartesian(6, 0).putCartesian(2, 1).putCartesian(0, 0).build(1);
		ComplexMatrix testMatrix = A.product(B);

		assertEquals(trueMatrix, testMatrix);
	}

	@Test
	void testKronecker() {
		ComplexMatrix trueMatrix = new ComplexMatrix.Builder()
				.putCartesian(1, 0).putCartesian(2, 0).putCartesian(0, 1).putCartesian(0, 2)
				.putCartesian(3, 0).putCartesian(4, 0).putCartesian(0, 3).putCartesian(0, 4)
				.putCartesian(0, 0).putCartesian(0, 0).putCartesian(-1, 0).putCartesian(-2, 0)
				.putCartesian(0, 0).putCartesian(0, 0).putCartesian(-3, 0).putCartesian(-4, 0)
				.putCartesian(0, -1).putCartesian(0, -2).putCartesian(2, 0).putCartesian(4, 0)
				.putCartesian(0, -3).putCartesian(0, -4).putCartesian(6, 0).putCartesian(8, 0)
				.build(4);
		
		ComplexMatrix A = new ComplexMatrix.Builder()
				.putCartesian(1, 0).putCartesian(0, 1)
				.putCartesian(0, 0).putCartesian(-1, 0)
				.putCartesian(0, -1).putCartesian(2, 0)
				.build(2);
		
		ComplexMatrix B = new ComplexMatrix.Builder()
				.putCartesian(1, 0).putCartesian(2, 0)
				.putCartesian(3, 0).putCartesian(4, 0)
				.build(2);
		
		ComplexMatrix testMatrix = A.kronecker(B);
		
		assertEquals(trueMatrix, testMatrix);
	}
	
	@Test
	void testConjugateTransposeLine() {
		ComplexMatrix trueMatrix = new ComplexMatrix.Builder().putCartesian(1, 0).putCartesian(2, 1).putCartesian(3, -1)
				.putCartesian(4, 0).build(1);
		ComplexMatrix matrix = new ComplexMatrix.Builder().putCartesian(1, 0).putCartesian(2, -1).putCartesian(3, 1)
				.putCartesian(4, 0).build(4);
		ComplexMatrix testMatrix = matrix.conjugateTranspose();
		
		assertEquals(trueMatrix, testMatrix);
	}

	@Test
	void testConjugateTransposeColumn() {
		ComplexMatrix trueMatrix = new ComplexMatrix.Builder().putCartesian(1, 0).putCartesian(2, 1).putCartesian(3, -1)
				.putCartesian(4, 0).build(4);
		ComplexMatrix matrix = new ComplexMatrix.Builder().putCartesian(1, 0).putCartesian(2, -1).putCartesian(3, 1)
				.putCartesian(4, 0).build(1);
		ComplexMatrix testMatrix = matrix.conjugateTranspose();
		
		assertEquals(trueMatrix, testMatrix);
	}
	
	@Test
	void testConjugateTranspose() {
		ComplexMatrix trueMatrix = new ComplexMatrix.Builder().putCartesian(1, 0).putCartesian(4, 0).putCartesian(2, 1)
				.putCartesian(5, 1).putCartesian(3, -1).putCartesian(6, -1).build(2);
		ComplexMatrix matrix = new ComplexMatrix.Builder().putCartesian(1, 0).putCartesian(2, -1).putCartesian(3, 1)
				.putCartesian(4, 0).putCartesian(5, -1).putCartesian(6, 1).build(3);
		ComplexMatrix testMatrix = matrix.conjugateTranspose();
		
		assertEquals(trueMatrix, testMatrix);
	}
	
}
