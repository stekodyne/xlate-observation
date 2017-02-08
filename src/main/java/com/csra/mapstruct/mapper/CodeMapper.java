package com.csra.mapstruct.mapper;

import ca.uhn.fhir.model.primitive.CodeDt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by steffen on 12/27/16.
 */
@Component
public class CodeMapper {

    static Logger log = LoggerFactory.getLogger(CodeMapper.class.getName());

    public String asString(CodeDt code) {
        return new String(code.getValue());
    }

    public CodeDt asCode(String value) {
        try {
            return new CodeDt(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
