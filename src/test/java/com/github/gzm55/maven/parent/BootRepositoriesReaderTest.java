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

import org.codehaus.plexus.PlexusJUnit4TestCase;

/**
 * Tests {@code BootRepositoriesReader}.
 */
public class BootRepositoriesReaderTest extends PlexusJUnit4TestCase
{
  @BeforeEach
  public void beforeTest() throws Exception {
    super.beforeTest();
  }
  @AfterEach
  public void afterTest() throws Exception {
    super.afterTest();
  }

  @DisplayName("test null input")
  @Test
  void testNullInput() {
    final Throwable e = assertThrows(IllegalArgumentException.class, () -> new BootRepositoriesReader().read(null));
    assertEquals("input file missing", e.getMessage());
  }

  @DisplayName("test fake input")
  @Test
  void testFakeInput() throws IOException {
    final File file = new File(getClass().getClassLoader().getResource("empty.xml").getFile());
    final BootRepositoriesReader reader = lookup(BootRepositoriesReader.class);
    assertNotNull(reader);
    final List<Repository> l = reader.read(file);
    assertNotNull(l);
    assertIterableEquals(new ArrayList<Repository>(0), l);
  }
}
