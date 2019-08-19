package net.av.code.references.restapi.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.av.code.references.restapi.context.ContextParameterConstants;
import net.av.code.references.restapi.context.ContextParameters;
import net.av.code.references.restapi.filters.wrappers.AuditHttpServletRequestWrapper;
import net.av.code.references.restapi.filters.wrappers.AuditHttpServletResponseWrapper;

import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ContentHandler;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;
import static java.util.Arrays.asList;

@Component
@Order
public class AuditFilter implements Filter {

    private final Logger log = getLogger(getClass());

    private long maxContentSize=0;

    private Set<String> excludedPaths;

    private boolean enabled=false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("Filter Name: {}", filterConfig.getFilterName());

        String maxContentSizeParam = filterConfig.getInitParameter("audit.maxcontentsize");

        if (maxContentSizeParam != null) {
            this.maxContentSize = Long.parseLong (maxContentSizeParam);
        }

        String excludedPathsParam = filterConfig.getInitParameter("audit.excludedpaths");
        if (isNotBlank(excludedPathsParam)) {
            String[] paths = excludedPathsParam.split("\\s*,\\s*");
            this.excludedPaths = new HashSet<>(asList(paths));
        }

        String enabledParam = filterConfig.getInitParameter("audit.enable");

        if (isNotBlank(enabledParam))
            this.enabled=Boolean.parseBoolean(enabledParam);

        log.info("maxContentSize:{}", maxContentSize);
        log.info("excludedPaths:{}", excludedPaths);
        log.info("enabled:{}", enabled);

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        /* Skip audit when disabled */
        if (!enabled) {
            chain.doFilter(httpRequest,httpResponse);
            log.info("Skip Audit filter because disabled");
            return;
        }

        /* Skip audit when the path matches the exclusion rule */
        for (String excludedPath : excludedPaths) {
            String requestURI = httpRequest.getRequestURI();
            if (requestURI.startsWith(excludedPath)) {
                chain.doFilter(httpRequest, httpResponse);
                log.info("Skip Audit filter because matching exclusion pattern {}",excludedPath);
                return;
            }
        }

        AuditHttpServletRequestWrapper requestWrapper = new AuditHttpServletRequestWrapper(httpRequest);
        AuditHttpServletResponseWrapper responseWrapper = new AuditHttpServletResponseWrapper(httpResponse);

        //final String contentRequest=requestWrapper.getContent();
        String content="";

        try {
            //extract user and tenant
            ContextParameters.setUser("avalentino");
            ContextParameters.setTenant("World");
            ContextParameters.setValue(ContextParameterConstants.CONTEXT_KEY_HTTPMETHOD,requestWrapper.getMethod());
            //log.info("Request: {}",requestWrapper.getContent());
            content=requestWrapper.getContent();
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {

            /*
               Save in this block the audit trails. Be careful to manage request and response
             */

            log.info("Get values from context user:{}, tenant:{}, httpMethod:{}, customer:{}",
                    ContextParameters.getUser(),
                    ContextParameters.getTenant(),
                    ContextParameters.getValue(ContextParameterConstants.CONTEXT_KEY_HTTPMETHOD),
                    ContextParameters.getValue(ContextParameterConstants.CONTEXT_KEY_CUSTOMERNAME));
            log.info(ContextParameters.toJson());

            ContextParameters.remove();
            log.info(ContextParameters.toJson());

          log.info("Request: {}",content);
          log.info("Response: {}",responseWrapper.getContent());

          httpResponse.getOutputStream().write(responseWrapper.getContentAsBytes());
        }

    }

    /**
     * This method is responsible to write up the audit to data store
     * @param wrapperRequest
     * @param wrapperResponse
     */
    public void saveAudit(AuditHttpServletRequestWrapper wrapperRequest,
                          AuditHttpServletResponseWrapper wrapperResponse,
                          AuditStore auditTrail){

    }

    public void destroy() {

    } //destroy
}
