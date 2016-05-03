package com.ymatou.payment.infrastructure.serialize;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;

@Provider
public class JacksonConfig implements ContextResolver<ObjectMapper> {

	private final ObjectMapper objectMapper;

	public JacksonConfig() {
		objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(new BigCamelPropertyNamingStrategy());
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}