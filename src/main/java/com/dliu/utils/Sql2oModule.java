package com.dliu.utils;

import com.dliu.java8.BlogApplication;
import com.dliu.model.Model;
import com.dliu.model.Sql2oModel;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

public class Sql2oModule extends AbstractModule {
  private final static Logger LOG = LoggerFactory.getLogger(Sql2oModule.class.getCanonicalName());

  @Override
  protected void configure() {
    try {
      Properties props = new Properties();
      try (InputStream stream = this.getClass().getResourceAsStream("/db.properties")) {
        props.load(stream);
        Names.bindProperties(binder(), props);
      }
    } catch (IOException e) {
      LOG.error("Could not load config: ", e);
      System.exit(1);
    }
    bind(BlogApplication.class).in(Singleton.class);
    bind(UuidGenerator.class).to(RandomUuidGenerator.class);
    bind(Model.class).to(Sql2oModel.class);
  }

  @Provides
  Sql2o provideSql2o(@Named("dbHost") String dbHost,
                     @Named("dbPort") String dbPort,
                     @Named("database") String database,
                     @Named("dbUsername") String dbUsername,
                     @Named("dbPassword") String dbPassword) {

    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
    hikariConfig.addDataSourceProperty("serverName", dbHost);
    hikariConfig.addDataSourceProperty("portNumber", dbPort);
    hikariConfig.addDataSourceProperty("databaseName", database);
    hikariConfig.addDataSourceProperty("user", dbUsername);
    hikariConfig.addDataSourceProperty("password", dbPassword);
//    hikariConfig.setJdbcUrl("jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + database);
//    hikariConfig.setUsername(dbUsername);
//    hikariConfig.setPassword(dbPassword);

    DataSource dataSource = new HikariDataSource(hikariConfig);

    return new Sql2o(dataSource, new PostgresQuirks() {
      {
        // make sure we use default UUID converter.
        converters.put(UUID.class, new UUIDConverter());
      }
    });
  }
}
