package de.uni_passau.fim.se2.sa.ggnn.repodriller;

import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.commit.OnlyModificationsWithFileTypes;
import org.repodriller.filter.range.Commits;
import org.repodriller.scm.GitRemoteRepository;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class GGNNStudy implements Study {

    private final List<String> repos;
    private final List<String> commits;
    private final Path outputDirectory;

    public GGNNStudy(List<String> repos, List<String> commits, Path outputDirectory) {
        this.repos = repos;
        this.commits = commits;
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void execute() {
        // TODO Implement me
        try {
            new RepositoryMining()
                    .in(GitRemoteRepository.allProjectsIn(repos))
                    .through(Commits.list(commits))
                    .process(new JavaVisitor(), new JavaWriter(outputDirectory))
                    .filters(new OnlyModificationsWithFileTypes(Arrays.asList(".java")))
                    .mine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
