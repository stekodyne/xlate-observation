package com.csra.mapstruct.mapper;

import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import org.springframework.stereotype.Component;

/**
 * Created by steffen on 12/27/16.
 */
@Component
public class GenderMapper {

    public String asString(BoundCodeDt<AdministrativeGenderEnum> administrativeGender) {
        return administrativeGender.getValue();
    }

    public BoundCodeDt<AdministrativeGenderEnum> asAdministrativeGender(String sex) {
        try {
            return new BoundCodeDt<>(AdministrativeGenderEnum.VALUESET_BINDER, AdministrativeGenderEnum.forCode(sex));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
