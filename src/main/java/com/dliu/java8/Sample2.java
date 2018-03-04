package com.dliu.java8;


interface Fly {
  default public void takeOff() {
    System.out.println("Fly::takeOff");
  };
  default public void turn () {
    System.out.println("Fly::turn");
  };
  default public void cruise() {
    System.out.println("Fly::cruise");
  };
  default public void land() {
    System.out.println("Fly::land");
  };
}


interface FastFly extends  Fly{
  default public void takeOff() {
    System.out.println("FastFly::takeOff");
  }
}

interface Sail {
  default public void cruise() {
    System.out.println("Sail::cruise");
  }
}

class Viechle {
  public void land() {
    System.out.println("Viechle::land");
  }
}



class SeaPlane extends Viechle implements FastFly, Sail{
  @Override
  public void cruise() {
    System.out.println("Viehcle::cruise");
    FastFly.super.cruise();
  }
}

public class Sample2 {
  public static void main(String[] args) {
    SeaPlane seaPlane = new SeaPlane();
    seaPlane.takeOff();

    seaPlane.land();
  }
}
