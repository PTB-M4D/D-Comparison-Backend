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

package de.ptb.backend.model.dsi;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class SiConstant {
    private final String pid;
    private final String bipmPID;
    private final Boolean dSIApproved;
    private final String label;
    private final String quantityType;
    private final Double value;
    private final String unit;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private String dateTime;
    private final double uncertainty;
    private final String distribution;

    public SiConstant(String pid, String bipmPID, Boolean dSIApproved, String label, String quantityType, Double value, String unit, String dateTime, double uncertainty, String distribution) {
        this.pid = pid;
        this.bipmPID = bipmPID;
        this.dSIApproved = dSIApproved;
        this.label = label;
        this.quantityType = quantityType;
        this.value = value;
        this.unit = unit;
        this.dateTime = dateTime;
        this.uncertainty = uncertainty;
        this.distribution = distribution;
    }

    @Override
    public String toString() {
        return "SiConstant{" +
                "pid='" + pid + '\'' +
                ", bipmPID='" + bipmPID + '\'' +
                ", dSIApproved=" + dSIApproved +
                ", label='" + label + '\'' +
                ", quantityType='" + quantityType + '\'' +
                ", value=" + value +
                ", unit='" + unit + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", uncertainty=" + uncertainty +
                ", distribution='" + distribution + '\'' +
                '}';
    }

}
