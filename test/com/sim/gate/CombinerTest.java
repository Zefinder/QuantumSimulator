package com.sim.gate;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.sim.gate.single.HadamardGate;
import com.sim.gate.single.IdentityGate;
import com.sim.number.Complex;
import com.sim.number.ComplexMatrix;

class CombinerTest {
	
	@Test
	void testCombineNothing() {
		assertNull(Combiner.combine());
	}
	
	void testCombineOne() {
		assertEquals(new IdentityGate().matrix(), Combiner.combine(new IdentityGate()));
	}

	@Test
	void testCombineTwoIdentity() {
		Complex zero = Complex.ZERO;
		Complex one = Complex.ONE;

		// Matrix filled with zero
		ComplexMatrix trueMatrix = new ComplexMatrix.Builder()
				.putComplex(one).putComplex(zero).putComplex(zero).putComplex(zero)
				.putComplex(zero).putComplex(one).putComplex(zero).putComplex(zero)
				.putComplex(zero).putComplex(zero).putComplex(one).putComplex(zero)
				.putComplex(zero).putComplex(zero).putComplex(zero).putComplex(one)
				.build(4);
		
		ComplexMatrix testMatrix = Combiner.combine(new IdentityGate(), new IdentityGate()).matrix();
		assertEquals(trueMatrix, testMatrix);
	}
	
	@Test 
	void testCombineThreeHadamard() {
		Complex one = Complex.ONE;
		Complex mone = Complex.ONE.negate();
		Complex invTwoSqrtTwo = Complex.ofCartesian((1 / Math.sqrt(2)) * (1 / Math.sqrt(2)) * (1 / Math.sqrt(2)), 0);
		
		ComplexMatrix trueMatrix = new ComplexMatrix.Builder()
				.putComplex(one).putComplex(one).putComplex(one).putComplex(one).putComplex(one).putComplex(one).putComplex(one).putComplex(one)
				.putComplex(one).putComplex(mone).putComplex(one).putComplex(mone).putComplex(one).putComplex(mone).putComplex(one).putComplex(mone)
				.putComplex(one).putComplex(one).putComplex(mone).putComplex(mone).putComplex(one).putComplex(one).putComplex(mone).putComplex(mone)
				.putComplex(one).putComplex(mone).putComplex(mone).putComplex(one).putComplex(one).putComplex(mone).putComplex(mone).putComplex(one)
				.putComplex(one).putComplex(one).putComplex(one).putComplex(one).putComplex(mone).putComplex(mone).putComplex(mone).putComplex(mone)
				.putComplex(one).putComplex(mone).putComplex(one).putComplex(mone).putComplex(mone).putComplex(one).putComplex(mone).putComplex(one)
				.putComplex(one).putComplex(one).putComplex(mone).putComplex(mone).putComplex(mone).putComplex(mone).putComplex(one).putComplex(one)
				.putComplex(one).putComplex(mone).putComplex(mone).putComplex(one).putComplex(mone).putComplex(one).putComplex(one).putComplex(mone)
				.build(8);
		
		Gate gate = Combiner.combine(new HadamardGate(), new HadamardGate(), new HadamardGate());
		assertEquals(trueMatrix, gate.matrix());
		assertEquals(invTwoSqrtTwo, gate.coefficient());
	}
}
