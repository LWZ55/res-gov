package com.htsx.resgov.utils;

import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Pattern;

@Component
public class ParamCheckingHelper {


    public boolean isNullableRight(String fieldVal, String nullableLimit) {
        if (nullableLimit.equals("false"))
            return fieldVal !=null;
        else
            return true;
    }

    //targetType均为大写
    public boolean isTypeAndSizeRight(String fieldVal, String targetType, String sizeLimit) {
        boolean isRight = false;
        int size = Integer.valueOf(sizeLimit);
        switch (targetType) {
            case "VARCHAR":
            case "CHAR":
                isRight = isStringTypeSize(fieldVal, size);
                break;
            case "DECIMAL":
                isRight = isDoubleTypeSize(fieldVal, size);
                break;
            case "INT":
                isRight = isIntTypeSize(fieldVal);
                break;
            case "TIMESTAMP":
                isRight = isTimeStampTypeSize(fieldVal, size);
            default:
                break;
        }

        return isRight;


    }

    private boolean isStringTypeSize(String fieldVal, int sizeLimit) {
        if (fieldVal != null && fieldVal.length() > sizeLimit)
            return false;
        return true;
    }

    public int[] getNumberDigits(String number) {

        int[] counts = new int[]{0, 0};
        String[] num = number.split("\\.");
        System.out.println(Arrays.asList(num));
        counts[0] = num[0].length();
        if (num.length == 2)
            counts[1] = num[1].length();

        return counts;
    }

    //TODO：小数
    public boolean isDoubleTypeSize(String fieldVal, int sizeLimit) {


        if (fieldVal != null) {
            try {
                Double.parseDouble(fieldVal);
            } catch (NumberFormatException e) {
                return false;
            }
            int[] counts = getNumberDigits(fieldVal);
            if (counts[0] > sizeLimit)
                return false;

        }
        return true;

    }

    public boolean isIntTypeSize(String fieldVal) {

        if (null == fieldVal || "".equals(fieldVal)) {
            return true;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(fieldVal).matches();

    }


    @Test
    public void test() {
        System.out.println(isDoubleTypeSize("1.", 5));
        System.out.println(isDoubleTypeSize(".1", 5));
        System.out.println(isDoubleTypeSize("1.1", 5));
        System.out.println(isDoubleTypeSize("01.0", 5));
        //System.out.println(getNumberDecimalDigits(".111110"));
    }

    public boolean isTimeStampTypeSize(String fieldVal, int sizeLimit) {
        return false;

    }

}
