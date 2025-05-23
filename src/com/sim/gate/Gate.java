package com.sim.gate;

import com.sim.number.Complex;
import com.sim.number.ComplexMatrix;

public interface Gate {

	Complex coefficient();

	ComplexMatrix matrix();

}
