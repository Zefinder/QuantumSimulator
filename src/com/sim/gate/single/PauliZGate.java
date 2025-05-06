package com.sim.gate.single;

import com.sim.number.Complex;
import com.sim.number.QBit;

public class PauliZGate implements SingleGate {

	@Override
	public QBit apply(QBit qbit) {
		Complex oldZeroState = qbit.getZeroStateValue();
		Complex oldOneState = qbit.getOneStateValue();
		QBit newQbit = new QBit(oldZeroState, oldOneState.negate());
		
		return newQbit;
	}

}
