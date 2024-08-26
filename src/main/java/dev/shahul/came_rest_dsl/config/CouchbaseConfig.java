package dev.shahul.came_rest_dsl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.env.ClusterEnvironment;

@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("${spring.couchbase.connection-string}")
    private String connectionString;

    @Value("${spring.couchbase.username}")
    private String username;

    @Value("${spring.couchbase.password}")
    private String password;

    @Value("${spring.couchbase.bucket.name}")
    private String bucketName;

    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }

    @Bean
    public Cluster couchbaseCluster() {
        ClusterEnvironment env = ClusterEnvironment.builder().build();
        return Cluster.connect(getConnectionString(), ClusterOptions.clusterOptions(getUserName(), getPassword()).environment(env));
    }

    @Bean
    public Bucket couchbaseBucket(Cluster cluster) {
        return cluster.bucket(getBucketName());
    }
}
