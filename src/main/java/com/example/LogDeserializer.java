package com.example;

import com.example.model.Log;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Created by Szymon on 02.04.2017.
 */
public class LogDeserializer extends JsonDeserializer<Log> {

    @Override
    public Log deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        return new Log(0,node.get("date").textValue(),node.get("description").textValue(),node.get("module").textValue());
    }
}
