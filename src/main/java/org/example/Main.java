package org.example;

import org.example.core.day6.BoatRaces;
import org.example.shell.ReadFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
  static Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {

    try {
      var content = ReadFile.read("day6.txt");
      var result = BoatRaces.numberWaysToBeatRecordLongRace(content);
      logger.log(Level.INFO,"{0}", result);
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}