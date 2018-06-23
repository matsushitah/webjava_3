package jp.co.systena.tigerscave.shoppingsite.application.model;

import javax.validation.constraints.Pattern;

public class Item {
  @Pattern(regexp="^[0-9]*$")
  private Integer itemId;
  public Integer getItemId() {
    return itemId;
  }
  public void setItemId(Integer itemId) {
    this.itemId = itemId;
  }
  public String getItemNm() {
    return itemNm;
  }
  public void setItemNm(String itemNm) {
    this.itemNm = itemNm;
  }
  public Integer getPrice() {
    return price;
  }
  public void setPrice(Integer price) {
    this.price = price;
  }
  private String itemNm;
  @Pattern(regexp="^[0-9]*$")
  private Integer price;
}
