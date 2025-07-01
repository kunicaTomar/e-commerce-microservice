package com.ecommerce.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.product.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    @Query("SELECT p FROM Product p WHERE p.active=true and p.stockQuantity>0 and lower(p.name) like lower(concat('%', :keyword, '%'))")
    List<Product> searchProducts(String keyword);

    Optional<Product> findByIdAndActiveTrue(Long id);
    
}
