/*
 * Copyright (c) 2022 - 2023 Physikalisch-Technische Bundesanstalt (PTB), all rights reserved.
 * This source code and software is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, version 3 of the License.
 * The software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this XSD.  If not, see http://www.gnu.org/licenses.
 * CONTACT: 		info@ptb.de
 * DEVELOPMENT:	https://d-si.ptb.de
 * AUTHORS:		Wafa El Jaoua, Tobias Hoffmann, Clifford Brown, Daniel Hutzschenreuter
 * LAST MODIFIED:	29.08.23, 14:57
 */

package de.ptb.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.ptb.backend.BERT.*;
import de.ptb.backend.model.DKCRErrorMessage;
import de.ptb.backend.model.DKCRRequestMessage;
import de.ptb.backend.model.DKCRResponseMessage;
import de.ptb.backend.model.Participant;
import de.ptb.backend.model.dsi.MeasurementResult;
import de.ptb.backend.model.dsi.SiConstant;
import de.ptb.backend.model.dsi.SiExpandedUnc;
import de.ptb.backend.model.dsi.SiReal;
import de.ptb.backend.model.formula.EEqualsMC2;
import de.ptb.backend.services.PidConstantWebReaderService;
import de.ptb.backend.services.PidDccFileSystemReaderService;
import de.ptb.backend.services.PidReportFileSystemWriterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/d-comparison")
@Data
public class BackendController {
    /**
     * This is a test function to check if the DKCR backend is running on the server.
     *
     * @return always the string "Hello World"
     */
    @GetMapping("/sayHello")
    public String sayHelloWorld() {
        return "Hello World!";
    }


