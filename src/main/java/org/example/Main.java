package org.example;

import org.example.core.day1.CalibrationValues;
import org.example.shell.ReadFile;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
  public static void main(String[] args) {

    try {
      var content = ReadFile.read("day1.txt");
      var result = CalibrationValues.getSum(content);
      System.out.println(result);
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}