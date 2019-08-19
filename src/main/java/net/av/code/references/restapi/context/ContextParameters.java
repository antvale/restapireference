package net.av.code.references.restapi.context;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import org.json.JSONObject;

/**
 * Holds in a thread local the application context parameters injected by framework and by developers when
 * they need to propagate the context parameters across all the layers.
 * Variable key are defined at {@link ContextParameterConstants}.
 *
 * CAUTION: holding too large parameters might impact the memory allocation so take care to remove the context holder
 * calling the remove() method once no more used.
 * <br>
 *
 * @author avalentino
 */
public class ContextParameters {

    private static final Logger log = getLogger(ContextParameters.class);

    private static ThreadLocal<Map<String,String>> cxtHolder = new InheritableThreadLocal<Map<String,String>>();

    //private  Map<String, String> parameters= new HashMap<String, String>();

    public static String getUser(){
        return getParameter(ContextParameterConstants.CONTEXT_KEY_USER);
    }

    public static void setUser(String user){
        addParameter(ContextParameterConstants.CONTEXT_KEY_USER,user);
    }

    public static String getTenant(){
        return getParameter(ContextParameterConstants.CONTEXT_KEY_TENANT);
    }


    public static void setTenant(String tenant){
       addParameter(ContextParameterConstants.CONTEXT_KEY_TENANT,tenant);
    }

    public static String getValue(String key){
        return getParameter(key);
    }

    public static void setValue(String key, String value){
        addParameter(key,value);
    }

    public static boolean containsKey(String key){
        return cxtHolder.get()==null?false:cxtHolder.get().containsValue(key);
    }

    public static void removeValue(String key){
        removeParameter(key);
    }

    public static void remove(){
        cxtHolder.remove();
    }

    private static void addParameter(String key, String value){
        if (cxtHolder.get()==null) {
            if (log.isDebugEnabled())
                log.debug("Context Parameter map is empty, a new map is created and set in the holder");
            log.info("Context Parameter map is empty, a new map is created and set in the context holder");
            cxtHolder.set(new HashMap<>());
        }
        cxtHolder.get().put(key,value);
    }

    private static String getParameter(String key){
        if (cxtHolder.get()==null || !cxtHolder.get().containsKey(key)) {
            if (log.isDebugEnabled())
                log.debug("No parameter with key {} found in the Context Parameter holder",key);
            log.info("No parameter with key {} found in the Context Parameter holder",key);
            return "";
        }
        return cxtHolder.get().get(key);
    }

    private static void removeParameter(String key){
        if (cxtHolder.get()==null || !cxtHolder.get().containsKey(key)) {
            if (log.isDebugEnabled())
                log.debug("No parameter to be deleted with key {} found in the Context Parameter holder",key);
            log.info("No parameter to be deleted with key {} found in the Context Parameter holder",key);
            return;
        }
        cxtHolder.get().remove(key);
    }

    private static Map<String, String> getInternalMap(){
        if(cxtHolder.get()!=null) return cxtHolder.get();
        else return new HashMap<String,String>();
    }

    public static String toJson(){
        return new JSONObject(getInternalMap()).toString();
    }
}
