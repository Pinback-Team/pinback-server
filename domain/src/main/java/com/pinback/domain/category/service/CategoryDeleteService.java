package com.pinback.domain.category.service;

import com.pinback.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryDeleteService {
    
    private final CategoryRepository categoryRepository;

    public void deleteById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}