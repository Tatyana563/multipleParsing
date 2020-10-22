package ua.tpetrenko.esp.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Roman Zdoronok
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "product_price")
public class ProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigserial")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_market_city", nullable = false)
    private MarketCity marketCity;

    @ManyToOne
    @JoinColumn(name = "fk_product_item", nullable = false)
    private ProductItem productItem;

    @Column(name = "price", nullable = false)
    private Double price;
}
