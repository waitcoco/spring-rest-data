package com.mycompany.knwoledge.miami.publish.engine.gongan;

import com.mycompany.knowledge.miami.publish.engine.PublishEngine;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;

public class JenaGonganPublishEngine implements PublishEngine{
    private DatasetAccessor accessor;
    private String fusekiURI;
    public JenaGonganPublishEngine(String fusekiURI, String modelName) {
        this.fusekiURI = fusekiURI;
        this.accessor = DatasetAccessorFactory.createHTTP(fusekiURI);
    }

    @Override
    public void publish() {

    }
}
