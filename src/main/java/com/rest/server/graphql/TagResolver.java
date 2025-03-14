package com.rest.server.graphql;

import com.rest.server.models.Tag;
import com.rest.server.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class TagResolver {

    @Autowired
    private TagService tagService;

    // Query to get all tags with pagination
    @QueryMapping
    public List<Tag> tags(@Argument Integer page, @Argument Integer limit) {
        int pageNum = page != null ? page : 0;
        int pageSize = limit != null ? limit : 10;

        Page<Tag> tagsPage = tagService.allTags(PageRequest.of(pageNum, pageSize));
        return tagsPage.getContent();
    }

    // Query to get a single tag by ID
    @QueryMapping
    public Tag tag(@Argument String id) {
        Optional<Tag> tagOptional = tagService.singleTag(id);
        return tagOptional.orElse(null);
    }

    // Mutation to create a tag
    @MutationMapping
    public Tag createTag(@Argument String name) {
        Tag newTag = new Tag();
        newTag.setTagName(name);
        return tagService.createTag(newTag);
    }

    // Mutation to update a tag
    @MutationMapping
    public Tag updateTag(@Argument String id, @Argument String name) {
        Optional<Tag> tagOptional = tagService.singleTag(id);
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            tag.setTagName(name);
            return tagService.updateTag(id, tag);
        }
        return null;
    }

    // Mutation to delete a tag
    @MutationMapping
    public String deleteTag(@Argument String id) {
        tagService.deleteTag(id);
        return id;
    }
}