package com.dliu.model;

import com.dliu.utils.RandomUuidGenerator;
import com.dliu.utils.UuidGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class InMemoryModel implements Model {
  private Map<UUID, Post> posts = new HashMap();
  private Map<UUID, Comment> comments = new HashMap();
  private UuidGenerator uuidGenerator;

  public InMemoryModel() {
    uuidGenerator = new RandomUuidGenerator();
  }

  @Override
  public UUID createPost(String title, String content, List<String> categories) {
    UUID uuid = uuidGenerator.generate();
    Post post = new Post();
    post.setPost_uuid(uuid);
    post.setTitle(title);
    post.setContent(content);
    post.setCategories(categories);
    posts.put(uuid, post);
    return uuid;
  }

  @Override
  public UUID createComment(UUID post, String author, String content) {
    UUID uuid = uuidGenerator.generate();
    Comment comment = new Comment();
    comment.setComment_uuid(uuid);
    comment.setPost_uuid(post);
    comment.setAuthor(author);
    comment.setContent(content);
    comments.put(uuid, comment);
    return uuid;
  }

  @Override
  public List getAllPosts() {
    return (List) posts.keySet().stream().sorted().map((uuid) -> posts.get(uuid)).collect(Collectors.toList());
  }

  @Override
  public List getAllCommentsOn(UUID post) {
    return null;
  }

  @Override
  public boolean existPost(UUID post) {
    return posts.get(post) != null;
  }

  @Override
  public Post getPost(UUID post) {
    return posts.get(post);
  }
}
