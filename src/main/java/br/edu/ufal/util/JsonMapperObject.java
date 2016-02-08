package br.edu.ufal.util;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andersonjso on 10/15/15.
 */
public class JsonMapperObject {

    private ObjectMapper mapper;

    public JsonMapperObject() {
        this.mapper = new ObjectMapper();

        VisibilityChecker<?> visibilityChecker = mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE);

        mapper.setVisibilityChecker(visibilityChecker);
    }

    public <A> A fromJson(JsonNode objectJson, Class<A> object) {
        try {
            return mapper.treeToValue(objectJson, object);

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode toJson(Object object) {
        return mapper.valueToTree(object);
    }

    public JsonNode toJson(String objectString) {
        try{
            return mapper.readValue(objectString, JsonNode.class);
        }catch (Throwable var2) {
            throw new RuntimeException(var2);
        }
    }

    public <A> A parse(String stringJson, Class<A> object) {
        try {
            JsonNode jsonNode = toJson(stringJson);
            return mapper.treeToValue(jsonNode, object);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <A> List<A> toList(String bodyResponseCounter, Class<A> object) {
            List<A> listToReturn = new ArrayList<>();
            JsonNode jsonNode = toJson(bodyResponseCounter);

            for (JsonNode jNode : jsonNode){
                A pojo = mapper.convertValue(jNode, object);
                listToReturn.add(pojo);
            }

            return listToReturn;
    }
}
