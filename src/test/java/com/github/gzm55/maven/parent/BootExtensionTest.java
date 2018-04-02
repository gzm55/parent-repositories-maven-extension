package com.github.gzm55.maven.parent;

import java.io.File;
import java.util.List;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.ContainerConfiguration;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.bridge.MavenRepositorySystem;

import com.github.gzm55.sisu.plexus.PlexusJUnit5TestCase;


/**
 * Tests {@code BootExtension}.
 */
public class BootExtensionTest extends PlexusJUnit5TestCase
{
  @Override
  protected void customizeContainerConfiguration(final ContainerConfiguration configuration)
  {
    // scan the maven jsr330 compontents
    configuration.setClassPathScanning(PlexusConstants.SCANNING_INDEX);
  }

  @Test
  void testInjectRepo() throws Exception {
    final MavenSession ms = createMavenSession();
    assertNotNull(ms);

    final List<ArtifactRepository> before = ms.getProjectBuildingRequest().getRemoteRepositories();
    assertNotNull(before);
    assertEquals(1, before.size());
    assertEquals("central", before.get(0).getId());

    // emulate run the extension
    final BootExtension ext = (BootExtension)lookup(AbstractMavenLifecycleParticipant.class, "parent-repo");
    assertNotNull(ext);
    ext.afterSessionStart(ms);

    final List<ArtifactRepository> after = ms.getProjectBuildingRequest().getRemoteRepositories();
    assertEquals(2, after.size());

    final HashSet<String> expectedIds = new HashSet<String>();
    expectedIds.add("central");
    expectedIds.add("test-repo");
    assertTrue(expectedIds.contains(after.get(0).getId()));
    assertTrue(expectedIds.contains(after.get(1).getId()));
  }

  @SuppressWarnings("deprecation")
  MavenSession createMavenSession() throws Exception {
    final MavenExecutionRequest request = new DefaultMavenExecutionRequest();
    MavenRepositorySystem repoSys = lookup(MavenRepositorySystem.class);
    assertNotNull(repoSys);
    assertNotNull(request);
    ArtifactRepository r = repoSys.createDefaultRemoteRepository(request);
    assertNotNull(r);
    request.addRemoteRepository(r);
    request.setMultiModuleProjectDirectory(new File(getClass().getClassLoader().getResource("dot-mvn").getFile()));
    return new MavenSession(getContainer(), null, request, null);
  }
}
