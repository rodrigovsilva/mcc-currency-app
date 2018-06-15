package com.rvs.challenge.mcc.currency.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.util.Calendar;

/**
 * Custom java.util.Calendar serializer.
 */
public class CalendarSerializer extends StdSerializer<Calendar> {

    public CalendarSerializer() {
        this(null);
    }

    public CalendarSerializer(Class t) {
        super(t);
    }

    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException {
        gen.writeString(ISODateTimeFormat.dateTime().print(value.getTimeInMillis()));
    }
}
