package com.rest.server.graphql;

import com.rest.server.models.Comment;
import com.rest.server.models.Post;
import com.rest.server.models.User;
import com.rest.server.models.UserDto;
import com.rest.server.services.CommentService;
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
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin("*")
public class CommentResolver {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    // Query to get all comments with pagination
    @QueryMapping
    public List<Comment> comments(@Argument Integer page, @Argument Integer limit) {
        int pageNum = page != null ? page : 0;
        int pageSize = limit != null ? limit : 10;

        Page<Comment> commentsPage = commentService.allComments(PageRequest.of(pageNum, pageSize));
        return commentsPage.getContent();
    }
    // Query to get comments by post
    @QueryMapping
    public List<Comment> commentsByPost(@Argument String postId, @Argument Integer page,@Argument Integer limit) {

        int pageNum = page != null ? page : 0;
        int pageSize = limit != null ? limit : 10;

        Page<Comment> commentsPage = commentService.findCommentsByPostId(postId, pageNum, pageSize);
        return commentsPage.getContent();
    }
    // Query to get comments by user
    @QueryMapping
    public List<Comment> commentsByUser(@Argument String userId,@Argument Integer page,@Argument Integer limit) {

        int pageNum = page != null ? page : 0;
        int pageSize = limit != null ? limit : 10;

        Page<Comment> commentsPage = commentService.findCommentsByUserId(userId, pageNum, pageSize);
        return commentsPage.getContent();
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
    // Resolver for owner field in Comment type
    @SchemaMapping(typeName = "Comment", field = "owner")
    public UserDto owner(Comment comment) {
        if (comment.getCommentOwnerId() != null) {
            Optional<User> userOptional = userService.singleUser(comment.getCommentOwnerId());
            return userOptional.map(this::convertToDto).orElse(null);
        }
        return null;
    }

    // Resolver for post field in Comment type
    @SchemaMapping(typeName = "Comment", field = "post")
    public Post post(Comment comment) {
        if (comment.getCommentPostId() != null) {
            Optional<Post> postOptional = postService.singlePost(comment.getCommentPostId());
            return postOptional.orElse(null);
        }
        return null;
    }

    // Mutation to create a comment
    @MutationMapping
    public Comment createComment(@Argument String message,@Argument String ownerId,@Argument String postId) {

        Comment newComment = new Comment();
        newComment.setCommentMessage(message);
        newComment.setCommentOwnerId(ownerId);
        newComment.setCommentPostId(postId);

        return commentService.createComment(newComment);
    }

    // Mutation to delete a comment
    @MutationMapping
    public String deleteComment(@Argument String id) {
        commentService.deleteComment(id);
        return id;
    }
}