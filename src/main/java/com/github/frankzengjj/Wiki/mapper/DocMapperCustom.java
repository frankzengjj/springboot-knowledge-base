package com.github.frankzengjj.Wiki.mapper;

import com.github.frankzengjj.Wiki.domain.Test;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocMapperCustom {


    public void increaseViewCount(@Param("id") Long id);
    public void increaseVoteCount(@Param("id") Long id);
    public void updateEbookInfo();
}
