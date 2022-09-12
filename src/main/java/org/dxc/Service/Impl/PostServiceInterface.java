package org.dxc.Service.Impl;

import org.dxc.Payload.PostDTO;
import org.dxc.Payload.PostResponse;

public interface PostServiceInterface {
	
	PostDTO createPost(PostDTO postDTO);
	
	PostResponse getAppPosts(int pageNo, int pageSize, String sortBy, String sortDirection);

	PostDTO getPostById(Long id);
	
	PostDTO updatePost(PostDTO postDto, Long id);
	
	void deletePostById(Long id);
	
}
