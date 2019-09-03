package net.av.code.references.restapi.extractor.impl;

import java.util.List;

public class ExtractorConfiguration {

    private List<ExtractBean> extractors;

    public List<ExtractBean> getExtractors() {
        return extractors;
    }

    public void setExtractors(List<ExtractBean> extractors) {
        this.extractors = extractors;
    }
}
