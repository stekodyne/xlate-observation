package com.csra.service;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Type;
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
import ca.uhn.hl7v2.model.v22.group.ORU_R01_PATIENT_RESULT;
import ca.uhn.hl7v2.model.v22.message.ORU_R01;
import ca.uhn.hl7v2.model.v22.segment.MSH;
import ca.uhn.hl7v2.model.v22.segment.OBX;
import ca.uhn.hl7v2.model.v22.segment.PID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.st;

/**
 * Created by steffen on 1/21/17.
 */
@Service
public class ObservationService extends RootService {

    static Logger log = LoggerFactory.getLogger(ObservationService.class.getName());

    public Observation toObservation(ORU_R01 oru) {
        Observation observation = new Observation();


        PID pid = oru.getPATIENT_RESULT().getPATIENT().getPID();
        MSH msh = oru.getMSH();

        try {
            for(ORU_R01_ORDER_OBSERVATION oo : oru.getPATIENT_RESULT().getORDER_OBSERVATIONAll()) {
                for (ORU_R01_OBSERVATION o : oo.getOBSERVATIONAll()) {
                    OBX obx = o.getOBX();

                    IdentifierDt identifierDt = new IdentifierDt();
                    identifierDt.setValue(obx.getObx1_SetIDObservationalSimple().getValue());
                    observation.getIdentifier().add(identifierDt);

                    CodeableConceptDt code = observation.getCode();
                    CodingDt coding = new CodingDt();
                    CE ce = obx.getObservationIdentifier();
                    coding.setCode(ce.getCe1_Identifier().getValue());
                    coding.setDisplay(ce.getCe2_Text().getValue());
                    coding.setSystem(ce.getCe3_NameOfCodingSystem().getValue());
                    code.addCoding(coding);

                    List<ResourceReferenceDt> performers = observation.getPerformer();
                    ResourceReferenceDt performer = new ResourceReferenceDt();
                    performer.setDisplay(obx.getResponsibleObserver().getCn2_FamilyName().getValue());
                    performer.setReference(obx.getResponsibleObserver().getCn1_IDNumber().getValue());
                    performers.add(performer);

                    // Not loop enabled
                    observation.setStatus(ObservationStatusEnum.forCode(obx.getObservationResultStatus().getValue()));

                    ResourceReferenceDt subject = observation.getSubject();
                    subject.setDisplay(pid.getPatientIDExternalID().getCk1_IDNumber().getValue());

                    PeriodDt period = new PeriodDt();
                    try {
                        period.setStartWithSecondsPrecision(obx.getObx14_DateTimeOfTheObservation().getTimeOfAnEvent().getValueAsDate());
                    } catch (DataTypeException e) {
                        e.printStackTrace();
                    }

                    try {
                        observation.setIssuedWithMillisPrecision(msh.getDateTimeOfMessage().getTimeOfAnEvent().getValueAsDate());
                    } catch (DataTypeException e) {
                        e.printStackTrace();
                    }

                    QuantityDt value = new QuantityDt();
                    Type type = obx.getObservationValue().getData();
                    value.setCode(obx.getValueType().getValue());
                    value.setUnit(obx.getUnits().getCe2_Text().getValue());
                    if(type instanceof CE) {
                        try {
                            CE quantity = (CE) type;
                            value.setValue(new DecimalDt((quantity.getText().getValue())));
                        } catch (Exception e) {
                            value.setValue(0);
                        }
                    } else {

                    }
                    value.setSystem(obx.getUnits().getNameOfCodingSystem().getValue());
                    observation.setValue(value);
                }
            }
        } catch (HL7Exception e) {
            e.printStackTrace();
        }

        return observation;
    }

    public ORU_R01 toOru(Observation observation) {
        ORU_R01 message = new ORU_R01();

        try {
            QuantityDt value = (QuantityDt) observation.getValue();

            message.initQuickstart("ORU", "R01", "T");

            MSH msh = message.getMSH();
            ORU_R01_PATIENT_RESULT patientResult = message.getPATIENT_RESULT();

            int i = 0;
            for(IdentifierDt identifierDt : observation.getIdentifier()) {
                OBX obx = patientResult.insertORDER_OBSERVATION(i++).getOBSERVATION().getOBX();
                SI si1 = obx.getSetIDObservationalSimple();
                si1.setValue(identifierDt.getValue());
            }

            OBX obx = patientResult.getORDER_OBSERVATION().getOBSERVATION(0).getOBX();

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
            try {
                st7.setValue(observation.getReferenceRange().get(0).getHigh().getValue().toString());
            } catch (Exception e) {
                st7.setValue("");
            }


            ID[] id8 = obx.getAbnormalFlags();

            NM nm9 = obx.getProbability();

            ID id10 = obx.getNatureOfAbnormalTest();

            ID id11 = obx.getObservationResultStatus();
            id2.setValue(observation.getStatus());

            //TS ts12 = obx.getEffectiveDateLastObservationNormalValues();
            ST st13 = obx.getUserDefinedAccessChecks();

            TS ts14 = obx.getDateTimeOfTheObservation();
            try {
                ts14.getTimeOfAnEvent().setValue(((PeriodDt) observation.getEffective()).getStart().toString());
            } catch (Exception e) {
                ts14.getTimeOfAnEvent().setValue(new Date());
            }
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