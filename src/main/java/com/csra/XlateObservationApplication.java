package com.csra;

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.xml.datatype.XMLGregorianCalendar;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
public class XlateObservationApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(XlateObservationApplication.class);
        application.run(args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    @Bean
    public FhirContext fhirContext() {
        FhirContext fhirContext = FhirContext.forDstu2();
        return fhirContext;
    }

    @Bean
    public Docket observationApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Observation")
                .apiInfo(apiInfo("FHIR/HL7 Observation Xlate Service", "Spring-based FHIR REST API with a little Swagger!"))
                .select()
                .paths(regex("/observation.*"))
                .build()
                .directModelSubstitute(XMLGregorianCalendar.class, String.class);
    }

    private ApiInfo apiInfo(String title, String description) {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl("http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open")
                .contact("Steffen Kory")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
                .version("2.0")
                .build();
    }

}
