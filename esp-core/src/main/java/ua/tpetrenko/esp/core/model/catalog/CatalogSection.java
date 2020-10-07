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
import ua.tpetrenko.esp.core.model.EShop;

/**
 * @author Roman Zdoronok
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "catalog_section")
public class CatalogSection extends AbstractEndpoint {

    @ManyToOne
    @JoinColumn(name = "fk_e_shop", nullable = false)
    private EShop eShop;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "catalogSection", fetch = FetchType.LAZY)
    private Set<SectionGroup> sectionGroups;

    public CatalogSection(String name, String url, EShop eShop) {
        super(name, url);
        this.eShop = eShop;
    }
}

