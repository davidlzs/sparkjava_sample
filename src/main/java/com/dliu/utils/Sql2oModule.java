package com.dliu.utils;

import com.dliu.java8.BlogApplication;
import com.dliu.model.Model;
import com.dliu.model.Sql2oModel;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.util.UUID;

public class Sql2oModule extends AbstractModule {

  private String dbHost = "localhost";
  private String dbPort = "5432";
  private String database = "blog";
  private String dbUsername = "blog_owner";
  private String dbPassword = "sparkforthewin";

  @Override
  protected void configure() {
    bind(BlogApplication.class).in(Singleton.class);
    bind(UuidGenerator.class).to(RandomUuidGenerator.class);
    bind(Model.class).to(Sql2oModel.class);
  }

  @Provides
  Sql2o provideSql2o() {
    Sql2o sql2o = new Sql2o("jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + database +"?autocommit = false",
        dbUsername, dbPassword, new PostgresQuirks() {
      {
        // make sure we use default UUID converter.
        converters.put(UUID.class, new UUIDConverter());
      }
    });
    return sql2o;
  }
}
