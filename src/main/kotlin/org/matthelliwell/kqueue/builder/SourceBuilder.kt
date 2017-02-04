package org.matthelliwell.kqueue.builder

import org.matthelliwell.kqueue.Queue
import org.matthelliwell.kqueue.Source

/**
 * Builder for producers.
 *
 * @property sourceId Id of the producer to be created.
 * @property queueBuilders List of builders for the downstream queues.
 * @property allQueueBuilders Map of id to all the queue builders created. This used so we can reference the same queue
 * in different parts of the route.
 */
class SourceBuilder(val sourceId: String,
                    val allQueueBuilders: MutableMap<String, QueueBuilder>,
                    val queueBuilders: MutableList<QueueBuilder> = mutableListOf()) {

    fun queue(queueId: String, init: QueueBuilder.() -> Unit): QueueBuilder {
        val queueBuilder = QueueBuilder(queueId, allQueueBuilders)
        queueBuilder.init()
        queueBuilders.add(queueBuilder)

        if (allQueueBuilders.containsKey(queueId)) {
            throw Exception("Cannot create a new queue with the id of an existing queue. Queue $queueId")
        }

        allQueueBuilders.put(queueId, queueBuilder)
        return queueBuilder
    }

    fun queue(queueId: String) {
        val existingQueueBuilder = allQueueBuilders[queueId] ?: throw Exception("Unable to find existing queue with routeId $queueId")
        queueBuilders.add(existingQueueBuilder)
    }

    fun build(allBuiltQueues: MutableMap<String, Queue>): Source {
        return Source(sourceId, queueBuilders.map { it.build(allBuiltQueues) })
    }
}
