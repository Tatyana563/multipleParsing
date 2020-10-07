package ua.tpetrenko.esp.core.model.details;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.Data;
import ua.tpetrenko.esp.core.model.Item;

/**
 * @author Roman Zdoronok
 */
//@Entity
//@Table(name = "info_metric_value")
@Data
public class InfoMetricValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigserial")
    private Long id;

    @Column(name = "value", columnDefinition = "text", nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "fk_info_metric", nullable = false)
    private InfoMetric infoMetric;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Item> items;
}
