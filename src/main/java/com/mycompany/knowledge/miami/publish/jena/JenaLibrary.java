package com.mycompany.knowledge.miami.publish.jena;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface JenaLibrary {
    Model getModel(String modelName);
    void removeModel(String modelName);
    void closeDB();
    String getModelName();
    // read
    List<Statement> getStatements(Model model);
    Iterator<Statement> getStatementsByEntityType(Model model, String type);
    Iterator<Statement> getStatementsById(Model model, String id);
    Iterator<Statement> getStatementsBySP(Model model, Resource resource, String property);
    Iterator<Statement> getStatementsByBatchSP(Model model, HashSet<String> subjects, String property_str);
    Map<String, List<String>> getSOMapByBatchSP(Model model, HashSet<String> subjects, String property_str);
    Map<String, List<String>> getOSMapByBatchSP(Model model, HashSet<String> subjects, String property_str);
    Iterator<Statement> getStatementsByPOValue(Model model, String property, String value);
    Iterator<Statement> getStatementsByPO(Model model, String property, Resource object);
    Iterator<Statement> getStatementsByBatchPO(Model model, String property, HashSet<String> objects);
    Map<String, List<String>> getSOMapByBatchPO(Model model, String property, HashSet<String> objects);
    Map<String, List<String>> getOSMapByBatchPO(Model model, String property, HashSet<String> objects);

    Iterator<Statement> getStatementsBySourceAndType(Model model, String source, String type);

    // deprecated
    Iterator<Statement> getStatementsBySubjectSubStr(Model model, String substr);

    List<String> getStringValueBySP(Model model, Resource resource, String property);
    List<String> getStringValuesByBatchSP(Model model, HashSet<String> subjects, String property);
    List<String> getObjectNamesBySP(Model model, Resource resource, String property);


    void saveModel(Model newModel, String newModelName);

    Model deepCopyModel(Model model);
}
