package ua.tpetrenko.esp.core.model.catalog;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.tpetrenko.esp.core.model.AbstractEndpoint;

/**
 * @author Roman Zdoronok
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "section_group")
public class SectionGroup extends AbstractEndpoint {
    @ManyToOne
    @JoinColumn(name = "fk_catalog_section", nullable = false)
    private CatalogSection catalogSection;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "sectionGroup", fetch = FetchType.LAZY)
    private Set<ItemCategory> itemCategories;

    public SectionGroup(String name, String url, CatalogSection catalogSection) {
        super(name, url);
        this.catalogSection = catalogSection;
    }
}
