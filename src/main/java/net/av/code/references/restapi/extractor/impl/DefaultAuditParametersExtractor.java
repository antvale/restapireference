package net.av.code.references.restapi.extractor.impl;

import net.av.code.references.restapi.extractor.AuditParametersExtractor;
import net.av.code.references.restapi.extractor.AuditPhaseEnum;
import net.av.code.references.restapi.extractor.GenericAuditExtractor;

import java.util.*;

public class DefaultAuditParametersExtractor extends AuditParametersExtractor {


    public DefaultAuditParametersExtractor(){

    }

    public Map<String,String> extract(List<String> auditableParameters,Map<String,String> parameters){

        Map<String,String> params=new LinkedHashMap<String, String>();

        for(String param:auditableParameters){
            if(parameters.containsKey(param))
                params.put(param,parameters.get(param));
        }
        return params;
    }


    @Override
    public AuditPhaseEnum getAuditPhase() {
        return AuditPhaseEnum.HTTPHEADER;
    }

    public static void main(String args[]){

        DefaultAuditParametersExtractor extractor=new DefaultAuditParametersExtractor();

        Map<String,String> paramMap=new HashMap<String,String>();

        paramMap.put("dog","pippo");
        paramMap.put("cat","suzi");

        List<String> auditableParams=new ArrayList<>();
        auditableParams.add("cat");
        auditableParams.add("dog");

        Map<String,String> result= extractor.extract(paramMap);

        System.out.println(result.toString());

        if (extractor instanceof GenericAuditExtractor ){
            if (extractor.getAuditPhase()== AuditPhaseEnum.HTTPHEADER)
                System.out.println("Execute the extractor in HTTPHEADER PHASE");
        }
    }
}
