package com.csra.mapstruct.mapper;

import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import org.springframework.stereotype.Component;

/**
 * Created by steffen on 12/27/16.
 */
@Component
public class ReferenceMapper {

    public String asString(ResourceReferenceDt reference) {
        return new String(reference.getDisplay().toString());
    }

    public ResourceReferenceDt asReference(String item) {
        try {
            ResourceReferenceDt reference = new ResourceReferenceDt();
            reference.setDisplay(item);
            return reference;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
