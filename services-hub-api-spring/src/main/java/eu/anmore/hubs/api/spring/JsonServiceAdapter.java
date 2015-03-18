package eu.anmore.hubs.api.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.anmore.hubs.registration.HubService;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;

public abstract class JsonServiceAdapter<REQUEST, RESPONSE> implements HubService {

    private final Class<REQUEST> requestClass;

    private final Class<RESPONSE> responseClass;

    public JsonServiceAdapter() {
        this.requestClass = (Class<REQUEST>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.responseClass = (Class<RESPONSE>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public String call(String in) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            REQUEST request = (REQUEST) objectMapper.readValue(in, requestClass);
            RESPONSE response = callService(request);
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, response);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract RESPONSE callService(REQUEST request);

}
