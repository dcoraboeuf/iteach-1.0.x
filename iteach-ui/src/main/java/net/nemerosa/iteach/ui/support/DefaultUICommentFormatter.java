package net.nemerosa.iteach.ui.support;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class DefaultUICommentFormatter implements UICommentFormatter {

    private static final Pattern BOLD_PATTERN = Pattern.compile("\\*([^\\*]+)\\*");
    private static final Pattern ITALIC_PATTERN = Pattern.compile("_([^_]+)_");
    private static final Pattern LINK_PATTERN = Pattern.compile("(http\\:[^\\s]+)");

    @Override
    public String format(String content) {
        // Escape to HTML first
        String result = StringEscapeUtils.escapeHtml4(content);
        // Links
        result = LINK_PATTERN.matcher(result).replaceAll("<a href=\"$1\">$1</a>");
        // Replaces the carriage returns by <br/>
        result = result.replaceAll("\n|\r\n", "<br/>");
        // Bold: *...* --> <b>...</b>
        result = BOLD_PATTERN.matcher(result).replaceAll("<b>$1</b>");
        // Italic: _..._ --> <i>...</i>
        result = ITALIC_PATTERN.matcher(result).replaceAll("<i>$1</i>");
        // OK
        return result;
    }
}
