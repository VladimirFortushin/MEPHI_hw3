package ru.mephi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShortenLinkResponse {
    @JsonProperty(value = "link")
    private String link;
    @JsonProperty(value = "expires_at")
    private String expiresAt;
    @JsonProperty(value = "usages")
    private int usages;
    @JsonProperty(value = "response_code")
    private int responseCode;
    @JsonProperty(value = "response_message")
    private String responseMessage;
}
