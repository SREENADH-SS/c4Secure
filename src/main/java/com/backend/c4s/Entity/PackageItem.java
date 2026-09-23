package com.backend.c4s.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "package_items")
public class PackageItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package offerPackage;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product;

    private Integer quantity;
}
