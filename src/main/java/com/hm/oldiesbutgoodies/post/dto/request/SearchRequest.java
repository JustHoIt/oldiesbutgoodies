package com.hm.oldiesbutgoodies.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    private String keyword;
    private String category;
    private SearchField filed;
    private SearchOrder searchOrder;
}
