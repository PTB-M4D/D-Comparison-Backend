package de.ptb.backend.BERT;

import java.util.Vector;

public class RunResult {

	Double xRef;									// xRef value for a Run
	Double URef;									// URef value for a Run
	Vector<EO> EOResults = new Vector<EO>();		// A vector of length N (pairs of Equivalence and Outlier flags)

	/**
	 * This constructor method creates a 'blank' set of N En Test Results (EO) to be stored against the
	 * Run Number. Default values are provided that will be updated during this process Run for the Grubs
	 * Test.
	 *
	 * @param N     This is the total number of contributions to the DKCR. (The actual number of
	 *              contributions 'received' so far maybe less than this number, and this will be
	 *              indicated in the DIR collection object ResultPresentFlag. If a contribution is
	 *              available ResultPresentFlag is TRUE else it is FALSE)
	 */
	public RunResult(int N)							// N is the number of contributions to the DKCR
	{
		// Initialise xRef and URef
		xRef = 0.0;
		URef = 0.0;
		// Add a set of N EO objects into the EOResults vector
		for(int i = 0; i < N; i++)
		{
			EO objEO = new EO();
			// Initialise the object a
			objEO.EquivalenceValue = 0.0;
			objEO.EquivalenceValueRounded = 0.0;
			objEO.OutlierFlag = false;
			EOResults.add(objEO);
		}

	}

	/**
	 * This function returns a string of attributes for a RunResult object in the form of a JSON string
	 *
	 * @return  JSON string containing the core attributes for a RunResult object.
	 */
	@Override
	public String toString() {
		return "RunResult{" +
				"xRef=" + xRef +
				", URef=" + URef +
				", EOResults=" + EOResults.toString() +
				'}';
	}

	/**
	 * Getter for the xRef value for this set of run results
	 *
	 * @return      The xRef value (Double)
	 */
	public Double getxRef() {
		return xRef;
	}

	/**
	 * Getter for the URef value for this set of run results
	 *
	 * @return      The URef value (Double)
	 */
	public Double getURef() {
		return URef;
	}

	/**
	 * Getter for the set of N EO Results for this Run
	 *
	 * @return The EOResults collection of EO
	 */
	public Vector<EO> getEOResults() {
		return EOResults;
	}
}
