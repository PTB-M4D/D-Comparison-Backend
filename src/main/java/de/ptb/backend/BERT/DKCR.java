package de.ptb.backend.BERT;

import java.util.Vector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DKCR {


	String DKCRTitle;	// The title for the Digital Key Comparison Report (DKCR)
	String DKCRID;		// BIPM allocated ID for DKCR
	Integer NTotalContributions;			// The total number of contributions to the DKCR

	String PilotOrganisationID;			// ID of Pilot Organisation for DKCR

	String DKCRDimension;				// Measurement dimension
	String DKCRUnit;					// Unit of dimension

	Integer Nc = 0;			// The current (temporal) number of contributions (so far!)

	String DKRSetUpfilename = "DKCR.txt";	// File containing setup data for the one and only (atomic) DKCR
	String DKCRContributions = "DKCRContributions.txt";			// File containing current actual contributions

	String pathname = "TestFiles";	// The pathname of the folder containing DKCR data

	// Vector<DIR> RunResults = new Vector<DIR>(50, 10);
	Vector<DIR> DirInputs = new Vector<DIR>();				// A vector of N pairs of (xi, Ui)

	Vector<RunResult> RunResults = new Vector<RunResult>();	// XX A vector of Nr RunResult(s) (Ei, OutlierFlagi)
	Vector<GRunResult> GRunResults = new Vector<GRunResult>(); // XX ?? RunResult -> GRunResult

	GrubsStatisticsTable GSTable = new GrubsStatisticsTable();	// Table of Grubs Critical Value Statistics

	double DKCRMean;	// XX	Mean value determined for all data marked as Present
	double DKCRStdDev;	// XX	Standard Deviation determined for all data marked as Present

	public DKCR() throws IOException
	{
		// So far empty constructor

		// Read in the set up information for the DKCR from the folder "TestFiles"
		int result = ReadData();

	}
	public DKCR(Vector<DIR> dirInputs) throws IOException {
		this.DirInputs = dirInputs;
		this.NTotalContributions = this.DirInputs.size();
	}

	public int ReadData() throws IOException
	{
		// Read data from DKCR.txt file which contains the set up information for the DKCR

		try
		{
			BufferedReader in = new BufferedReader(new FileReader(pathname + "//" + DKRSetUpfilename));
			String line;
			int ln = 0;

			// The DKCR.txt file will contain the total number of contributions NTotalContributions

			while  (( line = in.readLine()) != null )
			{

				// Do something with the line
				System.out.println(line);		// Debug shows file being read
				String[] current_Record = line.split(",");

				if(ln == 0)
				{
					// Read info into the one and only (atomic) DKCR attributes
					this.DKCRTitle = current_Record[0];
					this.DKCRID = current_Record[1];
					this.NTotalContributions = Integer.parseInt(current_Record[2]);
					this.PilotOrganisationID = current_Record[3];
					this.DKCRDimension = current_Record[4];
					this.DKCRUnit = current_Record[5];
				}
				else
				{
					// Read info into a DIR new object
					DIR a = new DIR();
					a.DKCRCONID = current_Record[0];
					a.NMIID = current_Record[1];
					a.ResultPresentFlag = false;			// No data has been included yet!
					a.xi = 0.0;								// So force to 0.0
					a.Ui = 0.0;								// So force to 0.0
					this.DirInputs.add(a);
				}

				ln++;

			}
			in.close();
		}

		catch( IOException ioException )
		{
			System.out.println("Exception: "+ioException);
		}

		return 0;
	}

	public int ReadDKRCContributions() throws IOException
	{

		// Read data from DKCRContributions.txt file which contains the contribution data so far
		// Note the number of entries will be less than NTotalContributions if not all contributions
		// have been received.

		try
		{
			BufferedReader in = new BufferedReader(new FileReader(pathname + "//" + DKCRContributions));
			String line;
			int ln = 0;

			while  (( line = in.readLine()) != null )
			{

				// Do something with the line
				System.out.println(line);		// Debug shows file being read
				String[] current_Record = line.split(",");


				// Get Data from line
				String ID = current_Record[0];
				Double xi = Double.parseDouble(current_Record[1]);
				Double Ui = Double.parseDouble(current_Record[2]);

				AddContributions(ID, xi, Ui);

				ln++;

				// Increment the number of contributions counter
				Nc++;

			}
			in.close();
		}

		catch( IOException ioException )
		{
			System.out.println("Exception: "+ioException);
		}


		return 0;

	}

	public int AddContributions(String sID, Double dxi, Double dUi)
	{
		// The size of DirInputs will always be NTotalContributions
		// but the number of contributions so far may not equal NTotalContributions
		// Therefore count number of contributions and set this to Nc


		// Search through DirInputs for contribution ID and add results
		for(int i = 0; i < DirInputs.size(); i++)
		{
			DIR o = DirInputs.get(i);
			if(o.DKCRCONID.equals(sID))
			{
				// Add the results to this object
				o.xi = dxi;
				o.Ui = dUi;
				// Set the data present flag from false to true
				o.ResultPresentFlag = true;

			}

		}

		return 0;
	}

	public int ReadGrubsStatisticTable()
	{
		return 1;
	}

	public int CalcGrubsTestStatistic()		// XX
	{								// XX
		// Calculate the Grubs Test Statistic for contributions that are present



		return 1;					// XX
	}								// XX

	public int ProcessGrubsTestStatistic()	// XX
	{										// XX
		// Process the Grubs Test Statistic to find a possible, singular Outlier
		// This process looks for the largest value of the Grubs Statistic, and
		// Compares this to the


		return 1;							// XX
	}										// XX

	public double CalcMean()		// XX
	{								// XX

		double sum = 0.0;
		double mean = 0.0;
		int count = 0;

		// Calculate the Mean Value of the contributions held in DIRInputs

		// Loop through DIRInputs
		for(int i = 0; i < DirInputs.size(); i++)
		{
			// Get the ith object
			DIR o = DirInputs.get(i);
			// If data is present
			if(o.ResultPresentFlag == true)
			{
				// Add contribution to sum
				sum = sum + o.xi;
				// Increment count of contributions
				count++;
			}
		}

		mean = sum / count;

		return mean;					// XX
	}								// XX

	public double CalcStdDev(double mean)		// XX
	{								// XX

		double sum2 = 0.0;
		double stddev = 0.0;
		int count = 0;

		// Calculate the Standard Deviation of the contributions held in DIRInputs

		// Loop through DIRInputs
		for(int i = 0; i < DirInputs.size(); i++)
		{
			// Get the ith object
			DIR o = DirInputs.get(i);
			// If data is present
			if(o.ResultPresentFlag == true)
			{
				// Add contribution to sum
				sum2 = sum2 + Math.pow( (o.xi - mean), 2);
				// Increment count of contributions
				count++;
			}
		}

		stddev = Math.sqrt(sum2 / (count - 1));		//XX (Assumption about 1 degree of freedom)

		return stddev;				// XX
	}								// XX


	public Vector<GRunResult> ProcessGrubsDKCR(double mean, double stddev)
	{
		int NOutlierFlags = 0;		// The number of outlier flags from current Run
		int OldNOutlierFlags = 0;	// The number of outlier flags from the previous Run
		int NRun = 1;				// The number of the current Run
		Double dxi = 0.0;
		Double dUi = 0.0;			//

		// Note Only data with a ResultPresentFlag = true will be processed. All data for false cases will be ignored.
		// However the data structures used will assume that all data is present.

		// While this is the first run, or a new Outlier Flag has been added in the previous run process again.
		while(NRun == 1 || (NOutlierFlags != OldNOutlierFlags))
		{
			// Set up a new set of empty RunResults
			GRunResult a = new GRunResult(NTotalContributions);		// NTotalContributions is the total number of contributions
			// Add this to the RunResults
			GRunResults.add(a);

			// Set the OldNOutlierFlags
			OldNOutlierFlags = NOutlierFlags;

			// if this is not the first run, copy the outlier flags from the previous run into the current run's data structure.
			if(NRun != 1)
			{
				// Zero Number of outlierflag counts
				OldNOutlierFlags = 0;
				NOutlierFlags = 0;

				// Copy Outlier information from previous run
				GRunResult q = GRunResults.get(NRun - 2);  // SHould this be NRun - 2  ???
				// TODO copy the outlier information from Previous run into this new one
				for(int i = 0; i < DirInputs.size(); i++)
				{
					GEO eot = a.GEOResults.get(i);
					GEO eop = q.GEOResults.get(i);

					// Is the previous run for this contribution already an outlier?
					if(eop.OutlierFlag == true)
					{
						// Increment outlierflag counts
						OldNOutlierFlags++;
						NOutlierFlags++;
						// Set the outlierflag in this run to still be an outlier
						eot.OutlierFlag = true;
					}

				}

				// Determine NOutlierFlags and OldNOutlierFlags (the same value at the start of Run)

			}
			else
			{
				// Yes this is the first Run so need to reset the NOutlierFlags and OldNOutlierFlags (if not already!)
				NOutlierFlags = 0;
				OldNOutlierFlags = 0;
			}

			//// Do 1st Stage Grubs DKCR Processing
			// Determine the Grubs Value for all contributions (with a PresentFlag = true)


			// Loop through the contributions to calculate G (ignore any outliers from previous run, flagged
			// and cases where ResultPresentFlag = false).
			// Simultaneously note the largest value for G and for which contribution

			int iGMax = -1;
			double GMax = 0.0;
			double G = 0.0 ;			// The calculated value of G for a contribution (Note:- Positive definite)
			int Nopc = 0;				// This is the number of contributions processed
			for(int i = 0; i < DirInputs.size(); i++)
			{
				// Get the contribution data
				DIR o = DirInputs.get(i);

				// Process only if ResultPresent = true
				if(o.ResultPresentFlag == true)
				{
					// Check that its not already an outlier. If it is, no need to process further.
					GEO p = a.GEOResults.get(i);
					if(p.OutlierFlag == false)
					{
						// Increment the number of contributions processed
						Nopc++;

						// Calculate the Grubs Value for this contribution
						G = Math.abs(o.xi - mean) / stddev;
						p.GrubsValue = G;

						// Check to see if this G is the largest
						if(G > GMax)
						{
							// Store these values
							iGMax = i;
							GMax = G;
						}

					}
				}
			}

			// Process the iGMax and GMax information to see if there is a new (single) outlier
			if(iGMax != -1)
			{
				// Get the maximum data GEOResult object
				GEO p = a.GEOResults.get(iGMax);

				// Get the Grubs Critical Value for the number of contributions processed (Use Nopc from previous loop)
				GrubsStatistic gs = GSTable.gst.get(Nopc - 3);	//Table starts at 3

				// Check to see if the G Value exceeds the Grubs Critical Value
				if(Double.compare(GMax, gs.GrubsCritical) > 0)
				{
					//// Yes the GMax is greater than Grubs Critical Value

					// Set the outlier flag
					p.OutlierFlag = true;

					// Increment the count of outliers
					NOutlierFlags++;

					// Process the data to determine the Equivalence values for this run
					a.ProcessGRunResult(iGMax, DirInputs);

				}
				else
				{
					// No, the maximum is not greater than the Grubs Critical Value
					// Processing will stop after this run

					// Do we need to keep the current object 'a' or should this now get deteled?
					// For the moment leave it in place.
					a.ProcessGRunResult(iGMax, DirInputs);
				}




			}
			else
			{
				// There should always be a maximum value so iGMax != 0 always
				// If iGMax == 0 something has gone wrong!
				System.out.println("****ERROR***** in ProcessGrubsDKCR. No Maximum found.");

			}




			int zz = 1;

			// Increment the run number
			NRun++;

		} // End of Main processing Loop

		return this.GRunResults;
	}

	public int ProcessDKCR()
	{
		int NOutlierFlags = 0;		// The number of outlier flags from current Run
		int OldNOutlierFlags = 0;	// The number of outlier flags from the previous Run
		int NRun = 1;				// The number of the current Run
		Double dxi = 0.0;
		Double dUi = 0.0;			//
		Double soousq = 0.0;		// Sum of 1 / dUi**2


		// Note Only data with a ResultPresentFlag = true will be processed. All data for false cases will be ignored.
		// However the data structures used will assume that all data is present.

		// While this is the first run, or a new Outlier Flag has been added in the previous run process again.
		while(NRun == 1 || (NOutlierFlags != OldNOutlierFlags))
		{

			// Set up Outflier flag count for the next Run
			//OldNOutlierFlags = NOutlierFlags;		// NOutliferFlags maybe incremented in this Run

			// Set up a new set of empty RunResults
			RunResult a = new RunResult(NTotalContributions);		// NTotalContributions is the total number of contributions
			// Add this to the RunResults
			RunResults.add(a);

			// if this is not the first run, copy the outlier flags from the previous run into the current run's data structure.
			if(NRun != 1)
			{
				// Zero Number of outlierflag counts
				OldNOutlierFlags = 0;
				NOutlierFlags = 0;

				// Copy Outlier information from previous run
				RunResult q = RunResults.get(NRun - 2);  // SHould this be NRun - 2  ???
				// TODO copy the outlier information from Previous run into this new one
				for(int i = 0; i < DirInputs.size(); i++)
				{
					EO eot = a.EOResults.get(i);
					EO eop = q.EOResults.get(i);

					// Is the previous run for this contribution already an outlier?
					if(eop.OutlierFlag == true)
					{
						// Increment outlierflag counts
						OldNOutlierFlags++;
						NOutlierFlags++;
						// Set the outlierflag in this run to still be an outlier
						eot.OutlierFlag = true;
					}

				}

				// Determine NOutlierFlags and OldNOutlierFlags (the same value at the start of Run)

			}
			else
			{
				// Yes this is the first Run so need to reset the NOutlierFlags and OldNOutlierFlags (if not already!)
				NOutlierFlags = 0;
				OldNOutlierFlags = 0;
			}

			//// Do 1st Stage DKCR Processing

			// Initialise the sum
			Double G = 0.0;

			// Loop through the contributions to calculate G (ignore any outliers, flagged and cases where ResultPresentFlag = false)
			for(int i = 0; i < DirInputs.size(); i++)
			{
				// Get the contribution data
				DIR o = DirInputs.get(i);

				// Process only if ResultPresent = true
				if(o.ResultPresentFlag == true)
				{
					// Check that its not already an outlier
					EO p = a.EOResults.get(i);
					if(p.OutlierFlag == false)
					{
						G = G + 1 / (o.Ui * o.Ui);
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
				DIR o = DirInputs.get(i);

				// Process only if ResultPresent = true
				if(o.ResultPresentFlag == true)
				{
					// Check that its not already an outlier
					EO p = a.EOResults.get(i);
					if(p.OutlierFlag == false)
					{
						xRef = xRef + H * (o.xi / (o.Ui * o.Ui));
					}
				}
			}

			// Store the relevant xRef URef values in the data structure
			a.URef = URef;
			a.xRef = xRef;

			//// Do 2nd Stage DKCR Processing

			Double En95 = 0.0;

			for(int i = 0; i < DirInputs.size(); i++)
			{
				// Get the contribution data
				DIR o = DirInputs.get(i);

				// Process only if ResultPresent = true
				if(o.ResultPresentFlag == true)
				{
					// Check that its not already an outlier
					EO p = a.EOResults.get(i);
					if(p.OutlierFlag == false)
					{
						// Calc. En95 for each of the contributions
						En95 = Math.abs((o.xi - a.xRef) / Math.sqrt(o.Ui*o.Ui - a.URef*a.URef));

						// Get the relevant EO object
						// EO eo = a.EOResults.get(i);

						// Store value in data structure
						// eo.EquivalenceValue = En95;
						p.EquivalenceValue = En95;

						// Calculate the Rounded Value and store in EquivalenceValueRounded
						// eo.RoundEquivalenceValue();
						p.RoundEquivalenceValue();

						// Is it a (new) outlier?
						if(En95 > 1.0)
						{
							// Yes its an new Outlier
							// eo.OutlierFlag = true;
							p.OutlierFlag = true;

							// Increment the Outlier Count NOutlierFlags
							NOutlierFlags++;
						}
					}
				}
			}

			// Increment the Run Number
			NRun++;
		}

		return 0;
	}

	public void PresentResults()
	{

		System.out.println(" ");
		System.out.println("Start of Standard En");

		RunResults.forEach(RunResult->
		{
			System.out.println(" ");
			System.out.println("URef="+(RunResult.URef).toString()+" "+"xRef="+(RunResult.xRef).toString());
			RunResult.EOResults.forEach(EO->
			{
				System.out.println(EO.EquivalenceValue+ " ("+EO.EquivalenceValueRounded + ") " + EO.OutlierFlag);
			});
		});
		System.out.println(" ");
		System.out.println("End of Standard En");
	}

	public void PresentGResults()
	{

		System.out.println(" ");
		System.out.println("Start of Grubs En");

		GRunResults.forEach(GRunResult->
		{
			System.out.println(" ");
			System.out.println("URef="+(GRunResult.URef).toString()+" "+"xRef="+(GRunResult.xRef).toString());
			GRunResult.GEOResults.forEach(EO->
			{
				System.out.println(EO.EquivalenceValue+ " ("+EO.EquivalenceValueRounded + ") " + EO.OutlierFlag);
			});
		});
		System.out.println(" ");
		System.out.println("End of Grubs En");
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		// Create the NMIS object that will contain a list of BIPM registered NMIs
		NMIS objNMIS = new NMIS();

		// Create a new top level (atomic) DKCR object and read in setup data for this DKCR
		DKCR objDKCR = new DKCR();

		// Read in actual DKCR contributions from DKCRContribtions.txt and update the DirInputs Vector
		objDKCR.ReadDKRCContributions();

		// Calculate the mean of the contributions (uses Data Present flag)
		objDKCR.DKCRMean = objDKCR.CalcMean();

		// Calculate the Standard Deviation of the contributions given the mean (uses Data Present flag)
		objDKCR.DKCRStdDev = objDKCR.CalcStdDev(objDKCR.DKCRMean);

		// Perform the Processing of the DKRC
		objDKCR.ProcessDKCR();

		// Present Results
		objDKCR.PresentResults();	// Basic output to console window

		// Process Contributions using Grubs Analysis
		objDKCR.ProcessGrubsDKCR(objDKCR.DKCRMean, objDKCR.DKCRStdDev);

		objDKCR.PresentGResults();

		System.out.println("DKCR Completed!");


		int x = 1;


	}

}

