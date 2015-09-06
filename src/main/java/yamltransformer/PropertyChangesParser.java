package yamltransformer;


import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

public class PropertyChangesParser {

    public static final String START_LIST_OF_PROPERTIES = "[";
    public static final String END_LIST_OF_PROPERTIES = "]";
    public static final String PROPERTY_CHANGES_SEPARATOR = "|";
    public static final String PROPERTY_SET_OPERATOR = "->";
    public static final String PROPERTY_RESOLUTION_OPERATOR = ".";

    private static final String QUOTED_PROPERTY_CHANGES_SEPARATOR = Pattern.quote(PROPERTY_CHANGES_SEPARATOR);
    private static final String QUOTED_PROPERTY_SET_OPERATOR = Pattern.quote(PROPERTY_SET_OPERATOR);
    private static final String QUOTED_PROPERTY_RESOLUTION_OPERATOR = Pattern.quote(PROPERTY_RESOLUTION_OPERATOR);

    private final String propertyChangesString;

    public PropertyChangesParser(String propertyChangesString) {
        this.propertyChangesString = propertyChangesString.trim();
    }

    public Set<PropertyChange> parse() throws ParseException {
        Set<PropertyChange> propertyChanges = new HashSet<>();
        for (String unparsedChange : getFragments()) {
            propertyChanges.add(parsePropertyChange(unparsedChange));
        }
        return propertyChanges;
    }

    private List<String> getFragments() throws ParseException {
        if (propertyChangesString.isEmpty()) {
            return Collections.emptyList();
        } else {
            if (propertyChangesString.startsWith(START_LIST_OF_PROPERTIES)) {
                if (!propertyChangesString.endsWith(END_LIST_OF_PROPERTIES)) {
                    throw new ParseException("List of properties should end with: " + END_LIST_OF_PROPERTIES, propertyChangesString.length() - 1);
                } else {
                    String changesWithoutListDelimiters = propertyChangesString.substring(1, propertyChangesString.length() - 1);
                    return Lists.newArrayList(changesWithoutListDelimiters.split(QUOTED_PROPERTY_CHANGES_SEPARATOR));
                }
            } else {
                return Lists.newArrayList(propertyChangesString);
            }
        }
    }

    private PropertyChange parsePropertyChange(String unparsedChange) throws ParseException {
        String[] unparsedChanges = unparsedChange.split(QUOTED_PROPERTY_SET_OPERATOR);
        if (unparsedChanges.length != 2) {
            throw new ParseException("Wrong change specification: " + unparsedChange, 0);
        }
        String propertiesPathString = unparsedChanges[0].trim();
        String properyValue = unparsedChanges[1].trim();
        List<String> propertyTokens = Lists.newArrayList(propertiesPathString.split(QUOTED_PROPERTY_RESOLUTION_OPERATOR));
        return new PropertyChange(new Property(propertyTokens), properyValue);
    }

}
