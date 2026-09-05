package com.backend.c4s.Service;

import com.backend.c4s.Dto.Category.CategoryRequest;
import com.backend.c4s.Dto.Category.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse getCategoryById(Long id);
    List<CategoryResponse> getAllCCategories();
    void deleteCategory(Long id);
}
