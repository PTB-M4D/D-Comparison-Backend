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

import javax.swing.text.Document;
import javax.xml.xpath.XPathExpressionException;

public interface I_PidConstantWebReader {
    /**
     * This function sets the constant which is later requested from the constant backend.
     * @param constant String
     */
    void setConstant(String constant);

    /**
     * This function establishes a connection with this.dConstantUrl and this.constant and reads the necessary contents from the xml file received.
     * @return SiConstant containing the wanted constant
     * @throws XPathExpressionException
     */
    SiConstant getConstant() throws XPathExpressionException;
}
