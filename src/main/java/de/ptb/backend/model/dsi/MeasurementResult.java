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

import de.ptb.backend.BERT.GEO;
import lombok.Data;

@Data
public class MeasurementResult {
    String name = "";
    SiReal massValue;
    Double massDifference;
    Double kcMassValue;
    SiReal kcValue;
    Double enValue;
    SiReal energyValue;
    GEO grubsValue;
    Double grubsUncertainty;
    Double grubsComparisonValue;
    String result = "";

    /**
     * This constructor is for every normal measurement result entry in the new DCC.
     * @param massValue SiReal
     * @param kcMassValue Double
     * @param kcValue SiReal
     * @param enValue Double
     * @param energyValue SiReal
     * @param grubsValue GEO
     */
    public MeasurementResult(SiReal massValue, Double kcMassValue, SiReal kcValue, Double enValue, SiReal energyValue, GEO grubsValue) {
        this.massValue = massValue;
        this.kcMassValue = kcMassValue;
        this.kcValue = kcValue;
        this.enValue = enValue;
        this.energyValue = energyValue;
        this.grubsValue = grubsValue;
        generateMeasurementResult();
    }

    /**
     * This constructor is for the last measurement result entry in the new DCC.
     * @param massDifference Double which has the same value of the manipulator parameter of the function manipulateMassValues in BackendController.java
     * @param kcMassValue Double
     * @param kcValue Double
     * @param grubsValue Double
     * @param grubsUncertainty Double
     */
    public MeasurementResult(Double massDifference, Double kcMassValue, SiReal kcValue, Double grubsValue, Double grubsUncertainty){
        this.kcValue = kcValue;
        this.kcMassValue = kcMassValue;
        this.massDifference = massDifference;
        this.grubsComparisonValue = grubsValue;
        this.grubsUncertainty = grubsUncertainty;
        generateKCMeasurement();
    }

