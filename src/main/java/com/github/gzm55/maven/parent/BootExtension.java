package com.github.gzm55.maven.parent;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Named;
import javax.inject.Inject;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.InvalidRepositoryException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.model.Repository;
import org.apache.maven.project.ProjectBuildingHelper;
import org.apache.maven.project.ProjectBuildingRequest;


/**
 * Inject parents bootstrap repositories into the current maven session.
 */
@Named("parent-repo")
public class BootExtension extends AbstractMavenLifecycleParticipant {
  @Inject
  BootRepositoriesReader reader;

  @Inject
  ProjectBuildingHelper buildHelper;

  private static final String REPOSITORIES_FILENAME = ".mvn/parent-repositories.xml";

  @Override
  public void afterSessionStart(final MavenSession session) throws MavenExecutionException {
    if (null == session) {
      return;
    }
    final MavenExecutionRequest request = session.getRequest();
    if (null == request) {
      return;
    }
    final ProjectBuildingRequest buildRequest = session.getProjectBuildingRequest();
    if (null == buildRequest) {
      return;
    }
    final File basedir = request.getMultiModuleProjectDirectory();
    if (null == basedir) {
      return;
    }

    try {
      final List<Repository> pomBootRepos = reader.read(new File(basedir, REPOSITORIES_FILENAME));
      if (pomBootRepos.isEmpty()) {
        return;
      }
      final List<ArtifactRepository> bootRepos =
          buildHelper.createArtifactRepositories(pomBootRepos, buildRequest.getRemoteRepositories(), buildRequest);

      buildRequest.setRemoteRepositories(bootRepos);
    } catch (final IOException e) {
      throw new MavenExecutionException(e.getMessage(), e);
    } catch (final InvalidRepositoryException e) {
      throw new MavenExecutionException("repository [" + e.getRepositoryId() + "] is invalid.", e);
    }
  }
}
