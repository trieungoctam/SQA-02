package com.spring.privateClinicManage.custom.response.HFResponse;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HFChatCompletionResponse {

    private List<Choice> choices;
    private int created;
    private String id;
    private String model;
    private String systemFingerprint;
    private Usage usage;

    @Getter
    @Setter
    public static class Choice {
        private String finishReason;
        private int index;
        private Logprobs logprobs;
        private Message message;

        @Getter
        @Setter
        public static class Logprobs {
            private List<Content> content;

            @Getter
            @Setter
            public static class Content {
                private double logprob;
                private String token;
                private List<TopLogprob> topLogprobs;

                @Getter
                @Setter
                public static class TopLogprob {
                    private double logprob;
                    private String token;

                }
            }
        }
        @Getter
        @Setter
        public static class Message {
            private String content;
            private String role;
            private List<ToolCall> toolCalls;

            @Getter
            @Setter
            public static class ToolCall {
                private Function function;
                private String id;
                private String type;

                @Getter
                @Setter
                public static class Function {
                    private Object arguments;
                    private String description;
                    private String name;

                }
            }
        }
    }
    @Getter
    @Setter
    public static class Usage {
        private int completionTokens;
        private int promptTokens;
        private int totalTokens;

    }

}
