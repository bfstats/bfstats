package io.github.bfvstats;

import com.google.inject.AbstractModule;
import io.github.bfvstats.service.*;

public class InjectionModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(BasicApplication.class).asEagerSingleton();
    bind(ChatService.class).asEagerSingleton();
    bind(MapService.class).asEagerSingleton();
    bind(PlayerService.class).asEagerSingleton();
    bind(RankingService.class).asEagerSingleton();
    bind(RoundService.class).asEagerSingleton();
  }
}