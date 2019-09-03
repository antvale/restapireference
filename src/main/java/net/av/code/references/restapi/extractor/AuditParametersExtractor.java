package net.av.code.references.restapi.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * To be implemented to extract audit paremeters from a list of parameter map.
 *
 */
public abstract class AuditParametersExtractor extends GenericAuditExtractor<Map<String,String>>{



    public Map<String,String> extract(Map<String,String> parameters){
        //get parameter from auditconfiguration
        List<String> auditableParams=new ArrayList<>();
        auditableParams.add("cat");
        auditableParams.add("dog");

        Map<String,String> params=extract(auditableParams,parameters);
        for(String key:params.keySet()){
            holdAuditParameter(key,params.get(key));
        }
        return params;
    }

    abstract public Map<String,String> extract(List<String> auditableParameters, Map<String,String> parameters);

    @Override
    public AuditPhaseEnum getAuditPhase() {
        return AuditPhaseEnum.HTTPHEADER;
    }



    @Override
    public AuditScopeEnum getAuditScope() {
        return AuditScopeEnum.FILTER;
    }

}
