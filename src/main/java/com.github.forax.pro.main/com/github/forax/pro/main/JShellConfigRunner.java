package com.github.forax.pro.main;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.forax.pro.main.runner.ConfigRunner;

public class JShellConfigRunner implements ConfigRunner {
  @Override
  public Optional<Runnable> accept(Path configFile, String[] arguments) {
    return Optional.<Runnable>of(() -> run(configFile, arguments))
        .filter(__ -> configFile.toString().endsWith(".pro"));
  }
  
  private static void run(Path configFile, String... arguments) {
    //System.out.println("run with jshell " + configFile);
    
    var args =
      Stream.of(
        Stream.of("-R-Dpro.exitOnError=false"),
        Stream.of("-R-XX:+EnableValhalla").filter(__ -> System.getProperty("valhalla.enableValhalla") != null),
        Stream.of(arguments).filter(a -> a.length() != 0).map(a -> "-R-Dpro.arguments=" + String.join(",", a)),
        Stream.of(configFile.toString())
        )
      .flatMap(s -> s)
      .toArray(String[]::new);
    
    int exitCode = JShellWrapper.run(System.in, System.out, System.err, args);
    System.exit(exitCode);
  }
}
