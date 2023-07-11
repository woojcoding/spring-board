package com.study.springboard.repositories;

import com.study.springboard.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The type Category repository.
 */
@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final CategoryMapper categoryMapper;

    /**
     * 카테고리 목록을 반환해주는 메서드
     *
     * @return List<Category> 카테고리 List
     */
    public List<Category> findAll() {
        return categoryMapper.findAll();
    }
}
