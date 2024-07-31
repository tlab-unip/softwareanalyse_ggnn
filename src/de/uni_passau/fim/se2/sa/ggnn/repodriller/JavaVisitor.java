package de.uni_passau.fim.se2.sa.ggnn.repodriller;

import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

public class JavaVisitor implements CommitVisitor {

    @Override
    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism writer) {
        // TODO Implement me
        for (var m : commit.getModifications()) {
            if (!m.fileNameEndsWith("java")) {
                continue;
            }
            if (writer instanceof JavaWriter javaWriter) {
                var fullFileName = m.getFileName();
                var fileName = fullFileName.substring(fullFileName.lastIndexOf("/") + 1);
                javaWriter.setFileName(fileName);
            }
            writer.write(m.getSourceCode());
        }
    }
}
