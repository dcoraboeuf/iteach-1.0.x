package net.nemerosa.iteach.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Document {

    private final String title;
    private final String type;
    private final String extension;
    private final byte[] content;

    public Document(UntitledDocument doc, String title, String extension) {
        this(title, doc.getType(), extension, doc.getContent());
    }

    public UntitledDocument toUntitledDocument() {
        return new UntitledDocument(type, content);
    }

}
