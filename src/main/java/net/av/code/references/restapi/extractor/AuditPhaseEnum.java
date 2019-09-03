package net.av.code.references.restapi.extractor;

import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

public enum AuditPhaseEnum {
    HTTPREQUEST,
    HTTPRESPONSE,
    HTTPHEADER,
    BEFOREMETHOD,
    AFTERMETHOD
}
