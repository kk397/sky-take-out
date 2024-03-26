package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {


}
