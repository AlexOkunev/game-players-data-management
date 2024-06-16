package ru.otus.courses.kafka.util;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminUtils {

  private static final Logger log = LoggerFactory.getLogger(AdminUtils.class);

  public static void doAdminAction(Map<String, Object> adminConfig, AdminClientConsumer action) {
    try (var client = Admin.create(adminConfig)) {
      action.accept(client);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public interface AdminClientConsumer {

    void accept(Admin client) throws Exception;
  }

  public static void recreateTopics(Map<String, Object> adminConfig, String... topics) {
    recreateTopics(adminConfig, 1, 1, topics);
  }

  public static void recreateTopics(Map<String, Object> adminConfig, int numPartitions, int replicationFactor,
                                    String... topics) {
    doAdminAction(adminConfig, admin -> {
      removeAllTopics(admin);
      admin.createTopics(Stream.of(topics)
          .map(it -> new NewTopic(it, numPartitions, (short) replicationFactor))
          .toList());
    });
  }

  private static Collection<String> sync(Admin client) throws Exception {
    var topics = client.listTopics()
        .listings()
        .get()
        .stream()
        .map(TopicListing::name)
        .toList();

    log.info("External topics: {}", topics);

    client.deleteTopics(topics).all().get();

    log.info("All topics deleted");

    return topics;
  }

  public static void removeAllTopics(Admin client) throws Exception {
    var topics = sync(client);

    var newTopics = topics.stream().map(t -> new NewTopic(t, 1, (short) 1)).toList();
    var options = new CreateTopicsOptions().validateOnly(true);

    while (true) {
      try {
        client.createTopics(newTopics, options).all().get();
        break;
      } catch (ExecutionException ex) {
        if (ex.getCause() == null || ex.getCause().getClass() != TopicExistsException.class) {
          throw ex;
        }
        Thread.sleep(100);
      }
    }
  }
}
