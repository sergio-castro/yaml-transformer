package yamltransformer;

import static java.util.Arrays.asList;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.Sets;

public class YamlTransformerTest {

    @BeforeClass
    public static void setUp() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setPrettyFlow(true);
        YamlProvider.set(new Yaml(dumperOptions));
    }

    private static final String BASE_DIR = "src/test/resources/";
    private static final String TEST_DIRECTORY = "examples/";
    private static final String OUTPUT_DIRECTORY = "output/";
    public static final String FULL_TEST_DIRECTORY = BASE_DIR + TEST_DIRECTORY;
    public static final String FULL_OUTPUT_DIRECTORY = BASE_DIR + OUTPUT_DIRECTORY;
    private static final String TEST_FILE_NAME = "test1.yaml";

    private static final String FULL_INPUT_FILE_NAME = FULL_TEST_DIRECTORY + TEST_FILE_NAME;
    private static final String FULL_OUTPUT_FILE_NAME = FULL_OUTPUT_DIRECTORY + TEST_FILE_NAME;

    @Test
    public void testParsing() {
        try {
            Set<PropertyChange> changes = Sets.newHashSet();
            changes.add(new PropertyChange(
                    new Property(asList("root", "billTo", "address", "city")), "New City"));
            new YamlTransformer(FULL_INPUT_FILE_NAME, FULL_OUTPUT_FILE_NAME, changes).apply();
        } catch(IOException e) {
            fail();
        }
    }

}
