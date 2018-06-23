package jp.co.systena.tigerscave.shoppingsite.application.model;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListService {

  @Autowired
  JdbcTemplate jdbcTemplate;

  // 商品一覧
  private List<Item> itemList;

  // 商品一覧取得
  public List<Item> getItemList() {
    this.itemList = jdbcTemplate.query("SELECT * FROM item_list_tbl ORDER BY item_id", new BeanPropertyRowMapper<Item>(Item.class));

    return this.itemList;
  }

  // 購入履歴一覧取得
  public List<PurchaseHistoryList> getPurchaseHistory() {
    List<PurchaseHistoryList> purchaseHistoryList = jdbcTemplate.query("SELECT * FROM purchase_history_tbl ORDER BY purchase_id", new BeanPropertyRowMapper<PurchaseHistoryList>(PurchaseHistoryList.class));

    return purchaseHistoryList;
  }

  //購入履歴登録
  @Transactional
  public void insert(List cart) {
    for (int i = 0; i < cart.size(); i++) {
      HashMap cartItem = (HashMap) cart.get(i);
      int insertCount = jdbcTemplate.update(
          "INSERT INTO purchase_history_tbl(item_name, item_num, purchase_price) VALUES( ?, ?, ? )",
          cartItem.get("name"),
          (Integer)cartItem.get("num"),
          (Integer)cartItem.get("price")
        );
    }
  }

  public List addCart(List cart, HashMap order) {

    Boolean addFlg = true;
    String name = (String) order.get("name");
    Integer num = (int)order.get("num");
    Integer price = 0;

    for (int i = 0; i < itemList.size(); i++) {
      Item item = itemList.get(i);
      String itemNm = (String) item.getItemNm();
      if (name.equals(itemNm)) {
        Integer itemPrice = item.getPrice();
        price = itemPrice * num;
      }
    }

    for (int i = 0; i < cart.size(); i++) {
      HashMap cartItem = (HashMap) cart.get(i);
      String cartItemNm = (String) cartItem.get("name");
      if (name.equals(cartItemNm)) {
        Integer cartItemNum = (int)cartItem.get("num");
        Integer cartItemPrice = (int) cartItem.get("price");
        Integer addItemNum = num + cartItemNum;
        Integer addItemPrice = cartItemPrice + price;
        HashMap addCartItem = new HashMap();
        addCartItem.put("name", name);
        addCartItem.put("num", addItemNum);
        addCartItem.put("price", addItemPrice);
        cart.set(i, addCartItem);
        addFlg = false;

        break;
      }
    }

    if (addFlg == true && !(num.equals(0))) {
      order.put("price", price);
      cart.add(order);
    }

    return cart;
  }

  public HashMap getTotal(List cart) {
    HashMap totalItem = new HashMap();
    Integer totalItemNum = 0;
    Integer totalItemPrice = 0;
    Integer cartSize = cart.size();

    if (!(cartSize.equals(0))) {
      for (int i = 0; i < cartSize; i++) {
        HashMap cartItem = (HashMap) cart.get(i);
        int cartItemNum = (int) cartItem.get("num");
        int cartItemPrice = (int) cartItem.get("price");

        totalItemNum += cartItemNum;
        totalItemPrice += cartItemPrice;
      }

      totalItem.put("name", "合計");
      totalItem.put("num", totalItemNum);
      totalItem.put("price", totalItemPrice);
    }

    return totalItem;
  }
}
