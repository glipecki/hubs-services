package eu.anmore.hubs.service;

public class JsonObject {

    private String rawJson;

    /**
     * @deprecated Deserialization only
     */
    @Deprecated
    public JsonObject() {
    }

    private JsonObject(String rawJson) {
        this.rawJson = rawJson;
    }

    public static JsonObject of(String rawJson) {
        return new JsonObject(rawJson);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("JsonObject{");
        sb.append("rawJson='").append(rawJson).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getRawJson() {
        return rawJson;
    }

}
