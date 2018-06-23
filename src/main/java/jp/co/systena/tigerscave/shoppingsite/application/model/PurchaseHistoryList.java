package jp.co.systena.tigerscave.shoppingsite.application.model;

import javax.validation.constraints.Pattern;

public class PurchaseHistoryList {
  @Pattern(regexp="^[0-9]*$")
  private Integer purchaseId;
  public Integer getPurchaseId() {
    return purchaseId;
  }
  public void setPurchaseId(Integer purchaseId) {
    this.purchaseId = purchaseId;
  }
  public String getItemName() {
    return itemName;
  }
  public void setItemName(String itemName) {
    this.itemName = itemName;
  }
  public Integer getItemNum() {
    return itemNum;
  }
  public void setItemNum(Integer itemNum) {
    this.itemNum = itemNum;
  }
  public Integer getPurchasePrice() {
    return purchasePrice;
  }
  public void setPurchasePrice(Integer purchasePrice) {
    this.purchasePrice = purchasePrice;
  }
  public String getPurchaseDate() {
    return purchaseDate;
  }
  public void setPurchaseDate(String purchaseDate) {
    this.purchaseDate = purchaseDate;
  }
  private String itemName;
  @Pattern(regexp="^[0-9]*$")
  private Integer purchasePrice;
  @Pattern(regexp="^[0-9]*$")
  private Integer itemNum;
  private String purchaseDate;
}
