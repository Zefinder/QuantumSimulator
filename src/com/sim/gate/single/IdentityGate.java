package com.sim.gate.single;

import com.sim.number.QBit;

public class IdentityGate implements SingleGate {

	@Override
	public QBit apply(QBit qbit) {
		return qbit;
	}

	@Override
	public double coefficient() {
		return 1;
	}

	@Override
	public double[][] matrix() {
		return null;
	}

}
