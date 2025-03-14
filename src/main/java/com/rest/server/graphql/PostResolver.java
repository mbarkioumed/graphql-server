package com.rest.server.graphql;

import com.rest.server.models.Post;
import com.rest.server.models.User;
import com.rest.server.models.UserDto;
import com.rest.server.services.PostService;
import com.rest.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class PostResolver {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    // Query to get all posts with pagination
    @QueryMapping
    public List<Post> posts(@Argument Integer page, @Argument Integer limit) {
        int pageNum = page != null ? page : 0;
        int pageSize = limit != null ? limit : 10;

        Page<Post> postsPage = postService.allPosts(PageRequest.of(pageNum, pageSize));
        return postsPage.getContent();
    }

    // Query to get a single post by ID
    @QueryMapping
    public Post post(@Argument String id) {
        Optional<Post> postOptional = postService.singlePost(id);
        return postOptional.orElse(null);
    }

    // Query to get posts by user
    @QueryMapping
    public List<Post> postsByUser(@Argument String userId, @Argument Integer page, @Argument Integer limit) {

        int pageNum = page != null ? page : 0;
        int pageSize = limit != null ? limit : 10;

        Page<Post> postsPage = postService.findPostsByUserId(userId, pageNum, pageSize);
        return postsPage.getContent();
    }

    // Query to get posts by tag
    @QueryMapping
    public List<Post> postsByTag(@Argument String tag, @Argument Integer page, @Argument Integer limit) {

        int pageNum = page != null ? page : 0;
        int pageSize = limit != null ? limit : 10;

        Page<Post> postsPage = postService.findPostsByTag(tag, pageNum, pageSize);
        return postsPage.getContent();
    }

    // Resolver for owner field in Post type
    @SchemaMapping(typeName = "Post", field = "owner")
    public UserDto owner(Post post) {
        if (post.getPostOwnerId() != null) {
            Optional<User> userOptional = userService.singleUser(post.getPostOwnerId());
            return userOptional.map(this::convertToDto).orElse(null);
        }
        return null;
    }

    // Mutation to create a post
    @MutationMapping
    public Post createPost(@Argument String text, @Argument String ownerId) {
        Post newPost = new Post();
        newPost.setPostText(text);
        newPost.setPostOwnerId(ownerId);
        newPost.setPostLikes(0);

        return postService.createPost(newPost);
    }

    // Mutation to update a post
    @MutationMapping
    public Post updatePost(@Argument String id, @Argument String text, @Argument String image) {

        Optional<Post> postOptional = postService.singlePost(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (text != null) post.setPostText(text);
            if (image != null) post.setPostImage(image);

            return postService.updatePost(id, post);
        }
        return null;
    }

    // Mutation to delete a post
    @MutationMapping
    public String deletePost(@Argument String id) {
        postService.deletePost(id);
        return id;
    }

    // Convert User to UserDto
    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUserTitle(user.getUserTitle());
        userDto.setUserFirstName(user.getUserFirstName());
        userDto.setUserLastName(user.getUserLastName());
        userDto.setUserPicture(user.getUserPicture());
        return userDto;
    }
}