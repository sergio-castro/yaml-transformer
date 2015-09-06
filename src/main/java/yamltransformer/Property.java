package yamltransformer;

import java.util.List;

public class Property {

    private final List<String> propertyTokens;

    public Property(List<String> propertyTokens) {
        this.propertyTokens =propertyTokens;
    }

    public List<String> getPropertyTokens() {
        return propertyTokens;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Property property = (Property) o;

        return propertyTokens.equals(property.propertyTokens);

    }

    @Override
    public int hashCode() {
        return propertyTokens.hashCode();
    }
}
