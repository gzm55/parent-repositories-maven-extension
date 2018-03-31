package com.github.gzm55.maven.parent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.ByteArrayInputStream;

import java.util.List;
import java.util.Collections;

import org.apache.maven.model.Repository;
import org.apache.maven.model.io.ModelReader;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

@Component( role = BootRepositoriesReader.class )
public class BootRepositoriesReader {
  private static final String PRE_XML  = "<project>" +
                                           "<modelVersion>4.0.0</modelVersion>" +
                                           "<groupId>fake-group</groupId>" +
                                           "<artifactId>fake-id</artifactId>" +
                                           "<version>0</version>";
  private static final String POST_XML = "</project>";


  @Requirement
  private ModelReader reader;


  public List<Repository> read(final File input)
      throws IOException
  {
    if ( input == null ) {
      throw new IllegalArgumentException("input file missing");
    }
    if (!input.isFile()) {
      return Collections.emptyList();
    }

    final InputStream preModeStream  = new ByteArrayInputStream(PRE_XML.getBytes("UTF-8"));
    final InputStream fileStream     = new FileInputStream(input);
    final InputStream postModeStream = new ByteArrayInputStream(POST_XML.getBytes("UTF-8"));
    final InputStream inputStream =
      new SequenceInputStream(preModeStream, new SequenceInputStream(fileStream, postModeStream));

    final List<Repository> result = reader.read(inputStream, null).getRepositories();
    return result;
  }
}
