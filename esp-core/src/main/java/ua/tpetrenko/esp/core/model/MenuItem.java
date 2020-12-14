package ua.tpetrenko.esp.core.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Roman Zdoronok
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "menu_item")
public class MenuItem extends AbstractEndpoint {

    @ManyToOne
    @JoinColumn(name = "fk_parent_item")
    private MenuItem parentItem;

    @ManyToOne
    @JoinColumn(name = "fk_market")
    private Market market;
}
