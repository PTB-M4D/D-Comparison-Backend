package de.ptb.backend.BERT;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class GrubsStatisticsTable {

    // The table of Grubs Statistics

    Vector<GrubsStatistic> gst = new Vector<GrubsStatistic>();

    /**
     * This method generates the Grubs Statistic Table of data
     *
     * @throws IOException on file read error
     */
    public GrubsStatisticsTable() throws IOException
    {
        // Empty Constructor
        // int result = ReadGrubsTableData();

        double[] gstd = {1.154, 1.481, 1.715, 1.887, 2.020, 2.127, 2.215, 2.290,
                2.355, 2.412, 2.462, 2.507, 2.548, 2.586, 2.620, 2.652, 2.681, 2.708,
                2.734, 2.758, 2.780, 2.802, 2.822, 2.841, 2.859, 2.876, 2.893, 2.908,
                2.924, 2.938, 2.952, 2.965, 2.978, 2.991, 3.003, 3.014, 3.025, 3.036,
                3.047, 3.057, 3.067, 3.076, 3.085, 3.094, 3.103, 3.112, 3.120, 3.128,
                3.136, 3.144, 3.151, 3.159, 3.166, 3.173, 3.180, 3.187, 3.193, 3.200,
                3.206, 3.212, 3.218, 3.224, 3.230, 3.236, 3.241, 3.247, 3.252, 3.258,
                3.263, 3.268, 3.273, 3.278, 3.283, 3.288, 3.292, 3.297, 3.302, 3.306,
                3.311, 3.315, 3.319, 3.232, 3.328, 3.332, 3.336, 3.340, 3.344, 3.348,
                3.352, 3.355, 3.359, 3.363, 3.366, 3.370, 3.374, 3.377, 3.381, 3.384 };


        for(int i = 0; i < gstd.length; i++)
        {
            GrubsStatistic objGrubsStatistic = new GrubsStatistic();
            objGrubsStatistic.GrubsN = i + 3;
            objGrubsStatistic.GrubsCritical = gstd[i];

            gst.add(objGrubsStatistic);
        }

    }

    /**
     * This method reads in the Grubs Statistics table from a file on the local drive
     *
     * @return
     * @throws IOException on file read error
     */
    public int ReadGrubsTableData() throws IOException
    {
        // Read the Grubs Statistic Table data from standard filename "GrubsTable.txt" into Vector "gst"

        try
        {
            BufferedReader in = new BufferedReader(new FileReader("TestFiles//GrubsTable.txt"));
            String line;

            while  (( line = in.readLine()) != null )
            {
                // Do something with the line
                System.out.println(line);		// Debug shows file being read

                String[] current_Record = line.split(",");

                // Create a new GrubsStatistic
                GrubsStatistic objGrubsStatistic = new GrubsStatistic();
                objGrubsStatistic.GrubsN = Integer.parseInt(current_Record[0]);
                objGrubsStatistic.GrubsCritical = Double.parseDouble(current_Record[1]);


                // Add the new DKCRContributor to the nmis Vector
                gst.add(objGrubsStatistic);

            }

            in.close();
        }

        catch( IOException ioException )
        {
            System.out.println("Exception: "+ioException);
        }


        return 1;
    }


}