package org.example;

import org.example.core.day8.HauntedWasteland;
import org.example.shell.ReadFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
  static Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();
    try {
      var content = ReadFile.read("day8.txt");
      var result = HauntedWasteland.getStepsToReachAllNodesEndedByZ(content);
      logger.log(Level.INFO,"{0}", result);

      long endTime = System.currentTimeMillis();
      long duration = endTime - startTime;
      String message = MessageFormat.format("duration : {0}", duration);
      logger.info(message);
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }

  }
}