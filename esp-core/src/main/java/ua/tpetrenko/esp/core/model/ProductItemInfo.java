package ua.tpetrenko.esp.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ua.tpetrenko.esp.core.model.AbstractEndpoint;
import ua.tpetrenko.esp.core.model.MenuItem;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "product_item_info")
public class ProductItemInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigserial")
    private Long id;

    @Column(name = "external_id", columnDefinition = "text", nullable = false, unique = true)
    private String externalId;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @OneToOne
    @JoinColumn(name = "fk_product_item", nullable = false)
    private ProductItem productItem;

}
