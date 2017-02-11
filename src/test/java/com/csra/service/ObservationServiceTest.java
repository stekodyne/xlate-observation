package com.csra.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v22.message.ORU_R01;
import ca.uhn.hl7v2.parser.PipeParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by steffen on 2/9/17.
 */
public class ObservationServiceTest {
    static Logger log = LoggerFactory.getLogger(ObservationServiceTest.class.getName());

    FhirContext fhirContext = FhirContext.forDstu2();

    String jsonObservation = "{ \"resourceType\": \"Observation\", \"id\": \"f003\", \"text\": { \"fhir_comments\": [ \"     urn:oid:2.16.840.1.113883.4.642.1.7     \", \"     2.16.840.1.113883.4.642.1.118     \" ], \"status\": \"generated\", \"div\": \"<div><p><b>Generated Narrative with Details</b></p><p><b>id</b>: f003</p><p><b>identifier</b>: 6325 (OFFICIAL)</p><p><b>status</b>: final</p><p><b>code</b>: Carbon dioxide in blood <span>(Details : {LOINC code '11557-6' = 'Carbon dioxide [Partial pressure] in Blood', given as 'Carbon dioxide in blood'})</span></p><p><b>subject</b>: <a>P. van de Heuvel</a></p><p><b>effective</b>: 02/04/2013 10:30:10 AM --&gt; 05/04/2013 10:30:10 AM</p><p><b>issued</b>: 03/04/2013 3:30:10 PM</p><p><b>performer</b>: <a>A. Langeveld</a></p><p><b>value</b>: 6.2 kPa<span> (Details: http://unitsofmeasure.org code kPa = '??')</span></p><p><b>interpretation</b>: Above high normal <span>(Details : {http://hl7.org/fhir/v2/0078 code 'H' = 'High', given as 'Above high normal'})</span></p><h3>ReferenceRanges</h3><table><tr><td>-</td><td><b>Low</b></td><td><b>High</b></td></tr><tr><td>*</td><td>4.8 kPa<span> (Details: http://unitsofmeasure.org code kPa = '??')</span></td><td>6.0 kPa<span> (Details: http://unitsofmeasure.org code kPa = '??')</span></td></tr></table></div>\" }, \"identifier\": [ { \"use\": \"official\", \"system\": \"http://www.bmc.nl/zorgportal/identifiers/observations\", \"value\": \"6325\" } ], \"status\": \"final\", \"code\": { \"coding\": [ { \"system\": \"http://loinc.org\", \"code\": \"11557-6\", \"display\": \"Carbon dioxide in blood\" } ] }, \"subject\": { \"reference\": \"Patient/f001\", \"display\": \"P. van de Heuvel\" }, \"effectivePeriod\": { \"start\": \"2013-04-02T10:30:10+01:00\", \"end\": \"2013-04-05T10:30:10+01:00\" }, \"issued\": \"2013-04-03T15:30:10+01:00\", \"performer\": [ { \"reference\": \"Practitioner/f005\", \"display\": \"A. Langeveld\" } ], \"valueQuantity\": { \"value\": 6.2, \"unit\": \"kPa\", \"system\": \"http://unitsofmeasure.org\", \"code\": \"kPa\" }, \"interpretation\": { \"coding\": [ { \"system\": \"http://hl7.org/fhir/v2/0078\", \"code\": \"H\", \"display\": \"Above high normal\" } ] }, \"referenceRange\": [ { \"low\": { \"value\": 4.8, \"unit\": \"kPa\", \"system\": \"http://unitsofmeasure.org\", \"code\": \"kPa\" }, \"high\": { \"value\": 6.0, \"unit\": \"kPa\", \"system\": \"http://unitsofmeasure.org\", \"code\": \"kPa\" } } ] }";
    String er7Obx = "MSH|^~\\&|LCS|LCA|LIS|TEST9999|199807311532||ORU^R01|3629|P|2.2\rPID|2|2161348462|20809880170|1614614|20809880170^TESTPAT||19760924|M|||^^^^00000-0000|||||||86427531^^^03|SSN# HERE\rORC|NW|8642753100012^LIS|20809880170^LCS||||||19980727000000|||HAVILAND\rOBR|1|8642753100012^LIS|20809880170^LCS|008342^UPPER RESPIRATORY CULTURE^L|||19980727175800||||||SS#634748641 CH14885 SRC:THROA SRC:PENI|19980727000000||||||20809880170||19980730041800||BN|F\rOBX|1|ST|008342^UPPER RESPIRATORY CULTURE^L||FINALREPORT|||||N|F||||BN\rORC|NW|8642753100012^LIS|20809880170^LCS||||||19980727000000|||HAVILAND\rOBR|2|8642753100012^LIS|20809880170^LCS|997602^.^L|||19980727175800||||G|||19980727000000||||||20809880170||19980730041800|||F|997602|||008342\rOBX|2|CE|997231^RESULT 1^L||M415|||||N|F||||BN\rNTE|1|L|MORAXELLA (BRANHAMELLA) CATARRHALIS\rNTE|2|L| HEAVY GROWTH\\rNTE|3|L| BETA LACTAMASE POSITIVE\rOBX|3|CE|997232^RESULT 2^L||MR105|||||N|F||||BN\rNTE|1|L|ROUTINE RESPIRATORY FLORA\r";

    @Test
    public void toObservation() throws Exception {
        PipeParser pipeParser = new PipeParser();

        try {
            Message message = pipeParser.parse(er7Obx);
            ObservationService observationService = new ObservationService();
            if(message instanceof ORU_R01) {
                Observation observation = observationService.toObservation((ORU_R01) message);

                log.info("ER7: {}", message.printStructure());
                log.info("FHIR: {}", fhirContext.newJsonParser().encodeResourceToString(observation));
            } else {
                log.info("ER7: {}", message.printStructure());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void toOru() throws Exception {
        ObservationService observationService = new ObservationService();
        Observation observation = fhirContext.newJsonParser().parseResource(Observation.class, jsonObservation);
        ORU_R01 oruR01 = observationService.toOru(observation);

        log.info(oruR01.encode());
    }

}