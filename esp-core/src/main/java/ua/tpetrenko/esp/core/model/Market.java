package ua.tpetrenko.esp.core.model;

import javax.persistence.Entity;
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
@Table(name = "market")
public class Market extends AbstractEndpoint {

    public Market(String name, String url) {
        super(name, url);
    }
}
