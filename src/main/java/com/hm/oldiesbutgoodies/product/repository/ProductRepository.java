package com.hm.oldiesbutgoodies.product.repository;


import com.hm.oldiesbutgoodies.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDeletedFalse(long productId);
}
