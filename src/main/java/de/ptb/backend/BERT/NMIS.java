package de.ptb.backend.BERT;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;


public class NMIS
{
	Vector<DKCRContributor> nmis = new Vector<DKCRContributor>();		// A List of NMIs (Worldwide) who could be
														// a contributor to a DKCR process

	/**
	 * Constructor method for the singular list of NMIs object
	 *
	 * @throws IOException		// Exception thrown on file read error
	 */
	public NMIS() throws IOException
	{
		// Empty Constructor
		int result = ReadData();
	}

	/**
	 * Method to read a list of NMIs from a data file
	 *
	 * @return
	 * @throws IOException 		// Exception thrown on file read error
	 */
	public int ReadData() throws IOException
	{
		// Read the NMI data from standard filename "NMIS.txt" into Vector "NMIS"
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("TestFiles//NMIS.txt"));
			String line;

			while  (( line = in.readLine()) != null )
			{
				// Do something with the line
				System.out.println(line);		// Debug shows file being read

				String[] current_Record = line.split(",");

				// Create a new DKCRContributor
				DKCRContributor objDKCRContributor = new DKCRContributor();
				objDKCRContributor.MetrologyID = current_Record[0];
				objDKCRContributor.Name = current_Record[1];
				objDKCRContributor.Country = current_Record[2];

				// Add the new DKCRContributor to the nmis Vector
				nmis.add(objDKCRContributor);

			}

			in.close();
		}
		catch( IOException ioException )
		{
			System.out.println("Exception: "+ioException);
		}
		return 0;
	}
}