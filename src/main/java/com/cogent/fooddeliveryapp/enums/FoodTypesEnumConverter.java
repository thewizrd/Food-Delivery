package com.cogent.fooddeliveryapp.enums;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * FoodTypesEnumConverter
 * 
 * Converts string value to upper case to easily handle conversion
 *
 * @author bryan
 * @date Feb 25, 2022-4:26:09 PM
 */
@Component
public class FoodTypesEnumConverter implements Converter<String, FoodTypes> {
    @Override
    public FoodTypes convert(String value) {
        return FoodTypes.valueOf(value.toUpperCase());
    }
}