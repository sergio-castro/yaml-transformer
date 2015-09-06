package yamltransformer;


import org.yaml.snakeyaml.Yaml;

public class YamlProvider {

    private static final ThreadLocal<Yaml> threadLocalYaml = new ThreadLocal<Yaml>() {
        @Override
        protected Yaml initialValue() {
            return new Yaml();
        }
    };

    public static Yaml get() {
        return threadLocalYaml.get();
    }

    public static void set(Yaml yaml) {
        threadLocalYaml.set(yaml);
    }

}
