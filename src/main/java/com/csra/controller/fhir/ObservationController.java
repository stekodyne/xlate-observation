package com.csra.controller.fhir;

import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.hl7v2.model.v22.message.ORU_R01;
import ca.uhn.hl7v2.model.v22.segment.OBX;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by steffen on 12/21/16.
 */
@RestController
@EnableSwagger2
@RequestMapping("/observation")
public class ObservationController extends RootController {

    static Logger log = LoggerFactory.getLogger(ObservationController.class.getName());

    @ApiOperation(value = "toOru", nickname = "toOru")
    @RequestMapping(method = RequestMethod.GET, path = "/toOru", produces = "x-application/hl7-v2+er7")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "observation", value = "FHIR Observation", required = true, dataType = "string", paramType = "body", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ORU_R01.class),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<String> toOru(@RequestParam String patient) {
        ResponseEntity<String> response = null;

        observationService.toOru(new Observation());

        return response;
    }

    @ApiOperation(value = "toObservation", nickname = "toObservation")
    @RequestMapping(method = RequestMethod.GET, path = "/toObservation", produces = "x-application/fhir+json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "obx", value = "HL7 OBX", required = true, dataType = "string", paramType = "body", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Observation.class),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<String> toObservation(@RequestBody String oru) {
        ResponseEntity<String> response = null;

        Observation observation = observationService.toObservation(new ORU_R01());

        return response;
    }

}
