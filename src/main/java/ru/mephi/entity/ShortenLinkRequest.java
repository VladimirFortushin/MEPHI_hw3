package ru.mephi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShortenLinkRequest {
    @JsonProperty(value = "user")
    private User user;
    @JsonProperty(value = "url")
    private String url;
}
