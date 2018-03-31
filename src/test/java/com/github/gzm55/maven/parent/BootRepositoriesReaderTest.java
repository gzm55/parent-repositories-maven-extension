package com.github.gzm55.maven.parent;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import org.apache.maven.model.Repository;
import org.apache.maven.model.RepositoryPolicy;

import org.codehaus.plexus.PlexusJUnit4TestCase;

/**
 * Tests {@code BootRepositoriesReader}.
 */
public class BootRepositoriesReaderTest extends PlexusJUnit4TestCase
{
  @BeforeEach public void beforeTest() throws Exception { super.beforeTest(); }
  @AfterEach  public void afterTest()  throws Exception { super.afterTest(); }

  @DisplayName("test null input")
  @Test
  void testNullInput() {
    final Throwable e = assertThrows(IllegalArgumentException.class, () -> new BootRepositoriesReader().read(null));
    assertEquals("input file missing", e.getMessage());
  }

  @DisplayName("test fake input")
  @Test
  void testFakeInput() throws IOException {
    final File file = new File("fake.xml");

    final BootRepositoriesReader reader = lookup(BootRepositoriesReader.class);
    assertNotNull(reader);

    final List<Repository> l = reader.read(file);
    assertNotNull(l);

    assertEquals(0, l.size());
  }

  @DisplayName("test empty input")
  @Test
  void testEmptyInput() throws IOException {
    final File file = new File(getClass().getClassLoader().getResource("empty.xml").getFile());

    final BootRepositoriesReader reader = lookup(BootRepositoriesReader.class);
    assertNotNull(reader);

    final List<Repository> l = reader.read(file);
    assertNotNull(l);

    assertEquals(0, l.size());
  }

  @DisplayName("test normal input")
  @Test
  void testNormalInput() throws IOException {
    final File file = new File(getClass().getClassLoader().getResource("normal.xml").getFile());
    final List<Repository> l = lookup(BootRepositoriesReader.class).read(file);
    assertNotNull(l);
    assertEquals(3, l.size());

    /**
<repositories>
  <repository>
    <id>idA</id>
    <name>nameA</name>
    <url>urlA</url>
  </repository>
  <repository>
    <id>idB</id>
    <name>nameB</name>
    <url>urlB</url>
    <releases>
      <updatePolicy>never</updatePolicy>
    </releases>
    <snapshots>
      <enabled>false</enabled>
    </snapshots>
  </repository>
  <repository>
    <id>idC</id>
    <name>nameC</name>
    <url>urlC</url>
    <releases>
      <enabled>true</enabled>  
      <updatePolicy>always</updatePolicy>  
      <checksumPolicy>warn</checksumPolicy>
    </releases>
  </repository>
</repositories>
     */
    final ArrayList<Repository> expected = new ArrayList<Repository>(3);

    final Repository a = new Repository();
    a.setId("idA");
    a.setName("nameA");
    a.setUrl("urlA");

    final Repository b = new Repository();
    b.setId("idB");
    b.setName("nameB");
    b.setUrl("urlB");
    final RepositoryPolicy bRel = new RepositoryPolicy();
    bRel.setUpdatePolicy("never");
    final RepositoryPolicy bSnap = new RepositoryPolicy();
    bSnap.setEnabled("false");
    b.setReleases(bRel);
    b.setSnapshots(bSnap);

    final Repository c = new Repository();
    c.setId("idC");
    c.setName("nameC");
    c.setUrl("urlC");
    final RepositoryPolicy cRel = new RepositoryPolicy();
    cRel.setEnabled("true");
    cRel.setUpdatePolicy("always");
    cRel.setChecksumPolicy("warn");
    c.setReleases(cRel);

    assertRepositoryEquals(a, l.get(0));
    assertRepositoryEquals(b, l.get(1));
    assertRepositoryEquals(c, l.get(2));
  }

  private void assertRepositoryEquals(final Repository e, final Repository r) {
    assertEquals(e.getId(), r.getId());
    assertEquals(e.getName(), r.getName());
    assertEquals(e.getUrl(), r.getUrl());
    if (null == e.getReleases()) {
      assertNull(r.getReleases());
    } else {
      assertEquals(e.getReleases().getEnabled(), r.getReleases().getEnabled());
      assertEquals(e.getReleases().getUpdatePolicy(), r.getReleases().getUpdatePolicy());
      assertEquals(e.getReleases().getChecksumPolicy(), r.getReleases().getChecksumPolicy());
    }
    if (null == e.getSnapshots()) {
      assertNull(r.getSnapshots());
    } else {
      assertEquals(e.getSnapshots().getEnabled(), r.getSnapshots().getEnabled());
      assertEquals(e.getSnapshots().getUpdatePolicy(), r.getSnapshots().getUpdatePolicy());
      assertEquals(e.getSnapshots().getChecksumPolicy(), r.getSnapshots().getChecksumPolicy());
    }
  }
}
