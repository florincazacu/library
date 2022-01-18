package com.example.library.services;

import com.example.library.model.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService extends CrudService<Category, Long> {

    Category findByName(String name);
}
