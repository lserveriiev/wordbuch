package de.wordbuch;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {

    private final HikariDataSource hikariDataSource;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<DictionaryDe> dictionaries = new ArrayList<>();

        try (InputStream stream = ApplicationRunner.class.getResourceAsStream("/words-de.txt");
             Reader reader = new InputStreamReader(stream);
             BufferedReader br = new BufferedReader(reader)
        ) {
            while (br.readLine() != null) {
                var name = br.readLine();
                if (name != null) {
                    var dictionary = new DictionaryDe(name);
                    dictionaries.add(dictionary);
                }
            }
        }

        saveAllJdbcBatch(dictionaries);
    }

    public void saveAllJdbcBatch(List<DictionaryDe> dictionaries) {
        String sql = String.format(
                "INSERT INTO %s (name) VALUES (?) ON CONFLICT DO NOTHING",
                DictionaryDe.class.getAnnotation(Table.class).name()
        );
        try (Connection connection = hikariDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            int counter = 0;
            for (DictionaryDe dictionary : dictionaries) {
                statement.clearParameters();
                statement.setString(1, dictionary.getName());
                statement.addBatch();
                if ((counter + 1) % batchSize == 0 || (counter + 1) == dictionaries.size()) {
                    statement.executeBatch();
                    statement.clearBatch();
                }
                counter++;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
