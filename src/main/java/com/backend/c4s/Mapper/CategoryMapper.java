package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.Category.CategoryRequest;
import com.backend.c4s.Dto.Category.CategoryResponse;
import com.backend.c4s.Entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request){
        if (request==null)return null;

        return Category.builder()
                .name(request.getName())
                .build();
    }

    public CategoryResponse toCategoryResponse(Category category){
        if (category==null){
            return  null;
        }
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
