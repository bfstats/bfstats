package io.github.bfvstats;

import com.google.inject.AbstractModule;
import io.github.bfvstats.service.PlayerService;
import io.github.bfvstats.service.DatabasePlayerService;
import ro.pippo.core.Application;

public class InjectionModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(PlayerService.class).to(DatabasePlayerService.class).asEagerSingleton();

    bind(Application.class).to(BasicApplication.class).asEagerSingleton();
  }

}