package com.dliu.java8;

import com.dliu.model.InMemoryModel;
import com.dliu.model.Model;
import com.dliu.model.Post;
import com.dliu.utils.InMemoryModule;
import com.dliu.utils.Sql2oModule;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class BlogApplication {
  private final static Logger LOG = LoggerFactory.getLogger(BlogApplication.class.getCanonicalName());
  private static final int HTTP_BAD_REQUEST = 400;
  Model model;

  @Inject
  public BlogApplication(Model model) {
    this.model = model;
  }

  @Data
  static class NewPostPayload {
    private String title;
    private List categories = new LinkedList();
    private String content;

    public boolean isValid() {
      return title != null && !title.isEmpty() && !categories.isEmpty();
    }
  }


  @Data
  static class NewCommentPayload {
    private String author;
    private String content;
    public boolean isValid() { return author != null && !author.isEmpty() && content != null && !content.isEmpty();}
  }


  public static String dataToJson(Object data) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      StringWriter sw = new StringWriter();
      mapper.writeValue(sw, data);
      return sw.toString();
    } catch (IOException e) {
      throw new RuntimeException("IOException from a StringWriter?");
    }
  }

  void run(final int port) {
    port(port);
    LOG.info("port {}", port);
    get("/posts", (request, response)-> {
      response.status(200);
      response.type("application/json");
      return dataToJson(model.getAllPosts());
    });

    post("/posts", (request, response) -> {
      try {
        ObjectMapper mapper = new ObjectMapper();
        NewPostPayload creation = mapper.readValue(request.body(), NewPostPayload.class);
        if (!creation.isValid()) {
          response.status(HTTP_BAD_REQUEST);
          return "";
        }
        UUID uuid = model.createPost(creation.getTitle(), creation.getContent(), creation.getCategories());
        response.status(200);
        response.type("application/json");
        return uuid;
      } catch (JsonParseException jpe) {
        response.status(HTTP_BAD_REQUEST);
        return "";
      }
    });

    get("/posts/:uuid",  (request, response) -> {
      UUID uuid = UUID.fromString(request.params(":uuid"));
      if (model.existPost(uuid)) {
        Post post = model.getPost(uuid);
        response.status(200);
        response.type("application/json");
        return dataToJson(post);
      } else {
        response.status(400);
        return "";
      }
    });

    post("/posts/:uuid/comment", (request, response) -> {
      // get payload
      // check post existing
      // create comment
      // return uuid;
      // process exception

      try {
        ObjectMapper mapper = new ObjectMapper();
        NewCommentPayload creation = mapper.readValue(request.body(), NewCommentPayload.class);
        if (!creation.isValid()) {
          response.status(HTTP_BAD_REQUEST);
          return "";
        }

        UUID post = UUID.fromString(request.params(":uuid"));

        if (!model.existPost(post)) {
          response.status(HTTP_BAD_REQUEST);
          return "Post not existing";
        }

        UUID uuid = model.createComment(post, creation.getAuthor(), creation.getContent());
        response.status(200);
//        response.type("application/json");
        return uuid;
      } catch ( Exception e ) {
        System.out.println(e);
        response.status(HTTP_BAD_REQUEST);
        return "" + e.getMessage();
      }
    });
  }

  public static void main(String[] args) {
//    Injector injector = Guice.createInjector(new InMemoryModule());
    Injector injector = Guice.createInjector(new Sql2oModule());
    BlogApplication application = injector.getInstance(BlogApplication.class);
    application.run(4567);
  }
}
