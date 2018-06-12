package com.mycompany.knwoledge.miami.publish.engine.gongan;

import com.mycompany.knowledge.miami.publish.engine.PublishEngine;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.log4j.Logger;

public class JenaGonganPublishEngine implements PublishEngine{
    private DatasetAccessor accessor;
    private String fusekiURI;
    private Logger logger = Logger.getLogger(JenaGonganPublishEngine.class);
    private String inputModelName;
    public JenaGonganPublishEngine(String fusekiURI, String modelName) {
        this.fusekiURI = fusekiURI;
        this.accessor = DatasetAccessorFactory.createHTTP(fusekiURI);
        this.inputModelName = modelName;
    }

    @Override
    public String publish() {
        logger.info("In jena gongan publish engine");
        return this.inputModelName;
    }
}
