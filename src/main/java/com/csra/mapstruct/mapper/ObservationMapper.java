package com.csra.mapstruct.mapper;

import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.hl7v2.model.v22.message.ORU_R01;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by steffen on 12/27/16.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ReferenceMapper.class, IdMapper.class})
public interface ObservationMapper {

    ObservationMapper INSTANCE = Mappers.getMapper(ObservationMapper.class);

    static Logger log = LoggerFactory.getLogger(ObservationMapper.class.getName());

    @Mappings({
            @Mapping(source = "ien", target = "id"),
            @Mapping(source = "patient", target = "subject")
    })
    Observation oruToObservation(ORU_R01 oru);

    @InheritInverseConfiguration
    ORU_R01 oruFromObservation(Observation observation);

}
