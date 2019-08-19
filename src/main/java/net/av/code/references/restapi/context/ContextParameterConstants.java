package net.av.code.references.restapi.context;


/**
 * Constants used by ContextSet. Naming convention for these constants is CONTEXT_KEY_<KEY_NAME> where key is the
 * name for the key used to get the value from ContextSet.
 *
 * @author avalentino
 */
public interface ContextParameterConstants {

    public static final String CONTEXT_KEY_TENANT="context.key.tenant";

    public static final String CONTEXT_KEY_USER="context.key.user";

    public static final String CONTEXT_KEY_HTTPMETHOD="context.key.httpmethod";

    public static final String CONTEXT_KEY_CUSTOMERNAME="context.key.customername";

}
