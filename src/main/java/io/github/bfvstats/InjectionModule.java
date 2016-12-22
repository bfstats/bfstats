package io.github.bfvstats;

import com.google.inject.AbstractModule;
import io.github.bfvstats.service.ContactService;
import io.github.bfvstats.service.DatabaseContractService;
import ro.pippo.core.Application;

public class InjectionModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ContactService.class).to(DatabaseContractService.class).asEagerSingleton();

    bind(Application.class).to(BasicApplication.class).asEagerSingleton();
  }

}