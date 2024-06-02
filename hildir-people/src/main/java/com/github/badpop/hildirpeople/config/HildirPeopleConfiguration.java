package com.github.badpop.hildirpeople.config;

import com.github.badpop.hildir.common.database.HikariConnectionPool;
import com.github.badpop.hildirpeople.TestCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HildirPeopleConfiguration {

  @Bean
  public TestCommand testCommand(HikariConnectionPool hikariConnectionPool) {
    return new TestCommand(hikariConnectionPool);
  }
}
