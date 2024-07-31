package de.uni_passau.fim.se2.sa.ggnn.repodriller;

import org.repodriller.persistence.PersistenceMechanism;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.stream.Stream;

public class JavaWriter implements PersistenceMechanism {

    private final Path directory;
    private String fileName;

    public JavaWriter(Path directory) {
        this.directory = directory;
    }

    @Override
    public synchronized void write(Object... objects) {
        // TODO Implement me
        try {
            var args = Stream.of(objects).toArray(String[]::new);
            var sourceCode = args[0];

            var file = directory.resolve(fileName).toFile();
            var writer = new BufferedWriter(new FileWriter(file));

            directory.toFile().mkdirs();
            writer.write(sourceCode);
            writer.newLine();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void close() {
        // TODO Implement me
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
