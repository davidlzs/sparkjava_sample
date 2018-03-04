package com.dliu.model;

import com.dliu.utils.UuidGenerator;
import com.google.inject.Inject;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Sql2oModel implements Model {
  private Sql2o sql2o;
  private UuidGenerator uuidGenerator;

  @Inject
  public Sql2oModel(Sql2o sql2o, UuidGenerator uuidGenerator) {
    this.sql2o = sql2o;
    this.uuidGenerator = uuidGenerator;
  }

  @Override
  public UUID createPost(String title, String content, List<String> categories) {
    try (Connection connection = sql2o.beginTransaction()) {
      UUID uuid = uuidGenerator.generate();
      connection.createQuery("INSERT into posts(post_uuid, title, content, publishing_date) " +
          "VALUES (:post_id, :title, :content, :date)")
          .addParameter("post_id", uuid)
          .addParameter("title", title)
          .addParameter("content", content)
          .addParameter("date", new Date())
          .executeUpdate();
      categories.forEach( (category) -> connection.createQuery("INSERT into posts_categories(post_uuid, category) VALUES (:post_uuid, :category)")
          .addParameter("post_uuid", uuid)
          .addParameter("category", category)
          .executeUpdate());

      connection.commit();
      return uuid;
    }
  }

  @Override
  public UUID createComment(UUID post, String author, String content) {
    try (Connection connection = sql2o.beginTransaction()) {
      UUID uuid = uuidGenerator.generate();
      connection.createQuery("INSERT into comments (comment_uuid, post_uuid, author, content, approved, submission_date) " +
        "VALUES (:comment_uuid, :post_uuid, :author, :content, :approved, :date)")
          .addParameter("comment_uuid", uuid)
          .addParameter("post_uuid", post)
          .addParameter("author", author)
          .addParameter("content", content)
          .addParameter("approved", false)
          .addParameter("date", new Date())
          .executeUpdate();
      connection.commit();
      return uuid;
    }
  }

  @Override
  public List getAllPosts() {
    try (Connection connection = sql2o.beginTransaction()) {
      List<Post> posts = connection.createQuery("SELECT * from posts")
          .executeAndFetch(Post.class);
      return posts;
    }
  }

  @Override
  public List getAllCommentsOn(UUID post) {
    return null;
  }

  @Override
  public boolean existPost(UUID post) {
    try (Connection connection = sql2o.beginTransaction()) {
      List<Post> posts = connection.createQuery("SELECT * from posts where post_uuid = :post")
          .addParameter("post", post)
          .executeAndFetch(Post.class);
      return posts.size() > 0;
    }
  }

  @Override
  public Post getPost(UUID post) {
    try (Connection connection = sql2o.beginTransaction()) {
      List<Post> posts = connection.createQuery("SELECT * from posts where post_uuid = :post")
          .addParameter("post", post)
          .executeAndFetch(Post.class);
      return posts.get(0);
    }
  }
}
