package com.webapp.webapp_api.controller.Category;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.webapp_api.dto.category.CategoryDTO;
import com.webapp.webapp_api.service.category.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
    
    CategoryService categoryService; 

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping("/createCategory")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO category = categoryService.create(categoryDTO);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getCategories")
    public List<CategoryDTO> getAllProducts(@RequestBody long id){
        List<CategoryDTO> categories = categoryService.getAll();
        return categories;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getProductById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getById(id);
        return ResponseEntity.ok(categoryDTO);
    }

}
