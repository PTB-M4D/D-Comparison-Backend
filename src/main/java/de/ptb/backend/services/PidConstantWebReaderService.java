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
 * LAST MODIFIED:	29.08.23, 12:18
 */

package de.ptb.backend.services;

import de.ptb.backend.model.dsi.SiConstant;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

@Service
public class PidConstantWebReaderService implements I_PidConstantWebReader{
    final String dConstantUrl = "https://d-si.ptb.de/api/d-constant/";//"http://localhost:8082/api/d-constant/";
    String constant;

    /**
     * This function sets the constant which is later requested from the constant backend.
     * @param constant String
     */
    @Override
    public void setConstant(String constant) {
        this.constant = constant;
    }

    /**
     * This function establishes a connection with this.dConstantUrl and this.constant and reads the necessary contents from the xml file received.
     * @return SiConstant containing the wanted constant
     * @throws XPathExpressionException
     */
    @Override
    public SiConstant getConstant() throws XPathExpressionException {
        final String url = this.dConstantUrl+this.constant;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class, 200);
        SiConstant speedOfLight;
        Document doc = convertStringToXMLDocument(result);
        doc.getDocumentElement().normalize();
        XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression = "/speedOfLightInVacuum2018/constant";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String label = eElement.getElementsByTagName("si:label").item(0).getTextContent();
                String quantityType = eElement.getElementsByTagName("si:quantityTypeQUDT").item(0).getTextContent();
                Double value = Double.valueOf(eElement.getElementsByTagName("si:value").item(0).getTextContent());
                String unit = eElement.getElementsByTagName("si:unit").item(0).getTextContent();
                String dateTime = eElement.getElementsByTagName("si:dateTime").item(0).getTextContent();
                double uncertainty = Double.valueOf(eElement.getElementsByTagName("si:uncertainty").item(0).getTextContent());
                String distribution = eElement.getElementsByTagName("si:distribution").item(0).getTextContent();
                speedOfLight = new SiConstant(label, label, true, label, quantityType, value, unit, dateTime, uncertainty,distribution);
                return speedOfLight;
            }
        }
        return null;
    }

    /**
     * This is an auxiliary function to create a xml document from the received string.
     * @param xmlString String
     * @return Document containing all the information from the string
     */
    private static Document convertStringToXMLDocument(String xmlString) {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
            //Parse the content to Document object
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
