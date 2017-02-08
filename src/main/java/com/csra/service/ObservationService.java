package com.csra.service;

import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.hl7v2.model.v22.message.ORU_R01;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by steffen on 1/21/17.
 */
@Service
public class ObservationService extends RootService {

    static Logger log = LoggerFactory.getLogger(ObservationService.class.getName());

    public Observation toObservation(ORU_R01 oru) {
        return new Observation();
    }

    public ORU_R01 toOru(Observation observation) {
        return new ORU_R01();
    }

}