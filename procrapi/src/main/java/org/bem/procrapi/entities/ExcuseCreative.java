package org.bem.procrapi.entities;// ExcuseCreative.java

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcuseCreative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texte;

    @OneToMany(mappedBy="excusePreferee")
    private List<Utilisateur> utilisateurs;
}
