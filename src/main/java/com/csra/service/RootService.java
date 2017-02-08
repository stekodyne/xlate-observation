package com.csra.service;

import com.csra.mapstruct.mapper.ObservationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by steffen on 1/21/17.
 */
public class RootService {

    // Jackson
    @Autowired
    protected ObjectMapper objectMapper;

    // Mapstruct Mappers
    @Autowired
    protected ObservationMapper observationMapper;

}
