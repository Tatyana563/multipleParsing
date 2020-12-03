package ua.tpetrenko.esp.core.model;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author Roman Zdoronok
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "product_item")
public class ProductItem extends AbstractEndpoint {

    @Column(name = "code", columnDefinition = "text")
    private String code;

    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "fk_menu_item", nullable = false)
    private MenuItem menuItem;

    @OneToOne(mappedBy = "productItem", fetch = FetchType.EAGER)
    private ProductItemInfo itemInfo;

}
