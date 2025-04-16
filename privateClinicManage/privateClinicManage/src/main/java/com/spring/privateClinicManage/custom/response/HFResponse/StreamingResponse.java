package com.spring.privateClinicManage.custom.response.HFResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class StreamingResponse {
    private Choice[] choices;

    @Getter
    @Setter
    public static class Choice {
        private Delta delta;
        private String finishReason;

        @Getter
        @Setter
        public static class Delta {
            private String content;

        }
    }
}