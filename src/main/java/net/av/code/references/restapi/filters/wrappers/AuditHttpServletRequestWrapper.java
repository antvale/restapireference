package net.av.code.references.restapi.filters.wrappers;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * Wraps the request to get the content. The servlet inputStream can be read once so
 * we need to store locally the content.
 */
public class AuditHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditHttpServletRequestWrapper.class);

    private static final String METHOD_POST = "POST";
    private static final String METHOD_GET = "GET";
    private static final String EMPTY_STRING="[EMPTY]";


    private byte[] content;

    private final HttpServletRequest delegate;

    public AuditHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.delegate = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (ArrayUtils.isEmpty(content)) {
            return delegate.getInputStream();
        }
        return new AuditServletInputStream(content);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (ArrayUtils.isEmpty(content)) {
            return delegate.getReader();
        }
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * Get the content (body) encompassed in the request payload.
     * @return the content if any otherwise an empty string
     */
    public String getContent() {
        try {

            if (super.getContentLengthLong()<=0) {
                LOGGER.info("No body in the request - Content Length {}",super.getContentLengthLong());
                // in this event there isn't any body in the request
                return StringUtils.EMPTY;
            }

            content = IOUtils.toByteArray(delegate.getInputStream());
            String requestEncoding = delegate.getCharacterEncoding();
            String normalizedContent = StringUtils.normalizeSpace(new String(content, requestEncoding != null ? requestEncoding : StandardCharsets.UTF_8.name()));
            return StringUtils.isBlank(normalizedContent) ? EMPTY_STRING : normalizedContent;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Internal class returns back the input stream from content.
     */
    private class AuditServletInputStream extends ServletInputStream {

        private final InputStream is;

        private AuditServletInputStream(byte[] content) {
            this.is = new ByteArrayInputStream(content);
        }

        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            // not used
        }

        @Override
        public int read() throws IOException {
            return this.is.read();
        }

        @Override
        public void close() throws IOException {
            super.close();
            is.close();
        }
    }
}