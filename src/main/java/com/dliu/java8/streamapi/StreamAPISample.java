package com.dliu.java8.streamapi;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamAPISample {
  private static  final String RED = "RED";
  private static  final String BLUE = "BLUE";

  @Data
  private static class  Shape {
    String color;
  }

  public static void main(String[] args) {
    List<Shape> shapes = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Shape shape = new Shape();
      shapes.add(shape);
    }

    // set colors
    shapes.forEach(shape -> shape.setColor(RED));

    shapes.get(1).setColor(BLUE);
    shapes.get(5).setColor(BLUE);
    shapes.get(6).setColor(BLUE);

    // get colors
//    shapes.forEach(shape -> System.out.println(shape.getColor()));

    // stream
    shapes.stream().filter(shape -> shape.getColor().equals(BLUE))
        .map(shape -> shape.getColor())
        .forEach(System.out::println);

    List<Shape> shapeList = shapes.stream()
        .filter(shape -> shape.getColor().equals(BLUE))
        .collect(Collectors.toList());

    shapeList.forEach(shape -> System.out.println("In Shape list: " + shape.getColor()));
  }
}
