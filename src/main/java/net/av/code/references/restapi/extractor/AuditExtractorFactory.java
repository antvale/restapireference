package net.av.code.references.restapi.extractor;

public interface AuditExtractorFactory {

public Extractor getExtractor(String fullQualifiedName);

}