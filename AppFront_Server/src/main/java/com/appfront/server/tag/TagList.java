package com.appfront.server.tag;

import java.util.ArrayList;
import java.util.Iterator;

import com.appfront.server.resources.Tag;

/**
 * A immutable list of tags tags.
 * 
 * @author ente
 */
public class TagList implements Iterable<Tag> {
    
    private final ArrayList<Tag> tags = new ArrayList<Tag>();
    
    /**
     * Constructor of an empty TagList
     */
    public TagList() {
        // do nothing
    }
    
    /**
     * Constructor of TagList.
     * 
     * @param tagsIterator
     *            add all tags from iterator to the tags list
     */
    public TagList(final Iterable<Tag> tagsIterator) {
        for (Tag tag : tagsIterator) {
            this.tags.add(tag);
        }
    }
    
    @Override
    public Iterator<Tag> iterator() {
        return ((TagList) tags.clone()).iterator();
    }
}
