package ua.tpetrenko.esp.core.model.details;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import ua.tpetrenko.esp.core.model.catalog.ItemCategory;

/**
 * @author Roman Zdoronok
 */
//@Entity
//@Table(name = "info_group")
@Data
public class InfoGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigserial")
    private Long id;

    @Column(name = "name", columnDefinition = "text", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_item_category", nullable = false)
    private ItemCategory itemCategory;
}
