package za.ac.cput.service;
/*
 * JsonMapperFactory.java
 *
 * Our domain classes (Lecturer, Department, Class, ...) were copied
 * straight from the backend: private fields, a protected no-arg
 * constructor, and a Builder for creating new instances - but no
 * public setters. That's normal for a JPA entity, but it means a
 * default Jackson ObjectMapper can't populate them.
 *
 * This factory configures one ObjectMapper, shared by every
 * *Service class, that:
 *   - reads/writes private fields directly instead of requiring getters/setters
 *   - is allowed to call the protected no-arg constructor
 *   - understands java.time types (LocalDate, etc.)
 *   - ignores any extra JSON fields the backend sends that we don't map
 */
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonMapperFactory {

    private static final ObjectMapper INSTANCE = build();

    private JsonMapperFactory() {
    }

    public static ObjectMapper get() {
        return INSTANCE;
    }

    private static ObjectMapper build() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        VisibilityChecker<?> visibility = mapper.getSerializationConfig()
                .getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.ANY);

        mapper.setVisibility(visibility);

        return mapper;
    }
}
