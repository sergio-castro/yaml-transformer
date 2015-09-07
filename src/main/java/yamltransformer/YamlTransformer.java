package yamltransformer;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YamlTransformer {

    private static final String HELP_PROPERTIES_MAP = "PROPERTIES_MAP";
    private static final String HELP_FLOW_STYLE = "FLOW_STYLE";
    private static final String ROOT = "root";

    private final String inputFileName;
    private final String outputFileName;
    private final Set<PropertyChange> propertyChanges;


    public static void main(String[] args) throws ParseException, IOException {
        if (args.length < 3) {
            System.out.println("Incorrect number of arguments. ");
            System.out.println("Expected arguments: <INPUT_FILE> <OUTPUT_FILE> <" + HELP_PROPERTIES_MAP + "> <" + HELP_FLOW_STYLE + ">");
            System.out.println("Where <" + HELP_PROPERTIES_MAP + "> is a list of the form " +
                    PropertyChangesParser.START_LIST_OF_PROPERTIES +
                    "k1" + PropertyChangesParser.PROPERTY_SET_OPERATOR + "v1" + PropertyChangesParser.PROPERTY_CHANGES_SEPARATOR +
                    "k2" + PropertyChangesParser.PROPERTY_SET_OPERATOR + "v2" + PropertyChangesParser.PROPERTY_CHANGES_SEPARATOR +
                    "..." + PropertyChangesParser.END_LIST_OF_PROPERTIES);
            //System.out.println("<" + HELP_FLOW_STYLE + "> is one of: " + Arrays.toString(DumperOptions.FlowStyle.values()));
        } else {
            String inputFileName = args[0];
            String outputFileName = args[1].isEmpty() ? args[0] : args[1];
            Set<PropertyChange> propertyChanges = new PropertyChangesParser(args[2]).parse();

            DumperOptions dumperOptions = (args.length >=4) ? parseDumperOptions(args[3]) : new DumperOptions();
            YamlProvider.set(new Yaml(dumperOptions));

            new YamlTransformer(inputFileName, outputFileName, propertyChanges).apply();
        }
    }

    public static DumperOptions parseDumperOptions(String dumperOptionsString) {
        DumperOptions dumperOptions = new DumperOptions();
        DumperOptions.FlowStyle flowStyle = DumperOptions.FlowStyle.valueOf(dumperOptionsString.trim().toUpperCase());
        dumperOptions.setDefaultFlowStyle(flowStyle);
        return dumperOptions;
    }

    public YamlTransformer(String inputFileName, String outputFileName, Set<PropertyChange> propertyChanges) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.propertyChanges = propertyChanges;
    }

    public void apply() throws IOException {
        Object data = loadYaml();
        for (PropertyChange propertyChange : propertyChanges) {
            data = apply(propertyChange, data);
        }
        dumpYaml(data);
    }

    private Object apply(PropertyChange propertyChange, Object data) {
        List<String> propertyTokens = propertyChange.getProperty().getPropertyTokens();

        int i = 0;
        if (!propertyTokens.isEmpty() && propertyTokens.get(i).equals(ROOT)) {
            i++;
        }

        if (propertyTokens.size() == i) {
            data = propertyChange.getValue();
        } else {
            Object node = data;
            while (i < propertyTokens.size() - 1) {
                if (!(node instanceof Map)) {
                    throw new UnsupportedOperationException("The current implementation only supports the modification of map elements.");
                }
                node = ((Map) node).get(propertyTokens.get(i));
                i++;
            }
            ((Map) node).put(propertyTokens.get(i), propertyChange.getValue());
        }
        return data;
    }

    private Object loadYaml() {
        try {
            try (InputStream in = Files.newInputStream(Paths.get(inputFileName))) {
                return YamlProvider.get().load(in);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void dumpYaml(Object data) {
        File outputFile = new File(outputFileName);
        try {
            if (!outputFile.exists()) {
                if (!outputFile.createNewFile()) {
                    throw new IOException("Impossible to create file: " + outputFile.getAbsolutePath());
                }
            }
            try (Writer writer=new FileWriter(outputFile)) {
                YamlProvider.get().dump(data, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
