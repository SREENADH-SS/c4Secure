package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.Category.CategoryRequest;
import com.backend.c4s.Dto.Category.CategoryResponse;
import com.backend.c4s.Entity.Category;
import com.backend.c4s.Exception.BadRequestException;
import com.backend.c4s.Exception.ResourceNotFoundException;
import com.backend.c4s.Mapper.CategoryMapper;
import com.backend.c4s.Repository.CategoryRepository;
import com.backend.c4s.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())){
            throw new BadRequestException("Category with name '" + request.getName()+ "' already exists.");
        }
        Category category= categoryMapper.toEntity(request);
        Category savedCategory= categoryRepository.save(category);

        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category= categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category", "id", id));

        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper :: toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        Category category= categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category", "id", id));

        categoryRepository.delete(category);
    }
}
