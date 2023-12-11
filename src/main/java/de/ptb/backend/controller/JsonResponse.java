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

package de.ptb.backend.controller;

import de.ptb.backend.model.Participant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class JsonResponse {
    private String reportType;
    private String messageStatus;

    private Map<String, String> DKCReport;
    List<Participant> participants;

    public JsonResponse(String type, String status, Map<String, String> dkcrreport){
        reportType = type;
        messageStatus = status;
        DKCReport = dkcrreport;
    }
    @Override
    public String toString(){
        StringBuilder message = new StringBuilder("{ "+reportType+":\n\t{\n\t\tmessageStatus: "+ messageStatus +",\n\t\tDKCReport: \n\t\t{");
        int count=0;
        if(DKCReport != null){
            for(Map.Entry<String, String> entry : DKCReport.entrySet()){
                if(count==0){
                    message.append("\n\t\t\t").append(entry.getKey()).append(": ").append(entry.getValue());
                }else{
                    message.append(",\n\t\t\t").append(entry.getKey()).append(": ").append(entry.getValue());
                }
                count++;
            }
        }
        message.append("\n\t\t}\n\t}\n}");
        return message.toString();
    }

}
