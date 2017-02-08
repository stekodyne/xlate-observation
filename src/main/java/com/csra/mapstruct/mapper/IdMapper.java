package com.csra.mapstruct.mapper;

import ca.uhn.fhir.model.primitive.IdDt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by steffen on 12/27/16.
 */
@Component
public class IdMapper {

    static Logger log = LoggerFactory.getLogger(IdMapper.class.getName());

    public String asString(IdDt id) {
        return new String(id.getValue());
    }

    public IdDt asId(String value) {
        try {
            return new IdDt(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
