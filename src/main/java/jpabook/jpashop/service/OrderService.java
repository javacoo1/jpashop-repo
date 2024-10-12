package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.reopsitory.ItemRepository;
import jpabook.jpashop.reopsitory.MemberRepository;
import jpabook.jpashop.reopsitory.OrderRepository;
import jpabook.jpashop.reopsitory.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 注文
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //entity照会
        Member member = memberRepository.findById(memberId).get();
        Item item = itemRepository.findOne(itemId);

        //配送情報生成
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //注文商品生成
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //注文生成
        Order order = Order.createOrder(member, delivery, orderItem);

        //注文保存
        orderRepository.save(order);
        return order.getId();
    }
    /**
     * 注文キャンセル
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //注文entity照会
        Order order = orderRepository.findOne(orderId);
        //注文キャンセル
        order.cancel();
    }

    //検索
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
}
