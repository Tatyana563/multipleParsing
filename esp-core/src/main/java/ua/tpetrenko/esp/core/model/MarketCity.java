package ua.tpetrenko.esp.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Roman Zdoronok
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "market_city")
public class MarketCity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigserial")
    private Long id;

    @Column(name = "url", columnDefinition = "text", nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "fk_market", nullable = false)
    private Market market;

    @ManyToOne
    @JoinColumn(name = "fk_city", nullable = false)
    private City city;

    public MarketCity(String url, Market market, City city) {
        this.url = url;
        this.market = market;
        this.city = city;
    }
}
