package com.appfront.server.tag;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A REST-endpoint for tag querying
 * 
 * @author ente
 */
@Controller
public class TagController {
    
    private static final HashMap<Integer, String> tags = new HashMap<Integer, String>();
    
    /**
     * Constructor of the tag controller
     */
    public TagController() {
        // Add a bunch of sample tags
        tags.put(1, "Magic");
        tags.put(2, "Pokemon");
        tags.put(3, "Sports");
    }
    
    /**
     * Get tags
     * 
     * @return a list of tags
     */
    @RequestMapping("/get")
    @ResponseBody
    public HttpEntity<TagList> getTags() {
        final TagList tagsList = new TagList(tags);
        tagsList.add(linkTo(methodOn(TagController.class).getTags()).withSelfRel());
        return new ResponseEntity<TagList>(tagsList, HttpStatus.OK);
    }
}
