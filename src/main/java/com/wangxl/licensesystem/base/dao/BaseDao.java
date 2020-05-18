package com.wangxl.licensesystem.base.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface BaseDao<T>{


    /**
     * @MethodName : save
     * @Description : 保存一条记录
     * @Param :
     * @Return : int表示的是此sql语句执行后对数据库的影响条数
     * @Author : Wangxl
     * @Date : 2020/5/8 17:57
    */
    int save(T t);

    /**
     * @MethodName : update
     * @Description : 更新一条记录,更新的记录往往是对象的属性.
     * @Param :
     * @Return : int
     * @Author : Wangxl
     * @Date : 2020/5/8 17:58
    */
    int update(T t);

    /**
     * @MethodName : updateBatch
     * @Description : 更新一条记录,按照时间范围来更新:建议再写一个方法
     * @Param :
     * @Return : int
     * @Author : Wangxl
     * @Date : 2020/5/8 17:59
    */
    int updateBatch(Map<String, Object> condMap);

    /**
     * @MethodName : delete
     * @Description : 删除记录，按照ID删除，按照时间范围删除。
     * @Param :
     * @Return : int
     * @Author : Wangxl
     * @Date : 2020/5/8 17:59
    */
    int delete(Map<String, Object> condMap);

    /**
     * @MethodName : findOne
     * @Description : 查询一条记录，condMap 查询的条件；键=#{}，值是条件。
     * @Param :
     * @Return : T
     * @Author : Wangxl
     * @Date : 2020/5/8 18:00
    */
    T findOne(Map<String, Object> condMap);

    /**
     * @MethodName : findList
     * @Description : 查询多条记录
     * @Param :
     * @Return : java.util.List<T>
     * @Author : Wangxl
     * @Date : 2020/5/8 18:02
    */
    List<T> findList(Map<String,Object> condMap);
}
