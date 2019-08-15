package net.av.code.references.restapi.filters;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 * Includes the lightweight audit parameters to store in the threadlocal.
 * This class does not store the entire request and response because typically too large to be put in
 * the threadlocal.
 */
public class AuditStore {

    /* context parameters */
    private String pod;
    private String commodity;
    private String tenantID;
    private String user;

    /* */
    private String statusResult;
    private long requestTime;
    private long responseTime;
    private String httpMothod;
    private String httpQueryParams;


    private Map<String, String> extraParams= new HashMap<String, String>();

    public AuditStore() {
    }

    public AuditStore addExtraParameter(String key, String value) {
        extraParams.put(key, value);
        return this;
    }

    public String getExtraParams() {
        String jsonString = new JSONObject(parameters).toString();
        return jsonString;
    }



}
