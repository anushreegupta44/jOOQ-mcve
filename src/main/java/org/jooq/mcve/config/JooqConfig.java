package org.jooq.mcve.config;

import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

  @Bean
  public Settings settings() {
    return new Settings()
        .withRenderQuotedNames(RenderQuotedNames.NEVER)
        .withRenderNameCase(RenderNameCase.LOWER);
  }
}
