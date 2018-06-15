package com.rvs.challenge.mcc.currency.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Parser Util.
 */
public class ObjectParserUtil {

    /**
     * Logger definition.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static ObjectParserUtil ourInstance = new ObjectParserUtil();

    private ObjectParserUtil() {
    }

    public static ObjectParserUtil getInstance() {
        return ourInstance;
    }

    /**
     * Convert an object to string.
     * @param o Object to be converted.
     * @return String as Json.
     */
    public String toString(Object o) {
        String result;

        try {
            result = new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            result = e.getLocalizedMessage();
        }
        return result;
    }

}
