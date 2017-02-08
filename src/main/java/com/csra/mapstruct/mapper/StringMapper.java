package com.csra.mapstruct.mapper;

import ca.uhn.fhir.model.primitive.StringDt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by steffen on 12/27/16.
 */
@Component
public class StringMapper {

    static Logger log = LoggerFactory.getLogger(StringMapper.class.getName());

    public String asString(StringDt value) {
        return new String(value.getValue());
    }

    public StringDt asString(String value) {
        try {
            return new StringDt(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
