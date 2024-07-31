package de.uni_passau.fim.se2.sa.ggnn.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import de.uni_passau.fim.se2.sa.ggnn.Main;
import picocli.CommandLine;

public class GGNNTest {

    @Test
    public void CommandMineTest(@TempDir Path outputPath) throws Exception {
        List<String> repos = List.of("https://github.com/mauricioaniche/repodriller");
        List<String> commits = List.of("17c29807e839b2c6d581ea54029b396b662e33a1");
        String[] argsMine = {
                "mine",
                "-r", repos.getFirst(),
                "-c", commits.getFirst(),
                "-o", outputPath.toString(),
        };
        int exitCode = new CommandLine(new Main()).execute(argsMine);
        assertEquals(exitCode, 0);

        var outputFiles = Files.list(outputPath)
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toSet());
        assertEquals(outputFiles, Set.of("CSVFile.java", "GitRepository.java", "PathUtils.java"));
    }

    @Test
    public void CommandGGNNTest(@TempDir Path inputPath, @TempDir Path outputPath) throws Exception {
        var inputFilePath = inputPath.resolve("Ternary.java");
        List<String> fileContent = Arrays.asList(
                "public class Ternary {",
                "int ternary(int x, int y) {",
                "return x >= y ? x : y;}}");
        Files.write(inputFilePath, fileContent);
        assertTrue(inputFilePath.toFile().exists());

        String[] argsGGNN = {
                "ggnn",
                "-s", inputFilePath.toString(),
                "-o", outputPath.toString(),
                "-d"
        };
        int exitCode = new CommandLine(new Main()).execute(argsGGNN);
        assertEquals(exitCode, 0);

        var outputFiles = Files.list(outputPath)
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toList());
        assertEquals(outputFiles, List.of("Ternary.dot"));
    }

}