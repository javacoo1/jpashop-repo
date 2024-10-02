package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * total = 2
 * userA
 *    Javacoo's Book1
 *    Javacoo's Book2
 * userB
 *    Javacoo's NoteBook1
 *    Javacoo's NoteBook2
 */

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        
        public void dbInit1() {
            Member member = createMember("userA","北海道", "札幌", "111111");
            em.persist(member);

            Book book1 = createBook("Javacoo's Book1", 1000, 100);
            em.persist(book1);

            Book book2 = createBook("Javacoo's Book2", 2000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 1000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 2000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB","福岡", "博多", "222222");
            em.persist(member);

            Book book1 = createBook("Javacoo's NoteBook1", 2000, 200);
            em.persist(book1);

            Book book2 = createBook("Javacoo's NoteBook2", 4000, 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 2000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 4000, 4);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }
        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private static Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private Book createBook(String name, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }
    }
}