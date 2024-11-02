package com.bujdi.carRecords.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RecaptchaResponse {
    private boolean success;
    private String challenge_ts;
    private String hostname;

    @JsonProperty("error-codes")
    private String[] errorCodes;

}
