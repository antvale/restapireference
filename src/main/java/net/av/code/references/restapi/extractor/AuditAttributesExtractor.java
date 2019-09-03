package net.av.code.references.restapi.extractor;

import java.util.List;
import java.util.Map;

/**
 * Type to be implemented to extract attributes from a any data transfer object.
 *
 * @param <X>
 */
public interface AuditAttributesExtractor<X> {

    public Map<String,String> extract(List<String> auditableParameters, X anyDataTransferObject);
}
