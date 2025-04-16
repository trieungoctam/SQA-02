package com.spring.privateClinicManage;

import com.spring.privateClinicManage.service.impl.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Test suite for running all tests related to consultation Q&A functionality
 */
@Suite
@SelectClasses({
        BlogServiceImplTest.class,
        LikeBlogServiceImplTest.class,
        CommentBlogServiceImplTest.class,
        ChatRoomServiceImplTest.class,
        ChatMessageServiceImplTest.class
})
public class ConsultationQATestSuite {
    // This class serves as a test suite container
}
