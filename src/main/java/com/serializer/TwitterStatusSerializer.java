package com.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class TwitterStatusSerializer implements Serializer
{
    public void configure(Map map, boolean b)
    {

    }

    public byte[] serialize(String s, Object o)
    {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        CustomStatus status = (CustomStatus) o;
        try
        {
            retVal = objectMapper.writeValueAsString(status).getBytes();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return retVal;
    }

    public void close()
    {

    }
}