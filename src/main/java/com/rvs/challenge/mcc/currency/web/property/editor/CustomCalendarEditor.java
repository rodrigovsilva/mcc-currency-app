package com.rvs.challenge.mcc.currency.web.property.editor;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.lang.invoke.MethodHandles;
import java.util.Calendar;

/**
 * Custom calendar editor.
 */
public class CustomCalendarEditor extends PropertyEditorSupport {

    /**
     * Logger definition.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.date();

    @Override
    public String getAsText() {
        Calendar value = (Calendar) getValue();
        DateTime dateTime = new DateTime(value);
        String str = (value != null ? dateTime.toString(this.dateTimeFormatter) : "");


        return str;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.hasText(text)) {
            setValue(null);
        } else {
            LocalDate dateTime = this.dateTimeFormatter.parseLocalDate(text);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateTime.toDate());
            setValue(cal);
        }
    }
}