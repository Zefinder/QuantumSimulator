package com.sim;

import com.sim.gate.single.HadamardGate;
import com.sim.gate.single.IdentityGate;
import com.sim.gate.single.PauliXGate;
import com.sim.gate.single.PauliYGate;
import com.sim.gate.single.PauliZGate;
import com.sim.gate.single.PhaseShiftGate;
import com.sim.number.Complex;
import com.sim.number.QBit;

public class Main {

	public static void main(String[] args) {
//		int n = 10;
//		for (int i = 0; i < n; i++) {
//			System.out.printf("Test n°%d\n", i + 1);
//			QBit qbit = new QBit(Complex.ofCartesian(0.5, 0), Complex.ofCartesian(0, Math.sqrt(3) / 2));
//
//			System.out.println(qbit);
//			int value = qbit.check();
//			System.out.println("Value: " + value);
//			System.out.println(qbit);
//			System.out.println();
//		}

//		QBit qbit = new QBit(Complex.ofCartesian(1, 0), Complex.ofCartesian(0, 0));
		QBit qbit = new QBit(Complex.ofCartesian(1 / Math.sqrt(2), 0), Complex.ofCartesian(1 / Math.sqrt(2), 0));
		System.out.println(qbit);
		System.out.println(new IdentityGate().apply(qbit));
		System.out.println(new HadamardGate().apply(qbit));
		System.out.println(new PauliXGate().apply(qbit));
		System.out.println(new PauliYGate().apply(qbit));
		System.out.println(new PauliZGate().apply(qbit));
		System.out.println(new PhaseShiftGate(Math.PI/4).apply(qbit));
	}

}
