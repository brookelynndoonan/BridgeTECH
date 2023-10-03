package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

public class CreateUserRequest {

        @NotEmpty
        @JsonProperty("name")
        private String name;

        @JsonProperty("userId")
        private String userId;

        public CreateUserRequest() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
