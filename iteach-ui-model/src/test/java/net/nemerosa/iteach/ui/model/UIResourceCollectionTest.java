package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.nemerosa.iteach.common.json.JsonUtils;
import org.junit.Test;

import java.util.Arrays;

import static net.nemerosa.iteach.common.json.JsonUtils.object;
import static net.nemerosa.iteach.test.TestUtils.assertJsonRead;
import static net.nemerosa.iteach.test.TestUtils.assertJsonWrite;

public class UIResourceCollectionTest {

    @Test
    public void to_json() throws JsonProcessingException {
        assertJsonWrite(jsonResourceCollection(), resourceCollection());
    }

    @Test
    public void from_json() throws JsonProcessingException {
        assertJsonRead(resourceCollection(), jsonResourceCollection(), UISchoolCollection.class);
    }

    private ObjectNode jsonResourceCollection() {
        return object()
                .with("resources", JsonUtils.array()
                        .with(object().with("id", 1).with("href", "api/teacher/school/1").with("name", "School1").with("colour", "#111111").with("hasComments", false).end())
                        .with(object().with("id", 2).with("href", "api/teacher/school/2").with("name", "School2").with("colour", "#222222").with("hasComments", true).end())
                        .end())
                .with("href", "api/teacher/school")
                .end();
    }

    private UISchoolCollection resourceCollection() {
        return new UISchoolCollection(
                Arrays.asList(
                        new UISchoolSummary(1, "School1", "#111111", false),
                        new UISchoolSummary(2, "School2", "#222222", true)
                )
        );
    }

}
