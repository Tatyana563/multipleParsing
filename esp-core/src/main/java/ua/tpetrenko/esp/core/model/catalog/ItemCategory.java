package ua.tpetrenko.esp.core.model.catalog;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import ua.tpetrenko.esp.core.model.AbstractEndpoint;

/**
 * @author Roman Zdoronok
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "item_category")
public class ItemCategory extends AbstractEndpoint {
    @ManyToOne
    @JoinColumn(name = "fk_section_group", nullable = false)
    private SectionGroup sectionGroup;

    public ItemCategory(String name, String url, SectionGroup sectionGroup) {
        super(name, url);
        this.sectionGroup = sectionGroup;
    }
}
