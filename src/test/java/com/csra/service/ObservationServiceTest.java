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

    String jsonObservation = "{\"resourceType\":\"Observation\",\"identifier\":[{\"value\":\"1\"},{\"value\":\"2\"},{\"value\":\"3\"}],\"code\":{\"coding\":[{\"system\":\"L\",\"code\":\"008342\",\"display\":\"UPPER RESPIRATORY CULTURE\"},{\"system\":\"L\",\"code\":\"997231\",\"display\":\"RESULT 1\"},{\"system\":\"L\",\"code\":\"997232\",\"display\":\"RESULT 2\"}]},\"subject\":{\"display\":\"2161348462\"},\"issued\":\"1998-07-31T15:32:00.000-07:00\",\"valueQuantity\":{\"value\":0,\"code\":\"CE\"}}";
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

        log.info(oruR01.printStructure());
    }

}