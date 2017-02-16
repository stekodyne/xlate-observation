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

    String jsonObservation = "{ \"status\": \"final\", \"code\": { \"coding\": [ { \"code\": \"BPP\", \"display\": \"Blood Pressure Panel\", \"system\": \"http://org.mihin.fhir.patientgen.blood-pressures-panel\" } ] }, \"resourceType\": \"Observation\", \"component\": [ { \"valueQuantity\": { \"value\": 126, \"unit\": \"mmHg\" }, \"code\": { \"coding\": [ { \"code\": \"8459-0\", \"display\": \"Systolic\", \"system\": \"http://loinc.org\" } ] } }, { \"valueQuantity\": { \"value\": 84, \"unit\": \"mmHg\" }, \"code\": { \"coding\": [ { \"code\": \"8453-3\", \"display\": \"Diastolic\", \"system\": \"http://loinc.org\" } ] } }, { \"valueQuantity\": { \"value\": 71, \"unit\": \"bpm\" }, \"code\": { \"coding\": [ { \"code\": \"8867-4\", \"display\": \"Heart rate\", \"system\": \"http://loinc.org\" } ] } } ], \"effectiveDateTime\": \"2016-02-19T11:05:57\" }";
    String er7Obx = "MSH|^~\\&|||||20170216041758.424-0800||ORU^R01|3001|T|2.2\rOBR|\rOBX|1|CE|8459-0^Systolic^http://loinc.org||^126|^mmHg|||||final|||20160219110557^SECOND\rOBR|\rOBX|2|CE|8453-3^Diastolic^http://loinc.org||^84|^mmHg|||||final|||20160219110557^SECOND\rOBR|\rOBX|3|CE|8867-4^Heart rate^http://loinc.org||^71|^bpm|||||final|||20160219110557^SECOND";

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

        log.info(oruR01.printStructure());
    }

}