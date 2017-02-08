package com.csra.mapstruct.mapper;

import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.primitive.StringDt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by steffen on 12/27/16.
 */
@Component
public class HumanNameMapper {

    static Logger log = LoggerFactory.getLogger(HumanNameMapper.class.getName());

    public String asString(List<HumanNameDt> humanName) {
        HumanNameDt name = humanName.get(0);
        return new String(name.getFamily().get(0).getValue().concat(", ").concat(name.getGiven().get(0).getValue()));
    }

    public List<HumanNameDt> asHumanName(String fullname) {
        try {
            List<HumanNameDt> names = new ArrayList<HumanNameDt>();
            HumanNameDt name = new HumanNameDt();
            String[] nameTokens = fullname.split(",");
            name.setText(fullname);
            name.getFamily().add(new StringDt(nameTokens[0]));
            name.getGiven().add(new StringDt(nameTokens[1]));
            name.setId(UUID.randomUUID().toString());
            names.add(name);
            return names;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
