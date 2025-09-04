package com.pinback.application.category.port.out;

import java.util.Set;

import com.pinback.domain.category.enums.CategoryColor;
import com.pinback.domain.user.entity.User;

public interface CategoryColorServicePort {

	Set<CategoryColor> getUsedColorsByUser(User user);
}
