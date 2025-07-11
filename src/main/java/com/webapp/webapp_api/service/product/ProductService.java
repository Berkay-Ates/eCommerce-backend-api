package com.webapp.webapp_api.service.product;

import java.beans.Transient;
import java.util.List;import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.webapp.webapp_api.dto.product.ProductDTO;
import com.webapp.webapp_api.model.Category;
import com.webapp.webapp_api.model.Product;
import com.webapp.webapp_api.model.Seller;
import com.webapp.webapp_api.repository.category.CategoryRepository;
import com.webapp.webapp_api.repository.product.ProductRepository;
import com.webapp.webapp_api.repository.seller.SellerRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,SellerRepository sellerRepository, CategoryRepository categoryRepository){
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ProductDTO create(ProductDTO productDTO) {
        Product product = new Product();

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setStockQuantity(0L);
        product.setPhotoUrl(productDTO.getPhotoUrl());
        product.setSeller(null);
        product.setUpdatedAt(productDTO.getUpdatedAt());

        Seller seller = sellerRepository.getReferenceById(productDTO.getSellerId());
        product.setSeller(seller);

        Category category = categoryRepository.getReferenceById(productDTO.getCategoryId());
        product.setCategory(category);

        productRepository.save(product);
        return productDTO; 
    }

    public List<ProductDTO> getAll() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                    .map(p -> {
                        ProductDTO dto = new ProductDTO();
                        dto.setName(p.getName());
                        dto.setPrice(p.getPrice());
                        return dto;
                    })
                    .collect(Collectors.toList());
    }


    public ProductDTO getById(Long id) {
        Product product = productRepository.findById(id)
                   .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setDescription(product.getDescription());
        productDTO.setName(product.getName());
        productDTO.setPhotoUrl(product.getPhotoUrl());
        productDTO.setPrice(product.getPrice());
        productDTO.setSellerId(product.getSeller().getId());
        productDTO.setStockQuantity(product.getStockQuantity());
        productDTO.setUpdatedAt(product.getUpdatedAt());


        return productDTO;
    }

    @Transient
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Category category = categoryRepository.getReferenceById(productDTO.getCategoryId());
        existingProduct.setCategory(category);

                
        Seller seller = sellerRepository.getReferenceById(productDTO.getSellerId());
        existingProduct.setSeller(seller);

        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setName(productDTO.getName());
        existingProduct.setPhotoUrl(productDTO.getPhotoUrl());
        existingProduct.setPrice(productDTO.getPrice());                
        existingProduct.setUpdatedAt(productDTO.getUpdatedAt());
        existingProduct.setStockQuantity(productDTO.getStockQuantity());

        productRepository.save(existingProduct);
        return productDTO; 
    }

    @Transient
    public void delete(Long id) {
        if (!productRepository.existsById(id))
            throw new EntityNotFoundException("Product not found");
        productRepository.deleteById(id);
    }
}
