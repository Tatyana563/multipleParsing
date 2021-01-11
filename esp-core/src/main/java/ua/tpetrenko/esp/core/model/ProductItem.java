package ua.tpetrenko.esp.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Roman Zdoronok
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "product_item")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductItem extends AbstractEndpoint {

    @Column(name = "code", columnDefinition = "text")
    private String code;

    @Column(name = "external_id", columnDefinition = "text")
    private String externalId;

    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "fk_menu_item", nullable = false)
    private MenuItem menuItem;

}
