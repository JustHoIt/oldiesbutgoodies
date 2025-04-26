package com.hm.oldiesbutgoodies.content.repository;


import com.hm.oldiesbutgoodies.content.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
