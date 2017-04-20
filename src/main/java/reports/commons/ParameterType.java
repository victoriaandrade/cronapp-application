package reports.commons;

public enum ParameterType {

    STRING, INTEGER, DATE, BOOLEAN, LONG, DOUBLE, FLOAT;

    public static ParameterType toType(Class<?> aClass) {
        String name = aClass.getSimpleName();
        switch(name) {
            case "Integer":
                return INTEGER;
            case "Date":
                return DATE;
            case "Boolean":
                return BOOLEAN;
            case "Long":
                return LONG;
            case "Double":
                return DOUBLE;
            case "Float":
                return FLOAT;
            default:
                return STRING;
        }
    }

}
