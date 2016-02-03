package com.appfront.server.tag;

import java.util.ArrayList;
<<<<<<< HEAD
=======
import java.util.Iterator;
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42

import com.appfront.server.resources.Tag;

import com.appfront.server.resources.Tag;

/**
 * A immutable list of tags tags.
 * 
 * @author ente
 */
public class TagList implements Iterable<Tag> {
    
    private final ArrayList<Tag> tags = new ArrayList<Tag>();
    
    /**
<<<<<<< HEAD
     * Constructor of TagList.
     * 
     * @param tagsIterator
     *            add all tags from iterator to the tags list
     */
    public TagList(final Iterable<Tag> tagsIterator) {
        for (Tag tag : tagsIterator) {
            this.tags.add(tag);
        }
=======
     * Constructor of an empty TagList
     */
    public TagList() {
        // do nothing
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
    }
    
    /**
     * Constructor of TagList.
     * 
     * @param tagsIterator
     *            add all tags from iterator to the tags list
     */
<<<<<<< HEAD
    public ArrayList<Tag> getTags() {
        return tags;
=======
    public TagList(final Iterable<Tag> tagsIterator) {
        for (Tag tag : tagsIterator) {
            this.tags.add(tag);
        }
    }
    
    @Override
    public Iterator<Tag> iterator() {
        return ((TagList) tags.clone()).iterator();
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
    }
}
