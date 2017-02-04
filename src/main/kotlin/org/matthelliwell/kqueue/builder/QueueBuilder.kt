package org.matthelliwell.kqueue.builder

import org.matthelliwell.kqueue.Queue

/**
 * Builder for a queue. Note that you cannot have both a sink and other queues hanging off this queue.
 *
 * @param queueId Id of the queue that is being built
 * @param queueBuilders Builds for all the queues to which this queue can send entities
 * @param sinkBuilder Build for this sink to which this queue will send messages. If null then no sink will be created.
 */
class QueueBuilder(val queueId: String,
                   val allQueueBuilders: MutableMap<String, QueueBuilder>,
                   val queueBuilders: MutableList<QueueBuilder> = mutableListOf(),
                   var sinkBuilder: SinkBuilder? = null) {

    /**
     * Defines a new queue that feeds into a list of other queues.
     *
     * @param queueId Id of the queue being created
     * @param init Lambda defining the downstream queues
     */
    fun queue(queueId: String, init: QueueBuilder.() -> Unit): QueueBuilder {
        if (sinkBuilder != null) {
            throw Exception("Cannot add a queue to a queue that is already linked to a sink. Queue $queueId")
        }

        val queueBuilder = QueueBuilder(queueId, allQueueBuilders)
        queueBuilder.init()
        queueBuilders.add(queueBuilder)

        if (allQueueBuilders.containsKey(queueId)) {
            throw Exception("Cannot create a new queue with the id of an existing queue. Queue $queueId")
        }

        allQueueBuilders.put(queueId, queueBuilder)
        return queueBuilder
    }

    /**
     * References an existing queue so that it can be used in more than one place in the route. The queue must have
     * already been defined further up the route.
     *
     * @param queueId Id of the existing queue.
     */
    fun queue(queueId: String) {
        if (sinkBuilder != null) {
            throw Exception("Cannot add a queue to a queue that is already linked to a sink. Queue routeId $queueId")
        }

        val existingQueueBuilder = allQueueBuilders[queueId] ?: throw Exception("Unable to find existing queue with routeId $queueId")
        queueBuilders.add(existingQueueBuilder)
    }

    /**
     * Defines a sink feeding from this queue.
     *
     * @param sinkId Id of the sink being created
     */
    fun sink(sinkId: String) {
        if (queueBuilders.size != 0) {
            throw Exception("Cannot add a sink to a queue that is already linked to a queue. Queue routeId $sinkId")
        }

        sinkBuilder = SinkBuilder(sinkId)
    }


    /**
     * Builds the queue and its downstream queues and sinks.
     */
    fun build(allBuiltQueues: MutableMap<String, Queue>): Queue {
        return if (allBuiltQueues.containsKey(queueId)) {
            allBuiltQueues[queueId]!!
        } else if (sinkBuilder != null) {
            val queue = Queue(queueId, listOf(sinkBuilder!!.build()))
            allBuiltQueues[queueId] = queue
            queue
        } else {
            val queue = Queue(queueId, queueBuilders.map { it.build(allBuiltQueues)})
            allBuiltQueues[queueId] = queue
            queue
        }
    }
}
