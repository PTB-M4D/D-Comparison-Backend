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

import de.ptb.backend.model.DKCRRequestMessage;
import de.ptb.backend.model.dsi.SiReal;
import org.springframework.boot.configurationprocessor.json.JSONException;

import javax.xml.parsers.ParserConfigurationException;
import java.text.ParseException;
import java.util.List;

public interface I_PidDccFileSystemReader {
    /**
     * This function sets the message which is used to read out all the important information from the request
     * @param message DKCRRequestMessage which contains all the information from request of the frontend
     */
    void setMessage(DKCRRequestMessage message);

    /**
     * This function iterates through the participantList of the Requestmessage and the dcc files on the system
     * and creates SiReals for every matching name containing the values of the respective dcc file.
     * @return List<SiReal> which contains the mass values of the participant dcc files
     * @throws ParserConfigurationException Throws exception if the DocumentBuilderFactory is not set up properly.
     */
    List<SiReal> readFiles() throws ParserConfigurationException, JSONException;
}
