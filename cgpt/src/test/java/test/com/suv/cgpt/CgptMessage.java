package test.com.suv.cgpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CgptMessage {

    public static final String GPT_3_5 = "gpt-3.5-turbo";
    public static final String GPT_4 = "gpt-4";
    public static final String MODEL_TAG = "model";
    public static final int DEFAULT_MAX_TOKENS = 1000;
    public static final String MAX_TOKENS_TAG = "max_tokens";

    public static final String TEMPERATURE_TAG = "temperature";

    public static final float DEFAULT_TEMPERATURE_VALUE = 0f;

    public static final String ROLE_TAG = "role";

    public static final String CONTENT_TAG = "content";
    public static final String MESSAGES_TAG = "messages";
    public static final String ROLE_SYSTEM = "system";
    public static final String ROLE_USER = "user";


    ObjectNode rootNode;
    ArrayNode messages;
    ObjectNode roleSystem;
    ObjectNode user;

    public CgptMessage() {

        ObjectMapper mapper = new ObjectMapper();
        rootNode = mapper.createObjectNode();
        rootNode.put(MODEL_TAG, GPT_3_5); // Или "gpt-4"
        rootNode.put(MAX_TOKENS_TAG, DEFAULT_MAX_TOKENS);
        rootNode.put(TEMPERATURE_TAG, DEFAULT_TEMPERATURE_VALUE);

        messages =  rootNode.putArray(MESSAGES_TAG);
        roleSystem = messages.addObject();
        roleSystem.put(ROLE_TAG, ROLE_SYSTEM);
        user = messages.addObject().put(ROLE_TAG, ROLE_USER);

    }

    public CgptMessage(ObjectNode root) {
        this.rootNode = root;
    }

    public CgptMessage role(String message) {
        roleSystem.put(CONTENT_TAG, message);
        return this;
    }

    public CgptMessage content(String message){
        user.put(CONTENT_TAG, message);
        return this;
    }

    public CgptMessage model(String model) {
        rootNode.put(MODEL_TAG, model);
        return this;
    }

    public CgptMessage temperature(Float temperature) {
        rootNode.put(TEMPERATURE_TAG, temperature);
        return this;
    }

    public ObjectNode getRoot() {
        return this.rootNode;
    }

    public CgptMessage maxTokens(Integer maxTokens){
        rootNode.put(MAX_TOKENS_TAG, maxTokens);
        return this;
    }
    public String getText() {
        return this.rootNode.toPrettyString();
    }
    public String toString() {
        return this.rootNode.toString();
    }
}
