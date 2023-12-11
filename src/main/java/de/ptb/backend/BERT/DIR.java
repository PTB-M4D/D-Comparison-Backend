package de.ptb.backend.BERT;

import java.util.*;		// For Vector collection

public class DIR {
	String DKCRCONID;							// DKCR Contribution ID
	String NMIID;								// The ID of the Metrology Institute
	Double xi;									// The xi value for an individual contribution
	Double Ui;									// The Ui value for an individual contribution
	Boolean ResultPresentFlag;					// Indicates that a valid Result is Present
	//   (Note:- this is not the same as the Outlier Flag)

	String filename;

	// Vector<RunResult> RunResults = new Vector<RunResult>();	// A vector of Nr RunResult(s)
	public DIR() {
	}

	/**
	 * This is the constructor for the DIR (DKCR Input Report) object
	 *
	 * @param xi   	This is the core measured input value in the DIR (Double)
	 * @param ui	THis is the expanded uncertainty in the DIR associated with the measurement (Double)
	 */
	public DIR(Double xi, Double ui) {
		this.xi = xi;
		this.Ui = ui;
		this.ResultPresentFlag=true;	// Note the assumption that a DIR contribution is present in the
										// input information. It will be reset to false if the contribution
										// is not present in initial processing of the input data.
	}

	/**
	 * This function returns a string of attributes for a DIR object in the form of a JSON string
	 *
	 * @return	JSON string containing the core attributes for a DIR object.
	 */
	@Override
	public String toString() {
		return "DIR{" +
				"DKCRCONID='" + DKCRCONID + '\'' +
				", NMIID='" + NMIID + '\'' +
				", xi=" + xi +
				", Ui=" + Ui +
				", ResultPresentFlag=" + ResultPresentFlag +
				", filename='" + filename + '\'' +
				'}';
	}
}

