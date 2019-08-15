package net.av.code.references;

import net.av.code.references.restapi.filters.AuditFilter;
import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Value("${audit.enable}")
    private String isEnabled;

    @Value("${audit.includedpaths}")
    private String includedPaths;

    @Value("${audit.excludedpaths}")
    private String excludedPaths;

    @Value("${audit.maxcontentsize}")
    private String maxContentSize;

    @Autowired
    DataSourceProperties dataSourceProperties;

    @Bean
    AuditFilter auditFilter(){
        return  new AuditFilter();
    }


    @Bean
    DataSource dataSource() {
        DataSource dataSource = DataSourceBuilder
                .create(this.dataSourceProperties.getClassLoader())
                .url(this.dataSourceProperties.getUrl())
                .username(this.dataSourceProperties.getUsername())
                .password(this.dataSourceProperties.getPassword())
                .build();
        return new DataSourceSpy(dataSource);
    }

    @Bean
    public FilterRegistrationBean loggingFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(this.auditFilter());
        registrationBean.addUrlPatterns(getPaths(includedPaths));
        registrationBean.addInitParameter("audit.excludedpaths",excludedPaths);
        registrationBean.addInitParameter("audit.maxcontentsize",maxContentSize);
        registrationBean.addInitParameter( "audit.enable",isEnabled);
        return registrationBean;
    }

    /**
     * Return back the list of allowed url patterns from a comma-separated string of patterns
     * @param path - a comma-separated string of included patterns
     * @return the list of the included patterns
     */
    private String[] getPaths(String path){
        if (path!=null && !path.equals(""))
            return path.split("\\s*,\\s*");
        else return new String[]{"/*"};
    }

}