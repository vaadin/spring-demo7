package com.vaadin.framework7.samples.crud;

import com.vaadin.data.util.converter.StringToBigDecimalConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A converter that adds/removes the euro sign and formats currencies with two
 * decimal places.
 */
public class EuroConverter extends StringToBigDecimalConverter {
    @Override
    public BigDecimal convertToModel(String value, Class<? extends BigDecimal> targetType, Locale locale) throws ConversionException {
        value = value.replaceAll("[€\\s]", "").trim();
        if("".equals(value)) return BigDecimal.ZERO;
        return super.convertToModel(value, targetType, locale);
    }

    @Override
    public String convertToPresentation(BigDecimal value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return super.convertToPresentation(value, targetType, locale) + " €";
    }

    @Override
    protected NumberFormat getFormat(Locale locale) {
        // Always display currency with two decimals
        NumberFormat format = super.getFormat(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setMaximumFractionDigits(2);
            ((DecimalFormat) format).setMinimumFractionDigits(2);
        }
        return format;
    }

}
