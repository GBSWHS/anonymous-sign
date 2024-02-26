package com.threlease.base.utils;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

// 세션 안의 데이터
@Getter
@Builder
public class Session {
    public String student_id;
    public String name;
    public Date created_At;
}
