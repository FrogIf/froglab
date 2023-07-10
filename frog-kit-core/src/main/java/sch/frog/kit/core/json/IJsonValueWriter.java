package sch.frog.kit.core.json;

interface IJsonValueWriter {

    void writeString(String str);

    void writeOrigin(String origin);

    void writeObject(JsonObject jsonObject);

    void writeArray(JsonArray array);

    String toJson();

}
