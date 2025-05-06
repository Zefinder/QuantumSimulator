package com.sim.gate.single;

import com.sim.number.Complex;
import com.sim.number.QBit;

public class PhaseShiftGate implements SingleGate {

	private final Complex phaseShift;

	public PhaseShiftGate(double angle) {
		this.phaseShift = Complex.ofCis(angle);
	}

	@Override
	public QBit apply(QBit qbit) {
		Complex oldZeroState = qbit.getZeroStateValue();
		Complex oldOneState = qbit.getOneStateValue();

		QBit newQbit = new QBit(oldZeroState, oldOneState.multiply(phaseShift));

		return newQbit;
	}

}
