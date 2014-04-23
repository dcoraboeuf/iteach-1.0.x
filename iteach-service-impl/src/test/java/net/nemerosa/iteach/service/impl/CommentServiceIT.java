package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.it.AbstractITTestSupport;
import net.nemerosa.iteach.service.CommentService;
import net.nemerosa.iteach.service.model.Comment;
import net.nemerosa.iteach.service.model.Student;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class CommentServiceIT extends AbstractITTestSupport {

    @Autowired
    private ServiceITSupport serviceITSupport;

    @Autowired
    private CommentService commentService;

    @Test
    public void comments() throws Exception {
        Student student = serviceITSupport.createStudent();
        int teacherId = student.getTeacherId();
        int studentId = student.getId();
        serviceITSupport.asTeacher(teacherId, () -> {
            assertTrue("No comment", commentService.getComments(CommentEntity.student, studentId).isEmpty());
            assertFalse("No comment", commentService.hasComments(CommentEntity.student, studentId));
            // Creates one comment
            int c1 = commentService.postComment(CommentEntity.student, studentId, "First comment");
            // Gets this comment
            Comment c = commentService.getComment(CommentEntity.student, c1);
            assertEquals(c1, c.getId());
            assertEquals(CommentEntity.student, c.getEntity());
            assertEquals(studentId, c.getEntityId());
            assertEquals("First comment", c.getRawContent());
            assertNotNull("Creation date", c.getCreation());
            assertNull("No update date", c.getUpdate());
            // Creates a second comment
            int c2 = commentService.postComment(CommentEntity.student, studentId, "Second comment");
            // Gets the list of comments
            List<Comment> comments = commentService.getComments(CommentEntity.student, studentId);
            assertEquals("Two comments", 2, comments.size());
            assertEquals(c2, comments.get(0).getId());
            assertEquals(c1, comments.get(1).getId());
            // Deletes the second comment
            commentService.deleteComment(CommentEntity.student, c2);
            comments = commentService.getComments(CommentEntity.student, studentId);
            assertEquals("One comment left", 1, comments.size());
            assertEquals(c1, comments.get(0).getId());
            // Updates the first comment
            commentService.updateComment(CommentEntity.student, c1, "*Very* first comment");
            c = commentService.getComment(CommentEntity.student, c1);
            assertEquals(c1, c.getId());
            assertEquals(CommentEntity.student, c.getEntity());
            assertEquals(studentId, c.getEntityId());
            assertEquals("*Very* first comment", c.getRawContent());
            assertNotNull("Creation date", c.getCreation());
            assertNotNull("Update date", c.getUpdate());
            // End
            return null;
        });
    }

}
