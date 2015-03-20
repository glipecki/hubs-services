package eu.anmore.hubs.samples.boot;

import eu.anmore.hubs.api.spring.JsonServiceAdapter;
import eu.anmore.hubs.registration.ServiceRegistration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//  curl -X POST http://localhost:8080/api/v1/services/ComplexDataService/call -H "Content-Type: application/json" -H "Accept: application/json" -d '["test","test"]'
// [{"test":"test"},{"test":"test"}]
@Component
public class ComplexDataService extends JsonServiceAdapter<List<String>, List<Map<String, String>>> {

    @Override
    public List<Map<String, String>> callService(List<String> stringList) {
        final List<Map<String, String>> result = new ArrayList<>();
        if (stringList == null || stringList.isEmpty()) {
            HashMap<String, String> singleRow = new HashMap<>();
            singleRow.put("empty", "empty");
            result.add(singleRow);
        } else {
            for (String s : stringList) {
                HashMap<String, String> singleRow = new HashMap<>();
                singleRow.put(s, s);
                result.add(singleRow);
            }
        }
        return result;
    }

    @Override
    public ServiceRegistration getServiceRegistration() {
        return new ServiceRegistration(ComplexDataService.class.getSimpleName(), "1.0");
    }

}
