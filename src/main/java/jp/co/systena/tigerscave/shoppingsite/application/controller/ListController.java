package jp.co.systena.tigerscave.shoppingsite.application.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import jp.co.systena.tigerscave.shoppingsite.application.model.Item;
import jp.co.systena.tigerscave.shoppingsite.application.model.ListForm;
import jp.co.systena.tigerscave.shoppingsite.application.model.ListService;
import jp.co.systena.tigerscave.shoppingsite.application.model.PurchaseHistoryList;

@Controller
public class ListController {

  @Autowired
  HttpSession session;
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  private ListService listService;

  @RequestMapping(value = "/ListView", method = RequestMethod.GET) // URLとのマッピング
  public ModelAndView show(ModelAndView mav) {

 // Viewに渡すデータを設定
    ListForm listForm = (ListForm) session.getAttribute("form");
    session.removeAttribute("form");

    if (listForm != null) {
      HashMap order = listForm.getOrder();
      if (order.get("name") != null && !(order.get("num").equals(0))) {
        mav.addObject("message", order.get("name") +"を"+ order.get("num") +"個カートに追加しました。");
      }
    }
    mav.addObject("listForm", new ListForm());  // 新規クラスを設定

    List<Item> itemList = listService.getItemList();
    mav.addObject("itemList", itemList);

    List cart = (List) session.getAttribute("cart");
    if( cart == null) {
        cart = new ArrayList();
        session.setAttribute("cart", cart);
    }

    HashMap totalItem = (HashMap) session.getAttribute("totalItem");

    mav.addObject("cart", cart);
    mav.addObject("totalItem", totalItem);

    BindingResult bindingResult = (BindingResult) session.getAttribute("result");
    if (bindingResult != null) {
      mav.addObject("bindingResult", bindingResult);
    }

    mav.setViewName("ListView");
    return mav; // 文字列を返却

  }

  @RequestMapping(value="/ListView", method = RequestMethod.POST)  // URLとのマッピング
  private ModelAndView order(ModelAndView mav, @Valid ListForm listForm, BindingResult bindingResult, HttpServletRequest request) {

    List cart = (List)session.getAttribute("cart");
    if( cart == null) {
        cart = new ArrayList();
    }

    List<Item> itemList = listService.getItemList();
    mav.addObject("itemList", itemList);

    String name = request.getParameter("name");
    String numStr = request.getParameter("num");
    int num = Integer.parseInt(numStr);

    listForm.setOrder(name, num);

    HashMap order = listForm.getOrder();

    cart = listService.addCart(cart, order);

    HashMap totalItem = listService.getTotal(cart);

    mav.addObject(cart);
    mav.addObject(totalItem);

    // データをセッションへ保存
    session.setAttribute("form", listForm);
    session.setAttribute("cart", cart);
    session.setAttribute("totalItem", totalItem);
    return new ModelAndView("redirect:/ListView");        // リダイレクト
  }

  @RequestMapping(value = "/purchaseHistory", method = RequestMethod.GET) // URLとのマッピング
  public ModelAndView showPurchaseHistory(ModelAndView mav) {

 // Viewに渡すデータを設定
    ListForm listForm = (ListForm) session.getAttribute("form");
    session.removeAttribute("form");

    mav.addObject("listForm", new ListForm());
    List<PurchaseHistoryList> purchaseHistoryList = listService.getPurchaseHistory();
    mav.addObject("purchaseHistoryList", purchaseHistoryList);

    BindingResult bindingResult = (BindingResult) session.getAttribute("result");
    if (bindingResult != null) {
      mav.addObject("bindingResult", bindingResult);
    }

    mav.setViewName("purchaseHistory");
    return mav; // 文字列を返却

  }

  @RequestMapping(value="/purchaseHistory", method = RequestMethod.POST)  // URLとのマッピング
  private ModelAndView orde(ModelAndView mav, @Valid ListForm listForm, BindingResult bindingResult, HttpServletRequest request) {


    return new ModelAndView("redirect:/purchaseHistory");        // リダイレクト
  }

  @RequestMapping(value="/back", params="back", method = RequestMethod.POST)
  private ModelAndView back(ModelAndView mav, @Valid ListForm listForm, BindingResult bindingResult, HttpServletRequest request) {

    List cart = (List)session.getAttribute("cart");
    if( cart == null) {
        cart = new ArrayList();
    }

    List<Item> itemList = listService.getItemList();
    mav.addObject("itemList", itemList);

    HashMap totalItem = (HashMap) session.getAttribute("totalItem");
    if(totalItem != null) {
      mav.addObject(totalItem);
    }

    mav.addObject(cart);

    // データをセッションへ保存
    session.setAttribute("form", listForm);
    session.setAttribute("cart", cart);
    session.setAttribute("totalItem", totalItem);
    return new ModelAndView("redirect:/ListView");        // リダイレクト
  }

  @RequestMapping(value = "/purchase", method = RequestMethod.POST) // URLとのマッピング
  public String purchase(Model model) {

    List cart = (List)session.getAttribute("cart");
    if (cart != null) {
      // 購入履歴へデータ登録
      listService.insert(cart);
    }

    HashMap totalItem = (HashMap) session.getAttribute("totalItem");

    cart.clear();
    totalItem.clear();

    session.setAttribute("cart", cart);
    session.setAttribute("totalItem", totalItem);

    return "purchaseComplete";

  }
}
