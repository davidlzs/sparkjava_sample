package com.dliu.java8;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Java8Tester {
  public static void main(String[] args) {
    List<String> names1 = new ArrayList<>();
    names1.add("David");
    names1.add("Susie");
    names1.add("Felix");
    names1.add("Ford");
    names1.add("Jeep");

    List<String> names2 = new ArrayList<>();
    names2.add("David");
    names2.add("Susie");
    names2.add("Felix");
    names2.add("Ford");
    names2.add("Jeep");

    Java8Tester tester = new Java8Tester();
    System.out.println("sorting java 8");
    sortInJava8(names1);
    System.out.println(names1);
  }

  public static void sortInJava8(List<String> names) {
    Collections.sort(names, (s1, s2) -> s1.compareTo(s2));
  }
}
