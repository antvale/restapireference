package net.av.code.references.restapi.extractor;


import net.av.code.references.restapi.extractor.impl.DefaultAuditExtractorFactory;
import org.junit.Test;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateExtractorsFromConfigurationFile {

    DefaultAuditExtractorFactory extractorFactory=new DefaultAuditExtractorFactory("test-extractors.yaml");

    @Test
    public void createExtractorsReadingThemFromConfigurationFile(){
        Extractor extractor=extractorFactory.getExtractor("net.av.Filter.chain");
        if (extractor instanceof GenericAuditExtractor ){
                assertEquals(AuditPhaseEnum.HTTPHEADER.name(),((GenericAuditExtractor) extractor).getAuditPhase().name());
        }
    }
}
