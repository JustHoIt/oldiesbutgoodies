package com.hm.oldiesbutgoodies.product.repository;


import com.hm.oldiesbutgoodies.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
