package net.av.code.references.restapi.filters.wrappers;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class AuditHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final LoggingServletOutpuStream loggingServletOutpuStream = new LoggingServletOutpuStream();

    private final HttpServletResponse delegate;

    public AuditHttpServletResponseWrapper(HttpServletResponse response){
        super(response);
        delegate = response;
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return loggingServletOutpuStream;
    }

    @Override
    public PrintWriter getWriter() {
        return new PrintWriter(loggingServletOutpuStream.baos);
    }

    public String getContent() {
        try {
            String responseEncoding = delegate.getCharacterEncoding();
            return loggingServletOutpuStream.baos.toString(responseEncoding != null ? responseEncoding : StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return "[UNSUPPORTED ENCODING]";
        }
    }

    public byte[] getContentAsBytes() {
        return loggingServletOutpuStream.baos.toByteArray();
    }


    private class LoggingServletOutpuStream extends ServletOutputStream {

        private ByteArrayOutputStream baos = new ByteArrayOutputStream();

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            // not used
        }

        @Override
        public void write(int b) {
            baos.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            baos.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) {
            baos.write(b, off, len);
        }
    }
}
