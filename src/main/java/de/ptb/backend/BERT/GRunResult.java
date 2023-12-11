package de.ptb.backend.BERT;

import java.util.Vector;

public class GRunResult {

    Double xRef;									// xRef value for a Run
    Double URef;									// URef value for a Run
    Vector<GEO> GEOResults = new Vector<GEO>();		// A vector of length N (pairs of Equivalence and Outlier flags)

    /**
     * This constructor method creates a 'blank' set of N Grubs Test Results (GEO) to be stored against the
     * Run Number. Default values are provided that will be updated during this process Run for the Grubs
     * Test.
     *
     * @param N     This is the total number of contributions to the DKCR. (The actual number of
     *              contributions 'received' so far maybe less than this number, and this will be
     *              indicated in the DIR collection object ResultPresentFlag. If a contribution is
     *              available ResultPresentFlag is TRUE else it is FALSE)
     */
    public GRunResult(int N)							// N is the number of contributions to the DKCR
    {
        // Initialise xRef and URef
        xRef = 0.0;
        URef = 0.0;
        // Add a set of N EO objects into the EOResults vector using 'null' values for attributes
        for(int i = 0; i < N; i++)
        {
            GEO objGEO = new GEO();
            // Initialise the object a
            objGEO.EquivalenceValue = 0.0;
            objGEO.EquivalenceValueRounded = 0.0;
            objGEO.GrubsValue = 0.0;
            objGEO.OutlierFlag = false;
            GEOResults.add(objGEO);
        }

    }

    /**
     * This method processes values of the Grubs Statistic for each contribution and determines
     * the xRef and URef reference values for this particular Run.
     *
     * @param iGMax         This is the integer pointer to the contribution with the Maximum value
     *                      of the Grubs Statistic for this Run.
     * @param DirInputs     This is the collection of input contributions core data
     */
    public void ProcessGRunResult(int iGMax, Vector<DIR> DirInputs)
    {
        // Process this set of GEOResults and determine xRef and URef

        // Initialise the sum
        Double G = 0.0;

        // Loop through the contributions to calculate G (ignore any outliers, flagged and cases where ResultPresentFlag = false)
        for(int i = 0; i < DirInputs.size(); i++)
        {
            // Get the contribution data
            DIR objDIR = DirInputs.get(i);

            // Process only if ResultPresent = true
            if(objDIR.ResultPresentFlag == true)
            {
                // Check that its not already an outlier
                GEO objGEO = this.GEOResults.get(i);
                if(objGEO.OutlierFlag == false)
                {
                    G = G + 1 / (objDIR.Ui * objDIR.Ui);
                }
            }
        }

        Double H = 1 / G;

        Double K = Math.sqrt(G);

        Double URef = 1 / K;

        Double xRef = 0.0;

        // Now calculate xRef
        // Loop through the contributions to calculate G (ignore any outliers, flagged and cases where ResultPresentFlag = false)
        for(int i = 0; i < DirInputs.size(); i++)
        {
            // Get the contribution data
            DIR objDIR = DirInputs.get(i);

            // Process only if ResultPresent = true
            if(objDIR.ResultPresentFlag == true)
            {
                // Check that its not already an outlier
                GEO objGEO = this.GEOResults.get(i);
                if(objGEO.OutlierFlag == false)
                {
                    xRef = xRef + H * (objDIR.xi / (objDIR.Ui * objDIR.Ui));
                }
            }
        }

        // Store the relevant xRef URef values in the data structure
        this.URef = URef;
        this.xRef = xRef;

        //// Do 2nd Stage DKCR Processing
        // Process the non iGmax items

        Double En95 = 0.0;

        for(int i = 0; i < DirInputs.size(); i++)
        {
            // Get the contribution data
            DIR objDIR = DirInputs.get(i);

            // Process only if ResultPresent = true
            if(objDIR.ResultPresentFlag == true)
            {
                // Check that its not already an outlier
                // Get the relevant EO object
                GEO objGEO = this.GEOResults.get(i);
                if(objGEO.OutlierFlag == false)
                {
                    // Calc. En95 for each of the contributions
                    En95 = Math.abs((objDIR.xi - this.xRef) / Math.sqrt(objDIR.Ui*objDIR.Ui - this.URef*this.URef));

                    // Get the relevant EO object
                    // GEO eo = this.GEOResults.get(i);

                    // Store value in data structure
                    //eo.EquivalenceValue = En95;
                    objGEO.EquivalenceValue = En95;

                    // Calculate the Rounded Value and store in EquivalenceValueRounded
                    // eo.RoundEquivalenceValue();
                    objGEO.RoundEquivalenceValue();

                    // Is it a (new) outlier?
                    //if(En95 > 1.0)
                    //{
                    //	// Yes its an new Outlier
                    //	eo.OutlierFlag = true;
                    //
                    //	// Increment the Outlier Count NOutlierFlags
                    //	NOutlierFlags++;
                    //}
                }
                else
                {
                    // This is an outlier
                    // Calc. En95 for each of the contributions
                    En95 = Math.abs((objDIR.xi - this.xRef) / Math.sqrt(objDIR.Ui*objDIR.Ui + this.URef*this.URef));
                    objGEO.EquivalenceValue = En95;
                    objGEO.RoundEquivalenceValue();

                }
            }
        }
        // Process the iGmax item
        int zzz = 1;
    }

    /**
     * Getter for the Grubs xRef value for this set of run results
     *
     * @return      The Grubs xRef value (Double)
     */
    public Double getxRef() {
        return xRef;
    }

    /**
     * Getter for the Grubs URef value for this set of run results
     *
     * @return      The URef value (Double)
     */
    public Double getURef() {
        return URef;
    }

    /**
     * Getter for the set of N GEO Results for this Run of the Grubs Test
     *
     * @return The GEOResults collection of GEO
     */
    public Vector<GEO> getGEOResults() {
        return GEOResults;
    }

    /**
     * This function returns a string of attributes for a GRunResult object in the form of a JSON string
     *
     * @return  JSON string containing the core attributes for a GRunResult object.
     */
    @Override
    public String toString() {
        return "GRunResult{" +
                "xRef=" + xRef +
                ", URef=" + URef +
                ", GEOResults=" + GEOResults +
                '}';
    }
}
