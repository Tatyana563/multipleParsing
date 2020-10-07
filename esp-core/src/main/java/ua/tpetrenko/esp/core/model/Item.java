package ua.tpetrenko.esp.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.tpetrenko.esp.core.model.catalog.ItemCategory;

/**
 * @author Roman Zdoronok
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "item")
public class Item extends AbstractEndpoint {

    @Column(name = "price")
    private Double price;

    @Column(name = "code", columnDefinition = "text")
    private String code;

    @Column(name = "available")
    private boolean available;

    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "fk_item_category", nullable = false)
    private ItemCategory itemCategory;

    public Item(String code) {
        this.code = code;
    }
}
