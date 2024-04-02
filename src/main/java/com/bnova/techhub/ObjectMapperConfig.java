package com.bnova.techhub;


import com.fasterxml.jackson.databind.ObjectMapper;

import io.cloudevents.jackson.JsonFormat;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;


@Singleton
public class ObjectMapperConfig implements ObjectMapperCustomizer {
	@Override
	public void customize(ObjectMapper objectMapper) {
		objectMapper.registerModule(JsonFormat.getCloudEventJacksonModule());
	}
}