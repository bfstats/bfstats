package io.github.bfstats;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.DelegatingLoader;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import io.github.bfstats.pebble.DurationExtension;
import io.github.bfstats.pebble.Java8Extension;
import ro.pippo.core.Application;
import ro.pippo.core.PippoConstants;
import ro.pippo.pebble.PebbleTemplateEngine;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EnhancedPebbleTemplateEngine extends PebbleTemplateEngine {
  @Override
  protected void init(Application application, PebbleEngine.Builder builder) {
    builder.extension(new Java8Extension());
    builder.extension(new DurationExtension());

    FileLoader fileLoader = new FileLoader();
    fileLoader.setCharset(PippoConstants.UTF8);

    if (application.getPippoSettings().isDev()) {
      List<Loader<?>> loaders = new ArrayList<>();

      String templatesAbsolutePath = Paths.get(".").toAbsolutePath().normalize()
          .resolve("src\\main\\resources\\templates")
          .normalize()
          .toString();
      fileLoader.setPrefix(templatesAbsolutePath);
      fileLoader.setSuffix("." + "peb");
      loaders.add(fileLoader);

      ClasspathLoader classpathLoader = new ClasspathLoader();
      classpathLoader.setCharset(PippoConstants.UTF8);
      classpathLoader.setPrefix("templates/");
      classpathLoader.setSuffix(".peb");
      loaders.add(classpathLoader);

      builder.loader(new DelegatingLoader(loaders));
    }
  }
}
