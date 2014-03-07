package net.nemerosa.iteach.common.json;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ObjectMapperFactory {

    public static ObjectMapper create() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getDeserializationConfig();
        AnnotationIntrospector pair = AnnotationIntrospector.pair(config.getAnnotationIntrospector(), new ConstructorPropertiesAnnotationIntrospector());
        return mapper.setAnnotationIntrospector(pair);
    }

    private ObjectMapperFactory() {
    }

}
