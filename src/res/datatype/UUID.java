package res.datatype;

import com.testDataBuilder.core.baseType.TypeAdapter;

public class UUID extends TypeAdapter{


    public double getMax() {
        return 36;
    }

    public double getMin() {
        return 36;
    }

    public String getName() {
        return "UUID";
    }

    public Object randomValue(double min, double max) {
        return java.util.UUID.randomUUID().toString();
    }

    public Object valueOf(String value) {
        return value;
    }

}
