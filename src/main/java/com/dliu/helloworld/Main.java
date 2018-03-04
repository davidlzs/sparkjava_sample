package com.dliu.helloworld;

import static spark.Spark.get;

public class Main {

  public static void main(String[] args) {
    get("/hello", (req, res) ->  {
      System.out.println("hello");
      return "Hello World";
    });
  }
}
