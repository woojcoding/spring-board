package com.study.springboard.services;

import com.study.springboard.dtos.CategoryDto;
import com.study.springboard.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Category service.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 목록을 Repository에 요청하기 위한 메서드
     *
     * @return List<Category> 카테고리 List
     */
    public List<CategoryDto> getCategoryList() {
        return categoryRepository.getCategoryList();
    }
}
