package ua.tpetrenko.esp.core.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Roman Zdoronok
 */
@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class AbstractEndpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigserial")
    private Long id;

    @Column(name = "name", columnDefinition = "text")
    private String name;
    
    @Column(name = "url", columnDefinition = "text")
    private String url;

    protected AbstractEndpoint(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
