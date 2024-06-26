package ru.otus.courses.kafka.gameserver.util;

import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.errors.TopicExistsException;

@UtilityClass
@Slf4j
public class AdminUtils {

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
      log.info("Recreating topics. Remove topics");
      removeAndValidateTopics(admin, topics);

      log.info("Create topics");

      admin.createTopics(Stream.of(topics)
          .map(it -> new NewTopic(it, numPartitions, (short) replicationFactor))
          .toList());

      log.info("Topics are created");
    });
  }

  public static void removeAndValidateTopics(Admin client, String... topicsToRemove) throws Exception {
    performTopicsRemoveSync(client, topicsToRemove);

    var newTopics = Arrays.stream(topicsToRemove)
        .map(t -> new NewTopic(t, 1, (short) 1))
        .toList();

    var options = new CreateTopicsOptions().validateOnly(true);

    log.info("Validate removed topics");

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

    log.info("Removed topics validation finished");
  }

  private static void performTopicsRemoveSync(Admin client, String... topicsToRemove) throws Exception {
    var topics = client.listTopics()
        .listings()
        .get()
        .stream()
        .map(TopicListing::name)
        .collect(toSet());

    List<String> existingTopicsToRemove = Arrays.stream(topicsToRemove)
        .filter(topics::contains)
        .toList();

    log.info("External topics: {}", existingTopicsToRemove);

    client.deleteTopics(existingTopicsToRemove).all().get();

    log.info("All topics deleted");
  }

}
