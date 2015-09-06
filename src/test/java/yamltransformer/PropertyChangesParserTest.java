package yamltransformer;


import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Set;

import org.junit.Test;

public class PropertyChangesParserTest {

    @Test
    public void testParsingSinglePropertyChange() {
        PropertyChangesParser parser = new PropertyChangesParser("k" + PropertyChangesParser.PROPERTY_SET_OPERATOR + "v");
        Set<PropertyChange> propertyChanges = null;
        try {
            propertyChanges = parser.parse();
        } catch (ParseException e) {
            fail();
        }
        assertEquals(1, propertyChanges.size());
        PropertyChange propertyChange = propertyChanges.iterator().next();
        assertEquals(1, propertyChange.getProperty().getPropertyTokens().size());
        assertEquals("k", propertyChange.getProperty().getPropertyTokens().get(0));
        assertEquals("v", propertyChange.getValue());
    }

    @Test
    public void testParsingSinglePropertyChangeInList() {
        PropertyChangesParser parser = new PropertyChangesParser(
                PropertyChangesParser.START_LIST_OF_PROPERTIES +
                "k" + PropertyChangesParser.PROPERTY_SET_OPERATOR + "v" +
                        PropertyChangesParser.END_LIST_OF_PROPERTIES);
        Set<PropertyChange> propertyChanges = null;
        try {
            propertyChanges = parser.parse();
        } catch (ParseException e) {
            fail();
        }
        assertEquals(1, propertyChanges.size());
        PropertyChange propertyChange = propertyChanges.iterator().next();
        assertEquals(1, propertyChange.getProperty().getPropertyTokens().size());
        assertEquals("k", propertyChange.getProperty().getPropertyTokens().get(0));
        assertEquals("v", propertyChange.getValue());
    }

    @Test
    public void testParsingPropertyChanges() {
        String propertyString1 = "k1x" + PropertyChangesParser.PROPERTY_RESOLUTION_OPERATOR + "k1y" + PropertyChangesParser.PROPERTY_RESOLUTION_OPERATOR + "k1z";
        String propertyString2 = "k2x" + PropertyChangesParser.PROPERTY_RESOLUTION_OPERATOR + "k2y" + PropertyChangesParser.PROPERTY_RESOLUTION_OPERATOR + "k2z";
        PropertyChangesParser parser = new PropertyChangesParser(
                PropertyChangesParser.START_LIST_OF_PROPERTIES +
                        propertyString1 + PropertyChangesParser.PROPERTY_SET_OPERATOR + "v1" +
                        PropertyChangesParser.PROPERTY_CHANGES_SEPARATOR +
                        propertyString2 + PropertyChangesParser.PROPERTY_SET_OPERATOR + "v2" +
                        PropertyChangesParser.END_LIST_OF_PROPERTIES);
        Set<PropertyChange> propertyChanges = null;
        try {
            propertyChanges = parser.parse();
        } catch (ParseException e) {
            fail();
        }
        assertEquals(2, propertyChanges.size());
        PropertyChange pc1 = new PropertyChange(new Property(asList("k1x" ,"k1y", "k1z")), "v1");
        PropertyChange pc2 = new PropertyChange(new Property(asList("k2x" ,"k2y", "k2z")), "v2");
        assertTrue(propertyChanges.contains(pc1));
        assertTrue(propertyChanges.contains(pc2));
    }

}
