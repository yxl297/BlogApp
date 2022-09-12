package org.dxc.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.dxc.Payload.PostDTO;
import org.dxc.Payload.PostResponse;
import org.dxc.Repository.PostRepository;
import org.dxc.Service.Impl.PostServiceInterface;
import org.dxc.entity.Post;
import org.dxc.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImplementation implements PostServiceInterface {
	
	@Autowired
	private PostRepository postRepository;
	
	//convert entity to DTO
	private PostDTO mapToDto(Post post) {
		PostDTO postDto = new PostDTO();
		postDto.setId(post.getId());
		postDto.setTitle(post.getTitle());
		postDto.setDescription(post.getDescription());
		postDto.setContent(post.getContent());
		
		return postDto;
	}
	
	//convert DTO to entity
	public Post mapToEntity (PostDTO postDto) {
		Post post = new Post();
		post.setId(null);
		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());
		
		return post;
	}
	
	// implementing Create PostBlog
	@Override
	public PostDTO createPost(PostDTO postDTO) {
		// convert DTO to entity
		Post post = mapToEntity(postDTO);
		Post newPost = postRepository.save(post);
		
		// convert entity to DTO
		PostDTO postResponse = mapToDto(newPost);
		
		
		
		return postResponse;
	}
	// implementing get postblog
	@Override
	public PostResponse getAppPosts(int pageNo, int pageSize, String sortBy, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
				Sort.by(sortBy).ascending() :
					Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Post> posts = postRepository.findAll(pageable);
		
		// get content from page object
		List<Post> listOfPosts = posts.getContent();
		
		List<PostDTO> content = listOfPosts.stream()
				.map(post -> mapToDto(post))
				.collect(Collectors.toList());
		
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(content);
		postResponse.setPageNo(posts.getNumber());
		postResponse.setPageSize(posts.getSize());
		postResponse.setTotalElement(posts.getTotalElements());
		postResponse.setTotalPage(posts.getTotalPages());
		postResponse.setLast(posts.isLast());
		return postResponse;

	}

	@Override
	public PostDTO getPostById(Long id) {
		Post post = postRepository.findById(id).orElseThrow(
				()->new ResourceNotFoundException("POST", "id", id));
		return mapToDto(post);
	}

	@Override
	public PostDTO updatePost(PostDTO postDto, Long id) {
		Post post = postRepository.findById(id).orElseThrow(
				()->new ResourceNotFoundException("POST", "id", id));
		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());
		
		Post updatePost = postRepository.save(post);
		return mapToDto(updatePost);	
	}

	@Override
	public void deletePostById(Long id) {
		Post post = postRepository.findById(id).orElseThrow(
				()->new ResourceNotFoundException("POST", "id", id));
		postRepository.delete(post);
	}

	
}
