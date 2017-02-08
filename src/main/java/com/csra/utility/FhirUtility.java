package com.csra.utility;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome.Issue;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Created by steffen on 1/6/17.
 */
public abstract class FhirUtility {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
    public final static String FHIR_VERSION = "DSTU2";
    private static Logger log = LoggerFactory.getLogger(FhirUtility.class);

    public static InstantDt createInstant(java.util.Date date) {
        InstantDt instant = new InstantDt();
        instant.setValue(date);
        return instant;
    }

    public static IdDt createId(java.lang.String input) {
        IdDt id = new IdDt();
        id.setValue(input);
        return id;
    }

    public static StringDt createString(String input) {
        StringDt output = new StringDt();
        output.setValue(input);
        return output;
    }

    public static IdDt createId() {
        IdDt id = new IdDt();
        id.setValue(UUID.randomUUID().toString());
        return id;
    }

    public static IdDt createFhirVersion() {
        IdDt id = new IdDt();
        id.setId(createId());
        id.setValue("DSTU2");
        return id;
    }

    public static IdentifierDt createIdentifierDt() {
        IdentifierDt identifier = new IdentifierDt();
        identifier.setValue(createUuid().toString());
        return identifier;
    }

    public static StringDt createUuid() {
        return convert(UUID.randomUUID().toString());
    }

    public static StringDt convert(java.lang.String input) {
        StringDt output = new StringDt();
        output.setValue(input);
        return output;
    }

    public static UriDt createUri(String s) {
        UriDt uri = new UriDt();
        uri.setValue(s);
        return uri;
    }

    public static StringDt createUrn(IdentifierDt input) {
        return createUrn(input.getValue());
    }

    public static StringDt createUrn(String input) {
        String output = "urn:uuid:" + input;
        return convert(output);
    }

    public static SimpleQuantityDt createQuantity(BigDecimal input) {
        SimpleQuantityDt quantity = new SimpleQuantityDt();
        quantity.setValue(input);
        return quantity;
    }

    public static CodeDt createCode(String value) {
        CodeDt code = new CodeDt();
        code.setValue(value);
        return code;
    }

    public static CodeableConceptDt createCodeableConceptDt(String code, String display) {
        CodeableConceptDt codeableConcept = new CodeableConceptDt();
        CodingDt coding = new CodingDt();

        coding.setCode(code);
        coding.setDisplay(display);
        codeableConcept.getCoding().add(coding);
        return codeableConcept;
    }

    public static OperationOutcome createOperationOutcome(java.lang.String message, IssueTypeEnum issueType) {
        OperationOutcome operationOutcome = new OperationOutcome();
        Issue issue = new Issue();

        issue.setId(createUuid().getValue());
        issue.setCode(issueType);
        issue.setDetails(createCodeableConceptDt(issueType.getCode(), message));
        operationOutcome.setId(createId());
        operationOutcome.getIssue().add(issue);
        return operationOutcome;
    }

}
