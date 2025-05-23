package com.sim;

import com.sim.number.QNumber;

public interface Oracle {

	boolean predict(int value);

	QNumber quantumPredict(QNumber qvalue);

}
