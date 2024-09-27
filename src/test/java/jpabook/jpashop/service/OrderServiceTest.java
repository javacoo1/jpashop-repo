package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.reopsitory.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 商品注文() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook("javacoo's story", 1000, 100);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), 2);
        Order getOrder = orderRepository.findOne(orderId);

        //then
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "商品を注文したらステータスはORDER");
        assertEquals(getOrder.getOrderItems().size(), 1, "注文した商品の種類");
        assertEquals(getOrder.getTotalPrice(), 1000 * orderCount, "注文価格＝商品価格　*　数量");
        assertEquals(book.getStockQuantity(), 98, "注文量と同じく在庫が減る。");
    }


    @Test
    public void 商品注文_在庫量_オーバ() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("javacoo's history", 1000, 10);

        int orderCount = 11;

        //when //then
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), orderCount);
        });
    }

    @Test
    public void 注文キャンセル() throws Exception {
        //given
        Member member = createMember();
        Book item = createBook("javacoo's fairy tale", 1200, 5);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);
        Order getOrder = orderRepository.findOne(orderId);

        //then
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(),"注文キャンセルの後、ステータスはCANCEL");
        assertEquals(5, item.getStockQuantity(),"キャンセルされた商品の在庫量は以前と同じ");
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("会員１");
        member.setAddress(new Address("東京", "新宿", "123-123"));
        em.persist(member);
        return member;
    }
}