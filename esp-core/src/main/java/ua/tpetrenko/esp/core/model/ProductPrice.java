package ua.tpetrenko.esp.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import lombok.experimental.Accessors;

/**
 * @author Roman Zdoronok
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "product_price")
public class ProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigserial")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_city", nullable = false)
    private City city;

    @ManyToOne
    @JoinColumn(name = "fk_product_item", nullable = false)
    private ProductItem productItem;

    @Column(name = "price", nullable = false)
    private Double price;
}