    /**
     * This function generates for the parameter result the content of one measurement result entry of the new DCC file
     */
    private void generateMeasurementResult(){
        this.result = "         <dcc:measurementResult refId=\"NMIJ-1\">\n" +
                "       <dcc:name>\n" +
                "           <dcc:content lang=\"en\">"+this.name+"</dcc:content>\n" +
                "       </dcc:name>\n" +
                "       <dcc:results>\n" +
                "           <dcc:result refType=\"Mass\">\n" +
                "               <dcc:name>\n" +
                "                   <dcc:content lang=\"en\">Mass of Silicon Sphere</dcc:content>\n" +
                "               </dcc:name>\n" +
                "               <dcc:data>\n" +
                "                   <dcc:quantity reftype=\"Kilogram\">\n" +
                "                       <dcc:name>\n" +
                "                           <dcc:content lang=\"en\">Kilogram</dcc:content>\n" +
                "                       </dcc:name>\n" +
                "                       <si:real>\n" +
                "                           <si:label>Mass</si:label>\n" +
                "                           <si:value>"+this.massValue.getValue()+"</si:value>\n" +
                "                           <si:unit>"+this.massValue.getUnit()+"</si:unit>\n" +
                "                           <si:expandedUnc>\n" +
                "                               <si:uncertainty>"+this.massValue.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
                "                               <si:coverageFactor>"+this.massValue.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
                "                               <si:coverageProbability>"+this.massValue.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
                "                               <si:distribution>Normal</si:distribution>\n" +
                "                           </si:expandedUnc>\n" +
                "                       </si:real>\n" +
                "                   </dcc:quantity>\n" +
                "                   <dcc:quantity refType=\"En\">\n" +
                "                       <dcc:name>\n" +
                "                           <dcc:content lang=\"en\">En</dcc:content>\n" +
                "                       </dcc:name>\n" +
                "                       <si:real>\n" +
                "                           <si:value>"+this.enValue+"</si:value>\n" +
                "                           <si:unit>\\one</si:unit>\n" +
                "                       </si:real>\n" +
                "                   </dcc:quantity>\n" +
                "               </dcc:data>\n" +
                "           </dcc:result>\n" +
                "           <dcc:result refType=\"Energy (Grubs)\">\n" +
                "               <dcc:name>\n" +
                "                   <dcc:content lang=\"en\">Energy (Grubs)</dcc:content>\n" +
                "               </dcc:name>\n" +
                "               <dcc:data>\n" +
                "                   <dcc:quantity>\n" +
                "                       <dcc:name>\n" +
                "                           <dcc:content lang=\"en\">Energy (Grubs)</dcc:content>\n" +
                "                       </dcc:name>\n" +
                "                       <si:real>\n" +
                "                           <si:label>Energy</si:label>\n" +
                "                           <si:value>"+this.energyValue.getValue()+"</si:value>\n" +
                "                           <si:unit>"+this.energyValue.getUnit()+"</si:unit>\n" +
                "                           <si:expandedUnc>\n" +
                "                               <si:uncertainty>"+this.energyValue.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
                "                               <si:coverageFactor>"+this.energyValue.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
                "                               <si:coverageProbability>"+this.energyValue.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
                "                           </si:expandedUnc>\n" +
                "                       </si:real>\n" +
                "                   </dcc:quantity>\n" +
                "                </dcc:data>\n" +
                "           </dcc:result>\n" +
                "           <dcc:result refType=\"Energy\">\n"+
                "               <dcc:name>\n" +
                "                   <dcc:content lang=\"en\">Energy</dcc:content>\n" +
                "               </dcc:name>\n" +
                "               <dcc:data>\n" +
                "                   <dcc:quantity>\n" +
                "                       <dcc:name>\n" +
                "                           <dcc:content lang=\"en\">Energy</dcc:content>\n" +
                "                       </dcc:name>\n" +
                "                       <si:real>\n" +
                "                           <si:label>Energy</si:label>\n" +
                "                           <si:value>"+this.energyValue.getValue()+"</si:value>\n" +
                "                           <si:unit>"+this.energyValue.getUnit()+"</si:unit>\n" +
                "                           <si:expandedUnc>\n" +
                "                               <si:uncertainty>"+this.energyValue.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
                "                               <si:coverageFactor>"+this.energyValue.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
                "                               <si:coverageProbability>"+this.energyValue.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
                "                           </si:expandedUnc>\n" +
                "                       </si:real>\n" +
                "                   </dcc:quantity>\n" +
                "               </dcc:data>\n" +
                "           </dcc:result>\n" +
                "           <dcc:result refType=\"participant_delta_mass\">\n" +
                "               <dcc:name>\n" +
                "                   <dcc:content lang=\"en\">Delta Mass</dcc:content>\n" +
                "               </dcc:name>\n" +
                "               <dcc:data>\n" +
                "                   <dcc:quantity refType=\"participant_delta_mass\">\n" +
                "                       <dcc:name>\n" +
                "                           <dcc:content lang=\"en\">Delta Mass</dcc:content>\n" +
                "                       </dcc:name>\n" +
                "                       <si:real>\n" +
                "                           <si:value>"+this.massValue.getMassDifference()+"</si:value>\n" +
                "                           <si:unit>"+this.massValue.getUnit()+"</si:unit>\n" +
                "                       </si:real>\n" +
                "                   </dcc:quantity>\n" +
                "               </dcc:data>\n" +
                "           </dcc:result>\n" +
                "       </dcc:results>\n" +
                "   </dcc:measurementResult>\n";
    }
    /**
     * This function generates for the parameter result the content of the last measurement result entry of the new DCC file
     */
    public void generateKCMeasurement(){
        this.result = "         <dcc:measurementResult refId=\"RefVals\">\n" +
                "       <dcc:name>\n" +
                "           <dcc:content lang=\"en\">Mass of Silicon Sphere Output Reference Values</dcc:content>\n" +
                "       </dcc:name>\n" +
                "       <dcc:results>\n" +
                "           <dcc:result refType=\"RefMassUnc\">\n" +
                "           <dcc:name>\n" +
                "               <dcc:content lang=\"en\">Reference Output Values</dcc:content>\n" +
                "           </dcc:name>\n" +
                "           <dcc:data>\n" +
                "               <dcc:quantity>\n" +
                "                   <si:real>\n" +
                "                       <si:value>"+this.kcMassValue+"</si:value>\n" +
                "                       <si:unit>/gram</si:unit>\n" +
                "                       <si:difference>"+this.massDifference+"</si:difference>\n" +
                "                       <si:expandedUnc>\n" +
                "                           <si:uncertainty>0.00002535</si:uncertainty>\n" +
                "                           <si:coverageFactor>2</si:coverageFactor>\n" +
                "                           <si:coverageProbability>0.95</si:coverageProbability>\n" +
                "                           <si:distribution>Normal</si:distribution>\n" +
                "                       </si:expandedUnc>\n" +
                "                   </si:real>\n" +
                "               </dcc:quantity>\n" +
                "           </dcc:data>\n" +
                "           </dcc:result>\n" +
                "           <dcc:result refType=\"Energy\">\n" +
                "               <dcc:name>\n" +
                "                   <dcc:content lang=\"en\">Energy</dcc:content>\n" +
                "               </dcc:name>\n" +
                "               <dcc:data>\n" +
                "                   <dcc:quantity refType=\"Energy\">\n" +
                "                   <dcc:name>\n" +
                "                       <dcc:content lang=\"en\">Energy</dcc:content>\n" +
                "                   </dcc:name>\n" +
                "                       <si:real>\n" +
                "                           <si:label>Energy</si:label>\n" +
                "                           <si:value>"+this.kcValue.getValue()+"</si:value>\n" +
                "                           <si:unit>"+this.kcValue.getUnit()+"</si:unit>\n" +
                "                           <si:expandedUnc>\n" +
                "                               <si:uncertainty>"+this.kcValue.getExpUnc().getUncertainty()+"</si:uncertainty>\n" +
                "                               <si:coverageFactor>"+this.kcValue.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
                "                               <si:coverageProbability>"+this.kcValue.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
                "                               <si:distribution>Normal</si:distribution>\n" +
                "                           </si:expandedUnc>\n" +
                "                       </si:real>\n" +
                "                   </dcc:quantity>\n" +
                "               </dcc:data>\n" +
                "           </dcc:result>\n" +
                "           <dcc:result refType=\"Energy (Grubs)\">\n" +
                "               <dcc:name>\n" +
                "                   <dcc:content lang=\"en\">Energy (Grubs)</dcc:content>\n" +
                "               </dcc:name>\n" +
                "               <dcc:data>\n" +
                "                   <dcc:quantity refType=\"Energy (Grubs)\">\n" +
                "                       <dcc:name>\n" +
                "                           <dcc:content lang=\"en\">Energy (Grubs)</dcc:content>\n" +
                "                       </dcc:name>\n" +
                "                       <si:real>\n" +
                "                           <si:label>Energy (Grubs)</si:label>\n" +
                "                           <si:value>"+this.grubsComparisonValue+"</si:value>\n" +
                "                           <si:unit>"+this.kcValue.getUnit()+"</si:unit>\n" +
                "                           <si:expandedUnc>\n" +
                "                               <si:uncertainty>"+this.grubsUncertainty+"</si:uncertainty>\n" +
                "                               <si:coverageFactor>"+this.kcValue.getExpUnc().getCoverageFactor()+"</si:coverageFactor>\n" +
                "                               <si:coverageProbability>"+this.kcValue.getExpUnc().getCoverageProbability()+"</si:coverageProbability>\n" +
                "                               <si:distribution>Normal</si:distribution>\n" +
                "                           </si:expandedUnc>\n" +
                "                       </si:real>\n" +
                "                   </dcc:quantity>\n" +
                "                </dcc:data>\n" +
                "       </dcc:result>\n" +
                "       </dcc:results>\n" +
                "    </dcc:measurementResult>\n";
    }

    @Override
    public String toString() {
        return result;
    }
}
