package com.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class TwitterStatusDeserializer implements Deserializer
{

    public void configure(Map map, boolean b) {

    }

    public Object deserialize(String s, byte[] bytes)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        CustomStatus status = null;
        try
        {
            status = objectMapper.readValue(bytes, CustomStatus.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return status;
    }

    public void close() {

    }
}
