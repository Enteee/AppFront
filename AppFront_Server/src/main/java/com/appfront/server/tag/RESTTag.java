package com.appfront.server.tag;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.appfront.server.repositories.TagRepository;
import com.appfront.server.resources.Tag;

/**
 * A REST-endpoint for tag querying
 * 
 * @author ente
 */
@RestController
@RequestMapping("/tags")
public class RESTTag {
    
    private static final ArrayList<Tag> INIT_TAGS      = new ArrayList<Tag>();
    /**
     * The sorted field.
     */
    private static final String         TAG_SORT_FIELD = "tag";
    @Autowired
    private TagRepository               tagRepository;
    
    /**
     * Get all tags
     * 
     * @return a list of tags
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public TagList getTags() {
        final Sort tagSort = new Sort(new Order(Direction.ASC, TAG_SORT_FIELD));
        final Iterable<Tag> tagsIterator = tagRepository.findAll(tagSort);
        return new TagList(tagsIterator);
    }
    
    /**
     * Initialize some tags.
     */
    @RequestMapping(method = RequestMethod.PUT)
    public void initTags() {
        // Add a bunch of sample tags
        new Tag("Magic");
        new Tag("Pokemon");
        new Tag("Surfer");
    }
}
