package com.github.gzm55.maven.parent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.ByteArrayInputStream;

import java.util.List;

import org.apache.maven.model.Repository;
import org.apache.maven.model.io.ModelReader;

import org.codehaus.plexus.component.annotations.Requirement;

public class BootRepositoriesReader {
  private static final String XML_FILE = null;
  private static final String PRE_XML  = "<project>" +
                                           "<modelVersion>4.0.0</modelVersion>" +
                                           "<groupId>fake-group</groupId>" +
                                           "<artifactId>fake-id</artifactId>" +
                                           "<version>0</version>";
  private static final String POST_XML = "</project>";


  @Requirement
  private static ModelReader reader;


  public static List<Repository> read(final File input)
      throws IOException
  {
    if ( input == null ) {
      throw new IllegalArgumentException("input file missing");
    }

    final InputStream preModeStream  = new ByteArrayInputStream(PRE_XML.getBytes("UTF-8"));
    final InputStream fileStream     = new FileInputStream(input);
    final InputStream postModeStream = new ByteArrayInputStream(POST_XML.getBytes("UTF-8"));
    final InputStream inputStream =
      new SequenceInputStream(preModeStream, new SequenceInputStream(fileStream, postModeStream));

    return reader.read(inputStream, null).getRepositories();
  }
}
