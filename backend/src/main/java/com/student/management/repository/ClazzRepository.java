package com.student.management.repository;

import com.student.management.entity.Clazz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 班级数据访问层
 */
@Repository
public interface ClazzRepository extends JpaRepository<Clazz, Long> {

    /**
     * 根据名称查询班级
     */
    Optional<Clazz> findByName(String name);

    /**
     * 根据名称模糊查询（分页）
     */
    Page<Clazz> findByNameContaining(String name, Pageable pageable);

    /**
     * 检查班级名称是否已存在
     */
    boolean existsByName(String name);

    /**
     * 自定义查询：根据名称或描述搜索
     */
    @Query("SELECT c FROM Clazz c WHERE " +
           "(:keyword IS NULL OR c.name LIKE %:keyword% OR c.description LIKE %:keyword%)")
    Page<Clazz> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 统计班级数量
     */
    @Query("SELECT COUNT(c) FROM Clazz c")
    long countAll();
}
