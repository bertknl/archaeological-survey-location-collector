package edu.upenn.sas.archaeologyapp.util;

import java.util.Map;

public class ExtraTypes {
    @FunctionalInterface
    public interface StatusFunction {
        void apply();

    }

    @FunctionalInterface
    public interface ExtraHeadersFunction {
        Map<String, String> apply();

    }
    @FunctionalInterface
    public interface ChangeActivityFunction {
        void apply();

    }


}