    /**
     * This function reads the mass values from the DCC's specified in the participant list and calculates energy values, KC values and Grubstest values from them.
     * These are then returned in the form of a DCC.
     *
     * @param payload JsonNode which contains the PIDReport and Participantlist
     * @return ResponseEntity, which consists of the HTTPStatus and the message. The message can be an error message or the created DCCs as a base64 string.
     * @throws Exception In the future, more exceptions will be added for more specific cases.
     */
    @PostMapping("/evaluateComparison")
    public ResponseEntity evaluateDKCR(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "A Json Node contain all participants and the PIDDCC", content = @Content(schema = @Schema(example = "{\"keyComparisonData\":\n" +
            " {\n" +
            "  \"pidReport\" : \"NMIJ_All\",\n" +
            "  \"participantList\" : \n" +
            "        [ \n" +
            "            { \"participant\" : \n" +
            "                { \"name\" : \"BIPM\", \"pidDCC\" : \"https://d-si.ptb.de/api/d-dcc/dcc/CCM.M-K1-BIPM9502\" } \n" +
            "            },  \n" +
            "            { \"participant\" : \n" +
            "                { \"name\" : \"Physikalisch-Technische Bundesanstalt\", \"pidDCC\" : \"https://d-si.ptb.de/api/d-dcc/dcc/CCM.M-K1-PTB9608\" } \n" +
            "            },\n" +
            "            {\n" +
            "                \"participant\" :\n" +
            "                {\"name\": \"KRISS\", \"pidDCC\": \"https://d-si.ptb.de/api/d-dcc/dcc/CCM.M-K1-KRISS9703\"}\n" +
            "            },\n" +
            "            {\n" +
            "                \"participant\" :\n" +
            "                {\"name\": \"NPL\", \"pidDCC\": \"https://d-si.ptb.de/api/d-dcc/dcc/CCM.M-K1-NPL9507\"}\n" +
            "            }\n" +
            "        ] \n" +
            "    } \n" +
            "}\n"))) JsonNode payload) throws Exception {
        try{
            /*
             *This part of the function reads the passed data from the payload
             *and creates SiReal objects from the DCC files found from the pidDCC of the participantList.
             */
            JsonNode data = payload.get("keyComparisonData");
            String pidReport = data.get("pidReport").toString().substring(1, data.get("pidReport").toString().length() - 1);
            List<Participant> participantList = new ArrayList<>();
            for (JsonNode participant : data.get("participantList")) {
                participant = participant.get("participant");
                participantList.add(new Participant(participant.get("name").toString(), participant.get("pidDCC").toString()));
            }
            DKCRRequestMessage request = new DKCRRequestMessage(pidReport, participantList);
            PidDccFileSystemReaderService reader = new PidDccFileSystemReaderService();
            reader.setMessage(request);
            List<SiReal> SiReals = reader.readFiles();
            /*
            *In this part of the function, the dimension values in the SiReal objects are decreased by 1.
            * Then the speed of light is read from the d-constant backend and afterwards energy values are generated from the mass values by means of E=MC^2.
            */
            manipulateMassValues(SiReals, -1.0);
            PidConstantWebReaderService speedOfLightWebReader = new PidConstantWebReaderService();
            speedOfLightWebReader.setConstant("speedOfLightInVacuum2018");
            SiConstant speedOfLight = speedOfLightWebReader.getConstant();
            EEqualsMC2 equalsMC = new EEqualsMC2(speedOfLight, SiReals);
            List<SiReal> ergebnisse = equalsMC.calculate();
            /*
            *The generated energy values are now used to calculate the En and KC values.
            * Subsequently, the energy values are used to perform the Grubstest.
             */
            System.out.println(SiReals);
            fDKCR fdkcr = new fDKCR();
            RunfDKCR objRunfDKCR = new RunfDKCR();
            Vector<DIR> inputs = new Vector<>();
            for (SiReal ergebnis : ergebnisse) {
                DIR sirealAsDIR = new DIR(ergebnis.getValue(), ergebnis.getExpUnc().getUncertainty());
                inputs.add(sirealAsDIR);
            }
            objRunfDKCR.ReadData();
            objRunfDKCR.ReadDKRCContributions();
            fdkcr.setData(objRunfDKCR.getDKCRTitle(), objRunfDKCR.getDKCRID(), objRunfDKCR.getNTotalContributions(), objRunfDKCR.getPilotOrganisationID(), objRunfDKCR.getDKCRDimension(), objRunfDKCR.getDKCRUnit(), SiReals.size(), inputs, objRunfDKCR.getRunResults());
            objRunfDKCR.setNr(fdkcr.processDKCR());
            Vector<RunResult> Results = objRunfDKCR.getRunResults();
            SiReal kcVal = equalsMC.calculate(new SiReal(Results.get(0).getxRef(), "//joule", "", new SiExpandedUnc(0.0, 0, 0.0)));
            DKCR grubsTestDKCR = new DKCR(inputs);
            double mean = grubsTestDKCR.CalcMean();
            double stdDev = grubsTestDKCR.CalcStdDev(mean);
            Vector<GRunResult> gRunResults = grubsTestDKCR.ProcessGrubsDKCR(mean, stdDev);
            /*
            *Now after all values for the new DCC are determined, the values are transformed so that they fit in the structure of a MeasurementResult of a DCC xml file.
            * These MeasurementResults are then introduced into a DCC template. Finally, the function returns a finished XML file.
             */
            List<MeasurementResult> mResults = generateMResults(SiReals, Results, kcVal, ergebnisse, gRunResults);
            mResults.add(new MeasurementResult(SiReals.get(0).getMassDifference(), Results.get(0).getxRef(), kcVal, gRunResults.get(0).getxRef(), gRunResults.get(0).getURef()));
            PidReportFileSystemWriterService dccWriter = new PidReportFileSystemWriterService();
            dccWriter.setPid(pidReport);
            dccWriter.setParticipants(participantList);
            dccWriter.setMResults(mResults);
            DKCRResponseMessage response = new DKCRResponseMessage(pidReport, dccWriter.writeDataIntoDCC());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception e){
            /*
            * If something went wrong an error message is returned. Different error messages are planned for the future.
             */
            DKCRErrorMessage errorMessage = new DKCRErrorMessage(e.getMessage());
           return new ResponseEntity(errorMessage, HttpStatus.BAD_GATEWAY);
        }

    }

    /**
     * This function converts a DCC, which is a xml file, into a PDF file.
     *
     * @param payload JsonNode which contains the name and the content as a Base64 string
     * @return ResponseEntity which consists of the HTTPStatus and the message. The message is the DCC pdf file as a base64 string.
     */
    @PostMapping("/converterKCDB")
    public ResponseEntity<String> converterKCDB(@RequestBody JsonNode payload) {
        return new ResponseEntity<>("KCDB-Beispielausgabe", HttpStatus.OK);
    }

    /**
     * This function generates the content of the new DCC file from the previously determined or calculated values.
     *
     * @param mass         List<SiReal> which contains the mass values of the participant dcc files
     * @param enMassValues Vector<RunResult> which contains the en values
     * @param kcValue      SiReal
     * @param energy       List<SiReal> which contains the energy values calculated from the mass values
     * @param grubsValues  Vector<GRunResult> containing all the calculated Grubstest values
     * @return List<MeasurementResult> which contains every measurementresult entry in the new DCC
     */
    public List<MeasurementResult> generateMResults(List<SiReal> mass, Vector<RunResult> enMassValues, SiReal kcValue, List<SiReal> energy, Vector<GRunResult> grubsValues) {
        List<MeasurementResult> results = new ArrayList<>();
        RunResult runResult = enMassValues.get(0);
        for (int i = 0; i < mass.size(); i++) {
            results.add(new MeasurementResult(mass.get(i), runResult.getxRef(), kcValue, runResult.getEOResults().get(i).getEquivalenceValue(), energy.get(i), grubsValues.get(0).getGEOResults().get(i)));
        }
        return results;
    }

    /**
     * This function increase/decrease every mass value depending on the manipulator value
     *
     * @param siReals     List<SiReal> which contains the mass values of the participant dcc files
     * @param manipulator Double
     */
    private static void manipulateMassValues(List<SiReal> siReals, Double manipulator) {
        for (SiReal real : siReals) {
            real.manipulateValue(manipulator);
        }
    }
}