package jp.co.systena.tigerscave.shoppingsite.application.model;

import java.util.HashMap;

public class ListForm {
  private HashMap order = new HashMap();

  public HashMap getOrder() {
    return this.order;
  }
  public void setOrder(String name, int num) {
    this.order.put("name", name);
    this.order.put("num", num);
  }
}
