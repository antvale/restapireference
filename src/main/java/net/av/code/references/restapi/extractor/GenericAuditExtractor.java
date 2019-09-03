package net.av.code.references.restapi.extractor;

import net.av.code.references.restapi.context.ContextParameters;

import java.util.List;
import java.util.Map;

public abstract class GenericAuditExtractor<X> implements Extractor<X> {


    abstract public Map<String, String> extract(X any);


    protected void holdAuditParameter(String key, String value){
        ContextParameters.setValue(key,value);
    }


    abstract public AuditPhaseEnum getAuditPhase();

    abstract public AuditScopeEnum getAuditScope();

}
