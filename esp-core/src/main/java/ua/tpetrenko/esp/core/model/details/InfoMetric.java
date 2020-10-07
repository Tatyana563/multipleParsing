package ua.tpetrenko.esp.core.model.details;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

/**
 * @author Roman Zdoronok
 */
//@Entity
//@Table(name = "info_metric")
@Data
public class InfoMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigserial")
    private Long id;

    @Column(name = "key", columnDefinition = "text", nullable = false)
    private String key;

    @ManyToOne
    @JoinColumn(name = "fk_info_group", nullable = false)
    private InfoGroup infoGroup;
}
