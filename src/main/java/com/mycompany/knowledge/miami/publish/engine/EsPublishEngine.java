package com.mycompany.knowledge.miami.publish.engine;

import lombok.extern.log4j.Log4j;
import lombok.val;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j
public class EsPublishEngine implements AutoCloseable {
    private final RestHighLevelClient esClient;
    private final String indexName;
    private final String type;
    private final int batchSize;
    private final Map<String, String> queue = new HashMap<>();

    public EsPublishEngine(@Value("${miami.publish.elasticsearch.host}") String[] elasticsearchHosts,
                           @Value("${miami.publish.elasticsearch.index}") String indexName,
                           @Value("${miami.publish.elasticsearch.type}") String type,
                           @Value("${miami.publish.elasticsearch.batchSize}") int batchSize) {

        esClient = new RestHighLevelClient(RestClient.builder(Arrays.stream(elasticsearchHosts).map(HttpHost::create).toArray(HttpHost[]::new)));
        this.indexName = indexName;
        this.type = type;
        this.batchSize = batchSize;
    }

    public boolean indexExists() throws IOException {
        val response = esClient.getLowLevelClient().performRequest("HEAD", "/" + indexName);
        int statusCode = response.getStatusLine().getStatusCode();
        return statusCode != 404;
    }

    public void addJsonDocument(String id, String json) throws IOException {
        queue.put(id, json);
        if (queue.size() >= batchSize) {
            flush();
        }
    }

    public void deleteIndex() throws IOException {
        esClient.indices().delete(new DeleteIndexRequest(indexName));
    }

    public void createIndex(Object... mapping) throws IOException {
        val request = new CreateIndexRequest(indexName);
        if (mapping != null && mapping.length > 0) {
            request.mapping(type, mapping);
        }
        esClient.indices().create(request);
    }

    public void flush() throws IOException {
        if (!queue.isEmpty()) {
            val bulkRequest = new BulkRequest();
            queue.forEach((id, json) -> {
                val indexRequest = new IndexRequest(indexName, type, id).source(json, XContentType.JSON);
                bulkRequest.add(indexRequest);
            });
            BulkResponse bulkResponse;
            bulkResponse = esClient.bulk(bulkRequest);
            if (bulkResponse.hasFailures()) {
                throw new RuntimeException(bulkResponse.buildFailureMessage());
            }

            val count = queue.size();
            queue.clear();
            log.info("Imported " + count + " documents.");
        }
    }

    @Override
    public void close() throws Exception {
        esClient.close();
    }
}