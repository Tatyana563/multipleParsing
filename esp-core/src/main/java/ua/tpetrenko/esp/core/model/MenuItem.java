package ua.tpetrenko.esp.core.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
