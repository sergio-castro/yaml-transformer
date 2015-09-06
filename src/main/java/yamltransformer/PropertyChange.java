package yamltransformer;


import java.util.List;

public class PropertyChange {

    private final Property property;
    private final String value;

    public PropertyChange(Property property, String value) {
        this.property =property;
        this.value = value;
    }

    public Property getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final PropertyChange that = (PropertyChange) o;

        if (!property.equals(that.property)) return false;
        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        int result = property.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
