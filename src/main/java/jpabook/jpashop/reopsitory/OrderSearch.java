package jpabook.jpashop.reopsitory;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

    private String memberName;  //会員名
    private OrderStatus orderStatus;  //注文ステータス[ORDER, CANCEL]
}
