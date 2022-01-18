package com.example.library.controllers;

import com.example.library.model.Category;
import com.example.library.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Set<Category>> getAllCategories() {
        Set<Category> categories = categoryService.findAll();

        if (CollectionUtils.isEmpty(categories)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Category> findCategoryById(@PathVariable Long id) {
        Category category = categoryService.findById(id);

        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<Category> findCategoryByTitle(@RequestParam(value = "firstName") String firstName,
                                                        @RequestParam(value = "lastName") String lastName) {
        Category category = categoryService.findByName(firstName);

        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(Category category) {
        categoryService.save(category);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        categoryService.update(id, category);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> patchCategory(@PathVariable Long id, @RequestBody Category category) {
        categoryService.updatePartial(id, category);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/update/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
