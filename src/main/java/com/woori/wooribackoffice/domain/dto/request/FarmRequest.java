package com.woori.wooribackoffice.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarmRequest {
    private Long id;
    private String address;
    private String name;
    private String owner;
}
