package com.dliu.java8;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Sample {
  public static void main(String[] args) {
    List<Integer> values = Arrays.asList(1, 2,3,4,5,6);
//    for(int i = 0; i< values.size(); i++) {
//      System.out.println(i);
//    }
//
//    for(int e : values) {
//      System.out.println(e);
//    }
//
//    values.forEach(new Consumer<Integer>() {
//      @Override
//      public void accept(Integer integer) {
//        System.out.println(integer);
//      }
//    });

//    values.forEach((Integer value) -> System.out.println(value));

//      values.forEach(value -> System.out.println(value));
//      values.forEach(System.out::println);
    System.out.println( values.stream()
          .map(v -> v * 2)
          .reduce(0, (c, e) -> c + e));
  }
}
