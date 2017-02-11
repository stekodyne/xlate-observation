package com.csra.service;

import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v22.datatype.TS;
import ca.uhn.hl7v2.model.v22.datatype.CE;
import ca.uhn.hl7v2.model.v22.datatype.CN;
import ca.uhn.hl7v2.model.v22.datatype.ID;
import ca.uhn.hl7v2.model.v22.datatype.NM;
import ca.uhn.hl7v2.model.v22.datatype.SI;
import ca.uhn.hl7v2.model.v22.datatype.ST;
import ca.uhn.hl7v2.model.v22.datatype.TX;
import ca.uhn.hl7v2.model.v22.group.ORU_R01_OBSERVATION;
import ca.uhn.hl7v2.model.v22.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v22.message.ORU_R01;
import ca.uhn.hl7v2.model.v22.segment.MSH;
import ca.uhn.hl7v2.model.v22.segment.OBX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * Created by steffen on 1/21/17.
 */
@Service
public class ObservationService extends RootService {

    static Logger log = LoggerFactory.getLogger(ObservationService.class.getName());

    public Observation toObservation(ORU_R01 oru) {
        Observation observation = new Observation();
        QuantityDt value = new QuantityDt();
        OBX obx = oru.getPATIENT_RESULT().getORDER_OBSERVATION().getOBSERVATION().getOBX();

        value.setCode(obx.getObservationValue().getName());
        value.setUnit(obx.getObservationValue().getData().toString());
        value.setSystem("");

        observation.setValue(value);

        return observation;
    }

    public ORU_R01 toOru(Observation observation) {
        ORU_R01 message = new ORU_R01();

        try {
            QuantityDt value =  (QuantityDt) observation.getValue();

            message.initQuickstart("ORU", "R01", "T");

            MSH msh = message.getMSH();

            OBX obx = message.getPATIENT_RESULT().getORDER_OBSERVATION().getOBSERVATION(0).getOBX();

            SI si1 = obx.getSetIDObservationalSimple();
            si1.setValue(observation.getIdentifier().get(0).getValue());

            ID id2 = obx.getValueType();
            id2.setValue(value.getUnit());

            CE ce3 = obx.getObservationIdentifier();
            ce3.getIdentifier().setValue(observation.getCode().getElementSpecificId());
            ce3.getText().setValue(observation.getCode().getText());
            ce3.getNameOfCodingSystem().setValue(observation.getCode().getCoding().get(0).getDisplay());

            ST st4 = obx.getObservationSubID();

            CE ce = new CE(message);
            ce.getIdentifier().setValue(value.getCode());
            ce.getText().setValue(value.getValue().toString());
            ce.getNameOfCodingSystem().setValue(value.getSystem());
            Varies varies5 = obx.getObservationValue();
            varies5.setData(ce);

            CE ce6 = obx.getUnits();
            ce6.getIdentifier().setValue(value.getCode());
            ce6.getNameOfCodingSystem().setValue(value.getSystem());
            ce6.getText().setValue(value.getUnit());

            ST st7 = obx.getReferencesRange();
            st7.setValue(observation.getReferenceRange().get(0).getHigh().getValue().toString());

            ID[] id8 = obx.getAbnormalFlags();

            NM nm9 = obx.getProbability();

            ID id10 = obx.getNatureOfAbnormalTest();

            ID id11 = obx.getObservationResultStatus();
            id2.setValue(observation.getStatus());

            //TS ts12 = obx.getEffectiveDateLastObservationNormalValues();
            ST st13 = obx.getUserDefinedAccessChecks();

            TS ts14 = obx.getDateTimeOfTheObservation();
            ts14.getTimeOfAnEvent().setValue(((PeriodDt) observation.getEffective()).getStart().toString());
            ts14.getDegreeOfPrecision().setValue("");

            CE ce15 = obx.getProducerSID();
            CN cn16 = obx.getResponsibleObserver();

        } catch (HL7Exception e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

}