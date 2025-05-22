package com.sim.gate.single;

import com.sim.gate.Gate;
import com.sim.number.QBit;

public interface SingleGate extends Gate {

	QBit apply(QBit qbits);
	
}
