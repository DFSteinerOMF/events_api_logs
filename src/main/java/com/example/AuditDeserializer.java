package com.example;

import com.example.model.Audit;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Created by Dominik on 31.05.2017.
 */
public class AuditDeserializer extends JsonDeserializer<Audit> {

    @Override
    public Audit deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        return new Audit(0,
                node.get("date").textValue(),
                node.get("module").textValue(),
                node.get("userAction").textValue(),
                node.get("description").textValue());
    }
}
