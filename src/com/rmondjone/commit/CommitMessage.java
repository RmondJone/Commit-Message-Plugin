package com.rmondjone.commit;

import org.apache.commons.lang.WordUtils;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * @author Damien Arrachequesne <damien.arrachequesne@gmail.com>
 */
class CommitMessage {
    private static final int MAX_LINE_LENGTH = 72; // https://stackoverflow.com/a/2120040/5138796
    private final String content;

    CommitMessage(TypeAlias changeType, String changeScope, String shortDescription, String longDescription) {
        this.content = buildContent(changeType, changeScope, shortDescription, longDescription);
    }

    private String buildContent(TypeAlias changeType, String changeScope, String shortDescription, String longDescription) {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        builder.append(changeType.getTitle());
        builder.append(']');
        if (isNotBlank(changeScope)) {
            builder
                    .append('(')
                    .append(changeScope)
                    .append(')');
        }
        builder
                .append(": ")
                .append(shortDescription)
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(WordUtils.wrap(longDescription, MAX_LINE_LENGTH));

        return builder.toString();
    }

    @Override
    public String toString() {
        return content;
    }
}