package io.github.bfstats;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import io.github.bfstats.service.*;

public class InjectionModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(BasicApplication.class).in(Scopes.SINGLETON);
    bind(ChatService.class).in(Scopes.SINGLETON);
    bind(KitService.class).in(Scopes.SINGLETON);
    bind(MapService.class).in(Scopes.SINGLETON);
    bind(PlayerService.class).in(Scopes.SINGLETON);
    bind(RankingService.class).in(Scopes.SINGLETON);
    bind(RoundService.class).in(Scopes.SINGLETON);
    bind(VehicleService.class).in(Scopes.SINGLETON);
    bind(WeaponService.class).in(Scopes.SINGLETON);
  }
}
