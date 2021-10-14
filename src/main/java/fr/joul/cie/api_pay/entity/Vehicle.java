package fr.joul.cie.api_pay.entity;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.StringJoiner;

@DataObject(generateConverter = true)
public class Vehicle {

  private String name;
  private String style;
  private int price;

  public Vehicle() {
  }

  public Vehicle(JsonObject jsonObject) {
    VehicleConverter.fromJson(jsonObject, this);
    System.out.println("Vehicle : " + Thread.currentThread().getId());
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    VehicleConverter.toJson(this, json);
    System.out.println("toJson : " + Thread.currentThread().getId());
    return json;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Vehicle name(String name) {
    this.name = name;
    return this;
  }

  public String getStyle() {
    return style;
  }

  public void setStyle(String style) {
    this.style = style;
  }

  public Vehicle style(String style) {
    this.style = style;
    return this;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public Vehicle price(int price) {
    this.price = price;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(" | ", Vehicle.class.getSimpleName() + "[", "]")
      .add("name='" + name + "'")
      .add("style='" + style + "'")
      .add("price=" + price)
      .toString();
  }
}
