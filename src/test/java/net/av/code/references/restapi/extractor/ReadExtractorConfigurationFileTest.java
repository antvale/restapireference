package net.av.code.references.restapi.extractor;

import net.av.code.references.restapi.extractor.impl.DefaultAuditExtractorFactory;

import net.av.code.references.restapi.extractor.impl.ExtractorConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;

import java.util.Map;

public class ReadExtractorConfigurationFileTest {

    DefaultAuditExtractorFactory extractor=new DefaultAuditExtractorFactory("test-extractors.yaml");

    @Test
    public void readYamlFile(){
        ExtractorConfiguration yaml=extractor.readExtractorConfigurationFile("test-extractors.yaml");
        assertNotNull(yaml);
    }

    @Test
    public void readFromYamlFile(){
        ExtractorConfiguration yaml=extractor.readExtractorConfigurationFile("test-extractors.yaml");

        assertEquals("net.av.Filter.chain",yaml.getExtractors().get(0).getId());

    }
}
