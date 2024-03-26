package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private CategoryMapper categoryMapper;


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<DishVO> list = new ArrayList<>();
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        Long categoryId = dish.getCategoryId();
        lqw.eq(Dish::getCategoryId, categoryId)
                .eq(Dish::getStatus, dish.getStatus());
        List<Dish> dishes = dishMapper.selectList(lqw);
        dishes.forEach(dish1 -> {
            Category category = categoryMapper.selectById(categoryId);
            LambdaQueryWrapper<DishFlavor> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(DishFlavor::getDishId, dish1.getId());
            List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(lqw1);
            DishVO dishVO = DishVO.builder()
                    .categoryId(categoryId)
                    .categoryName(category.getName())
                    .description(dish1.getDescription())
                    .id(dish1.getId())
                    .image(dish1.getImage())
                    .name(dish1.getName())
                    .price(dish1.getPrice())
                    .status(dish1.getStatus())
                    .updateTime(dish1.getUpdateTime())
                    .flavors(dishFlavors)
                    .build();
            list.add(dishVO);
        });
        return list;
    }
}
