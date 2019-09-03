package net.av.code.references.restapi.extractor;

import java.util.List;
import java.util.Map;

public interface Extractor<X> {

    public Map<String,String> extract(X any);
}
