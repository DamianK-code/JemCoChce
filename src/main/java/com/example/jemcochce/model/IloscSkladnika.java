package com.example.jemcochce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IloscSkladnika {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Przepis przepis;

    @ManyToOne
    @ToString.Exclude
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Skladnik skladnik;

    private double ilosc;
}
