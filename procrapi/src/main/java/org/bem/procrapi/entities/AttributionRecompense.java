package org.bem.procrapi.entities;// AttributionRecompense.java

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributionRecompense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ExcuseCreative excuse;

    @ManyToOne
    private Recompense recompense;
}
