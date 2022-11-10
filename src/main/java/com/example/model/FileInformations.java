package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileInformations {
    private String tenantId;
    private String producerId;
    private String filename;
    private String url;
    private String traceParent;
}
