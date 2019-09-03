package net.av.code.references.restapi.extractor.impl;

import net.av.code.references.restapi.extractor.*;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 *  *.extractors.xml
 *   extractorClassName="clazz" | extractorID=method_FQN
 *   extractorID must match the fqn of the auditable method
 */
public class DefaultAuditExtractorFactory implements AuditExtractorFactory{

    private final Logger log = getLogger(getClass());

    Map<String,Extractor> extractors=new HashMap<>();

    private final String configFileName;

    public DefaultAuditExtractorFactory(String yamlFileName){
        this.configFileName=yamlFileName;
        loadAllExtractorsFromConfigurationFile(yamlFileName);
    }

    public Extractor getExtractor(String extractorId){
        if(extractors.containsKey(extractorId)) return extractors.get(extractorId);
        return null; //to review, never return back a null value
    }

    public ExtractorConfiguration readExtractorConfigurationFile(String yamlFileName){
        ExtractorConfiguration configuration= null;

        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(yamlFileName);

        if (inputStream!=null) {
            Yaml yaml = new Yaml(new org.yaml.snakeyaml.constructor.Constructor(ExtractorConfiguration.class));
            configuration = yaml.load(inputStream);
        }

        return configuration;
    }

    private void loadAllExtractorsFromConfigurationFile(String yamlFileName){
        ExtractorConfiguration config=readExtractorConfigurationFile(yamlFileName);
        for (ExtractBean bean:config.getExtractors()) {
            Extractor extractor = createExtractor(bean.getClassName());
            extractors.put(bean.getId(),extractor);
        }
    }

    public Extractor createExtractor(String fullQualifiedName) {

        Extractor extractor = null;

        try {
            Class clazz = Class.forName(fullQualifiedName);
            Constructor<?> constructor = clazz.getConstructor();
            Object instance = constructor.newInstance();

            extractor = (Extractor) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (InstantiationException e)
        {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e)
        {
            log.error(e.getMessage(), e);
        } catch (NoSuchMethodException e)
        {
            log.error(e.getMessage(), e);
        } catch (InvocationTargetException e){
            log.error(e.getMessage(),e);
        }

        return extractor;
    }


}
