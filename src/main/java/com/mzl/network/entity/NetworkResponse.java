package com.mzl.network.entity;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class NetworkResponse {

    private Integer port;

    private Boolean success;


}
