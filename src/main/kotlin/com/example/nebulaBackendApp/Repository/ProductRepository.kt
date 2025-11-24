package com.example.nebulaBackendApp.Repository

import com.example.nebulaBackendApp.Model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {

}