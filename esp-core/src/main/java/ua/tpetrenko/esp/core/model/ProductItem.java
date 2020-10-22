package ua.tpetrenko.esp.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Roman Zdoronok
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "product_item")
public class ProductItem extends AbstractEndpoint {

    @Column(name = "code", columnDefinition = "text")
    private String code;

    @Column(name = "external_id", columnDefinition = "text", nullable = false, unique = true)
    private String externalId;

    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "fk_menu_item", nullable = false)
    private MenuItem menuItem;

}
