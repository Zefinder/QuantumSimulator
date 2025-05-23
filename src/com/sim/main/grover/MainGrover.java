package com.sim.main.grover;

import com.sim.Sequencer;
import com.sim.exception.GateNotGoodSizeException;
import com.sim.exception.MatrixProductException;
import com.sim.exception.QNumberNotGoodSizeException;
import com.sim.gate.Combiner;
import com.sim.gate.Gate;
import com.sim.gate.single.HadamardGate;
import com.sim.number.QNumber;

public class MainGrover {

	private static final int BIT_NUMBER = 6;
	private static final int VALUE = 42;

	public static void main(String[] args)
			throws GateNotGoodSizeException, QNumberNotGoodSizeException, MatrixProductException {
		// Grover's algorithm starts with state 0 at probability 1
		// Algorithm repeats for ceil(sqrt(N) * pi/4) with N the number of states
		int stateNumber = 1 << BIT_NUMBER;
		int groverRepetition = Math.ceilDiv((int) (Math.PI * Math.sqrt(stateNumber)), 4);

		Gate Hn = Combiner.combineN(new HadamardGate(), BIT_NUMBER);
		Gate groverOperator = new GroverOperator(BIT_NUMBER);

		// Create sequencer with Grover's algorithm oracle
		Sequencer seq = new Sequencer(BIT_NUMBER, new GroverOracle(VALUE));

		// First step: H^n
		seq.addStep(Hn);

		// Second step: Grover diffusion operator (H^n -- 2|0^n> <0^n| - I^n -- H^n)
		// Repeat it the right number of times
		Sequencer.SequencerStep step = new Sequencer.SequencerStep();
		step.addGate(Hn)
			.addGate(groverOperator)
			.addGate(Hn)
			.repeat(groverRepetition);
		seq.addStep(step);

		// Run the algorithm
		int stepNumber = 1;
		do {
			System.out.printf("Step %d:\n", stepNumber);
			
			QNumber qNumber = new QNumber(BIT_NUMBER);
			QNumber qresult = seq.run(qNumber);
			
			System.out.println(qresult);
			System.out.printf("\nMeasured value: %d\n", seq.measure());
			
			stepNumber++;
		} while (!seq.evaluate());
	}
}
