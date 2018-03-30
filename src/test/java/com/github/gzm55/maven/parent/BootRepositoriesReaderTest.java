package com.github.gzm55.maven.parent;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
//import junit.framework.TestCase;

/**
 * Tests {@code BootRepositoriesReader}.
 */
public class BootRepositoriesReaderTest
{
  @DisplayName("test null input")
  @Test
  void testNullInput() {
    final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> BootRepositoriesReader.read(null));
    assertEquals("input file missing", e.getMessage());
    //try {
      //BootRepositoriesReader.read(null);
      //fail("should throw exception");
    //} catch (final IllegalArgumentException e) {
      //assertEquals("get expected exception cause", "input file missing", e.getMessage());
    //} catch (final Throwable e) {
      //fail("get unexcepted exception: " + e);
    //}
  }
}
