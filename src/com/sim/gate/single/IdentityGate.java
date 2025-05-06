package com.sim.gate.single;

import com.sim.number.QBit;

public class IdentityGate implements SingleGate {

	@Override
	public QBit apply(QBit qbit) {
		return qbit;
	}

}
