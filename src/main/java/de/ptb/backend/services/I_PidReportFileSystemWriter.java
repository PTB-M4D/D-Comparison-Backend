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

import de.ptb.backend.model.dsi.MeasurementResult;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import de.ptb.backend.model.Participant;
import java.util.List;

public interface I_PidReportFileSystemWriter {
    /**
     * This function set the pid used for naming the generated file.
     * @param pid String contains the PID which is similar to the name of the generated DCC
     */
    void setPid(String pid);

    /**
     * This function set the participants used to be written to the DCC.
     * @param participants List<Participant>
     */
    void setParticipants(List<Participant> participants);

    /**
     * This function set the Measurement Results used to be written to the DCC.
     * @param mResults List<MeasurementResult>
     */
    void setMResults(List<MeasurementResult> mResults);

    /**
     * This function generates the new DCC file from the Measurement results.
     * @return File which is a DCC xml file containing the given information.
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws XPathExpressionException
     * @throws TransformerException
     */
    File writeDataIntoDCC() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException, TransformerException;
}
