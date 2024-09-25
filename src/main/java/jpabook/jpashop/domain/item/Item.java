package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==ビジネスロジック==//

    /**
     *在庫増加
     */
    public void addStock(int quentity) {
        this.stockQuantity += quentity;
    }

    /**
     *在庫不足
     */
    public void removeStock(int quentity) {
        int restStock = this.stockQuantity - quentity;
        if (restStock < 0) {
            throw new NotEnoughStockException("在庫が足りません。");
        }
        this.stockQuantity = restStock;
    }
}
