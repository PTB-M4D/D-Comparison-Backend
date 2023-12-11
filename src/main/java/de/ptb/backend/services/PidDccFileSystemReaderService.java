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
 * LAST MODIFIED:	29.08.23, 13:26
 */

package de.ptb.backend.services;
import de.ptb.backend.model.DKCRRequestMessage;
import de.ptb.backend.model.Participant;
import de.ptb.backend.model.dsi.SiExpandedUnc;
import de.ptb.backend.model.dsi.SiReal;
import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Data
@Service
public class PidDccFileSystemReaderService implements I_PidDccFileSystemReader {
    String path;
    DKCRRequestMessage message;

    /**
     * This function sets the message which is used to read out all the requested DCC files.
     * @param message DKCRRequestMessage which contains all the information from request of the frontend
     */
    @Override
    public void setMessage(DKCRRequestMessage message){
        this.message = message;
        if(System.getProperty("os.name").contains("Windows")){
            this.path = "src\\main\\resources\\DCCFiles";
        }else{
            this.path = "DCCFiles";
        }
    }

    /**
     * This function iterates through the participantList of the Requestmessage and the dcc files on the system
     * and creates SiReals for every matching name containing the values of the respective dcc file.
     * @return List<SiReal> which contains the mass values of the participant dcc files
     * @throws ParserConfigurationException Throws exception if the DocumentBuilderFactory is not set up properly.
     */
    @Override
    public List<SiReal> readFiles() throws ParserConfigurationException, JSONException {
//        String urlListPid = "http://localhost:8085/api/d-dcc/dccPidList";
        String urlListPid = "https://d-si.ptb.de/api/d-dcc/dccPidList";
        RestTemplate restTemplate = new RestTemplate();
        String dccPidList = restTemplate.getForObject(urlListPid, String.class, 200);
        JSONArray PidListArray = new JSONArray(dccPidList);
        String[] pidList = new String[PidListArray.length()];
        for (int i = 0; i < PidListArray.length(); i++) {
            pidList[i] = PidListArray.getString(i);
        }
        List<SiReal> siReals = new ArrayList<>();
        if (!urlListPid.isEmpty()) {
            for (Participant participant : this.message.getParticipantList()) {
                for (String pid : pidList) {
                    System.out.println("participant:" + participant.getDccPid().substring(1, participant.getDccPid().length() - 1));
                    if (pid.equals(participant.getDccPid().substring(1, participant.getDccPid().length() - 1)) ) {
                        try {
                            String result = restTemplate.getForObject(participant.getDccPid().substring(1, participant.getDccPid().length() - 1), String.class, 200);
                            byte[] byteBase64 = Base64.getDecoder().decode(result);
                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder;
                            String decodedXml = new String(byteBase64);
                            builder = factory.newDocumentBuilder();
                            Document document = builder.parse(new InputSource(new StringReader(decodedXml)));
//                          System.out.println("decoded" + decodedXml);
                            XPath xPath = XPathFactory.newInstance().newXPath();
                            String expression = "/digitalCalibrationCertificate/measurementResults/measurementResult/results/result[@refType=\"mass_mass\"]/data/quantity[@refType=\"basic_measuredValue\"]/real";
                            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
                            for (int i = 0; i < nodeList.getLength(); i++) {
                                Node nNode = nodeList.item(i);
                                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement = (Element) nNode;
                                    Double value = Double.valueOf(eElement.getElementsByTagName("si:value").item(0).getTextContent());
                                    String unit = eElement.getElementsByTagName("si:unit").item(0).getTextContent();
                                    String dateTime = "nicht existent";//eElement.getElementsByTagName("si:dateTime").item(0).getTextContent();
                                    Double uncertainty = Double.valueOf(eElement.getElementsByTagName("si:uncertainty").item(0).getTextContent());
                                    int coverageFactor = Integer.parseInt(eElement.getElementsByTagName("si:coverageFactor").item(0).getTextContent());
                                    Double coverageProbability = Double.valueOf(eElement.getElementsByTagName("si:coverageProbability").item(0).getTextContent());
                                    SiExpandedUnc expUnc = new SiExpandedUnc(uncertainty, coverageFactor, coverageProbability);
                                    siReals.add(new SiReal(value, unit, dateTime, expUnc));
                                }
                            }
                        } catch (XPathExpressionException e) {
                            throw new RuntimeException(e);
                        } catch (IOException | SAXException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return siReals;
    }
}
