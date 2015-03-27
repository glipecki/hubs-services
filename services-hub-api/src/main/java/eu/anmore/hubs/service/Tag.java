package eu.anmore.hubs.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class Tag {

    private String tag;

    /**
     * @deprecated Deserialization only
     */
    @Deprecated
    public Tag() {
    }

    public Tag(String tag) {
        this.tag = tag;
    }

    public static Collection<Tag> of(Collection<String> tags) {
        return Optional
                .ofNullable(tags)
                .orElse(Collections.emptyList())
                .stream()
                .map(tagString -> Tag.of(tagString))
                .collect(Collectors.toList());
    }

    public static Tag of(String tag) {
        return new Tag(tag);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Tag{");
        sb.append("tag='").append(tag).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
