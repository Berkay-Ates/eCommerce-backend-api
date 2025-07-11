package com.webapp.webapp_api.service.category;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.webapp.webapp_api.dto.category.CategoryDTO;
import com.webapp.webapp_api.model.Category;
import com.webapp.webapp_api.repository.category.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryDTO create(CategoryDTO categoryDTO) {
        Category category = new Category();

        category.setProductCategory(categoryDTO.getProductCategory());
        categoryRepository.save(category);

        return categoryDTO;
    }

    public List<CategoryDTO> getAll() {
        List<Category> categoryDTOs = categoryRepository.findAll();

        return categoryDTOs.stream()
                    .map(p -> {
                        CategoryDTO dto = new CategoryDTO();
                        dto.setProductCategory(p.getProductCategory());
                        dto.setCreatedAt(p.getCreatedAt());
                        return dto;
                    })
                    .collect(Collectors.toList());
    }

    public CategoryDTO getById(Long id) {
        Category category = categoryRepository.findById(id)
                   .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        CategoryDTO categoryDto = new CategoryDTO();
        categoryDto.setCreatedAt(category.getCreatedAt());
        categoryDto.setProductCategory(category.getProductCategory());
        
        return categoryDto;
    }

    @Transient
    public void delete(Long id) {
        if (!categoryRepository.existsById(id))
            throw new EntityNotFoundException("Category not found");
        categoryRepository.deleteById(id);
    }

}