package org.example.shell;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadFile {
  public static String read(String filePath) throws URISyntaxException, IOException {
    Path path = Paths.get(Objects.requireNonNull(ReadFile.class.getClassLoader()
        .getResource(filePath)).toURI());

    Stream<String> lines = Files.lines(path);
    String data = lines.collect(Collectors.joining("\n"));
    lines.close();

    return data.trim();
  }
}
