package ua.kpi.utils;

import java.util.HashMap;
import java.util.Map;

public class CaptchaChecker {
    Map<Integer, String> variants = new HashMap<Integer, String>() {{
        put(1, "88");
        put(2, "89");
        put(3, "230");
        put(4, "68");
        put(5, "86");
        put(6, "10");
        put(7, "79");
        put(8, "183");
        put(9, "108");
        put(10, "2");
        put(11, "28");
        put(12, "38");
        put(13, "16");
        put(14, "1085");
        put(15, "258");
        put(16, "66");
        put(17, "290");
        put(18, "510");
        put(19, "48");
        put(20, "46");
        put(21, "59");
        put(22, "358");
        put(23, "10");
        put(24, "23");
    }};

    public CaptchaChecker (){}

    public boolean checkCaptcha (int key, String val)
    {
        boolean res = false;

        if(val.equals(variants.get(key)))
        {
            return true;
        }
        return false;
    }
}
