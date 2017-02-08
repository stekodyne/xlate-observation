package com.csra.controller.fhir;

import ca.uhn.fhir.context.FhirContext;
import com.csra.service.ObservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by steffen on 12/21/16.
 */
@RestController
@EnableSwagger2
public abstract class RootController {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected FhirContext fhirContext;

    @Autowired
    protected ObservationService observationService;

}
