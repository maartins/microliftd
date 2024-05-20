package example.test;

import io.micronaut.context.annotation.Value;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

@Singleton
public class SupaSecrets {

    @Value("${supa.secret.path:none}")
    private String SECRET_PATH;

    private final static String FILE_NAME = "tokens.txt";
    private final static HashMap<String, String> SECRET_MAP = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            FileReader input = new FileReader(SECRET_PATH + FILE_NAME);
            BufferedReader reader = new BufferedReader(input);
            String line;
            while((line = reader.readLine()) != null) {
                String[] pair = line.split("=");
                SECRET_MAP.put(pair[0], pair[1]);
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String key) {
        return SECRET_MAP.get(key);
    }
}
