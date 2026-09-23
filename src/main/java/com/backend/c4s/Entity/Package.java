package com.backend.c4s.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "packages")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;

    private BigDecimal packagePrice;

    private BigDecimal installationCharge;

    private String imageUrl;

    private String publicId;

    private boolean active;

    @OneToMany(
            mappedBy = "offerPackage",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PackageItem> items;
}
