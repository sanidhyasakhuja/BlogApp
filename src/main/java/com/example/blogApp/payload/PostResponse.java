package com.example.blogApp.payload;

import java.util.List;

import lombok.Data;

@Data
public class PostResponse {

	private List<PostDTO> content;

	private Integer pageSize;
	private Integer pageNumber;
	private Integer totalPages;
	private Long totalElements;
	private boolean isLastPage;
}
