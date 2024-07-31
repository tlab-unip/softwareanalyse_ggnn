package de.uni_passau.fim.se2.sa.ggnn.repodriller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class RepodrillerTest {

    @Test
    public void StudyTest(@TempDir Path outputPath) throws Exception {
        List<String> repos = List.of("https://github.com/mauricioaniche/repodriller");
        List<String> commits = List.of("44f3d1ba92f55133163c79639c68582e43a3117d");
        new GGNNStudy(repos, commits, outputPath).execute();

        var outputFiles = Files.list(outputPath)
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toSet());
        assertEquals(outputFiles, Set.of("GitRepository.java"));
    }

    @Test
    public void JavaWriterTest(@TempDir Path outputPath) throws Exception {
        String fileContent = "Example";
        String fileName = "javaWriterOutput.txt";
        var filePath = outputPath.resolve(fileName);

        var writer = new JavaWriter(outputPath);
        writer.setFileName(fileName);
        writer.write(fileContent);

        assertTrue(filePath.toFile().exists());
    }
}
