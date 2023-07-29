package com.ianmu.wiki.mapper;

import com.ianmu.wiki.entity.Test;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestMapper {

    public List<Test> list();
}
