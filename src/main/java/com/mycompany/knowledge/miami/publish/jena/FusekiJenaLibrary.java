package com.mycompany.knowledge.miami.publish.jena;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.log4j.Logger;

public class FusekiJenaLibrary extends BaseJenaLibrary {
    private String fusekiURI;
    private DatasetAccessor accessor;
    private Logger logger = Logger.getLogger(FusekiJenaLibrary.class);


    public FusekiJenaLibrary(String fusekiURI) {
        this.fusekiURI = fusekiURI;
        this.accessor = DatasetAccessorFactory.createHTTP(fusekiURI);
    }

    @Override
    public Model getModel(String modelName) {
        return accessor.getModel(modelName);
    }

    @Override
    public void removeModel(String modelName) {
        accessor.deleteModel(modelName);
    }

    @Override
    public void saveModel(Model newModel, String newModelName) {
        accessor.putModel(newModelName, newModel);
    }
}
