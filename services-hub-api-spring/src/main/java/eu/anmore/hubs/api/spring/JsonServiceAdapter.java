package eu.anmore.hubs.api.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.anmore.hubs.registration.HubService;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class JsonServiceAdapter<REQUEST, RESPONSE> implements HubService {

    private final Class<REQUEST> requestClass;

    private final Class<RESPONSE> responseClass;

    public JsonServiceAdapter() {
        Type requestType = ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        if (requestType instanceof Class) {
            this.requestClass = (Class<REQUEST>) requestType;
        } else if (requestType instanceof ParameterizedType) {
            this.requestClass = (Class<REQUEST>) ((ParameterizedType) requestType).getRawType();
        } else {
            throw new RuntimeException("Can't parse request type");
        }

        Type responseType = ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
        if (responseType instanceof Class) {
            this.responseClass = (Class<RESPONSE>) responseType;
        } else if (responseType instanceof ParameterizedType) {
            this.responseClass = (Class<RESPONSE>) ((ParameterizedType) responseType).getRawType();
        } else {
            throw new RuntimeException("Can't parse response type");
        }
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
