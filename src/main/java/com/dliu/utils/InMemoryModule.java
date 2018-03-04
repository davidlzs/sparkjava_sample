package com.dliu.utils;

import com.dliu.java8.BlogApplication;
import com.dliu.model.InMemoryModel;
import com.dliu.model.Model;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class InMemoryModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(BlogApplication.class).in(Singleton.class);
    bind(Model.class).to(InMemoryModel.class);
  }
}
