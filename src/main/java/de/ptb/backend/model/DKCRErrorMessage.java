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
 * LAST MODIFIED:	15.08.23, 15:41
 */

package de.ptb.backend.model;

import lombok.Data;

@Data
public class DKCRErrorMessage {
    String errorMessage;

    /**
     * This class contains the information of an error message which will be attached of an error response in BackendController.java
     * @param message String which contains the details of the error
     */
    public  DKCRErrorMessage(String message){
        this.errorMessage = message;
    }
    @Override
    public String toString() {
        return "DKCRResponseMessage{" +
                "Message='" + this.errorMessage +'\'' +
                '}';
    }
}
