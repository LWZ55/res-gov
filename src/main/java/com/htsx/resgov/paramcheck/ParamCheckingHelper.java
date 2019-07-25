package com.htsx.resgov.paramcheck;

import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
public class ParamCheckingHelper {


    public boolean isNullableRight(String fieldVal, String nullableLimit) {
        if (nullableLimit.equals("false"))
            return fieldVal != null;
        else
            return true;
    }

    //targetType均为大写
    public boolean isTypeAndSizeRight(String fieldVal, String targetType, String sizeLimit, String decimalLimit) {
        boolean isRight = false;
        int size = Integer.valueOf(sizeLimit);
        int decimalDigits = Integer.valueOf(decimalLimit);
        switch (targetType) {
            case "VARCHAR":
            case "CHAR":
                isRight = isStringTypeSize(fieldVal, size);
                break;
            case "DECIMAL":
                isRight = isDoubleTypeSize(fieldVal, size, decimalDigits);
                break;
            case "INT":
                isRight = isIntTypeSize(fieldVal, size);
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

    //返回数组，{总长度，小数位长度}
    public int[] getNumberDigits(String number) {

        int[] counts = new int[]{0, 0};
        String[] num = number.split("\\.");
        System.out.println(Arrays.asList(num));
        counts[0] = number.length() - 1;
        if (num.length == 2)  //考虑
            counts[1] = num[1].length();
        System.out.println(Arrays.toString(counts));
        return counts;
    }


    //0也认为是小数和整数位数
    public boolean isDoubleTypeSize(String fieldVal, int sizeLimit, int decimalLimit) {


        if (fieldVal != null) {
            try {
                Double.parseDouble(fieldVal);
            } catch (NumberFormatException e) {
                return false;
            }
            int[] counts = getNumberDigits(fieldVal);
            if (counts[0] > sizeLimit)
                return false;
            if (counts[1] > decimalLimit)
                return false;

        }
        return true;

    }

    public boolean isIntTypeSize(String fieldVal, int size) {

        if (fieldVal == null)
            return true;

        if (size > 10) {
            try {
                Long.parseLong(fieldVal);
            } catch (NumberFormatException e) {
                return false;
            }

        } else {
            try {
                Integer.parseInt(fieldVal);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }


    @Test
    public void test() {
        System.out.println(isDoubleTypeSize("0.0", 5, 2));
        System.out.println(isDoubleTypeSize("1111.1", 5, 2));
        System.out.println(isDoubleTypeSize("1234.12", 5, 2));
        System.out.println(isDoubleTypeSize("1.123", 5, 2));
        //System.out.println(getNumberDecimalDigits(".111110"));
    }


    public boolean isTimeStampTypeSize(String fieldVal, int sizeLimit) {
        if (fieldVal != null) {
            try {
                Long.parseLong(fieldVal);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }


}
