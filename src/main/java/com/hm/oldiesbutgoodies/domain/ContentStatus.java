package com.hm.oldiesbutgoodies.domain;

public enum ContentStatus {
    PUBLIC, //공개
    MEMBER_ONLY,//공개(회원 조회)
    PRIVATE, //숨김(작성자, 관리자만 조회 가능)
    PROTECT //숨김(관리자만 조회 가능)
}
