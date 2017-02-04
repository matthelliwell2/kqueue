package org.matthelliwell.kqueue.builder

import org.matthelliwell.kqueue.Queue
import org.matthelliwell.kqueue.Route

/**
 * The starting function for defining a route.
 *
 * @param routeId Id of the route being defined
 * @param init Lambda defining the downstream producers etc
 */
fun route(routeId: String, init: RouteBuilder.() -> Unit): RouteBuilder {
    val routeBuilder = RouteBuilder(routeId)
    routeBuilder.init()
    return routeBuilder
}

/**
 * Builder for a route.
 *
 * @property routeId Id of the route being defined
 * @property allQueueBuilders Map of id to all the queue builders created. This used so we can reference the same queue
 * in different parts of the route.
 * @property allBuiltQueues Map of id to the queues created. This used so we can reference the same queue
 * in different parts of the route.
 * @property sourceBuilders List of builders for the producers
 */
class RouteBuilder(val routeId: String,
                   val allQueueBuilders: MutableMap<String, QueueBuilder> = mutableMapOf(),
                   val allBuiltQueues: MutableMap<String, Queue> = mutableMapOf(),
                   val sourceBuilders: MutableMap<String, SourceBuilder> = mutableMapOf()) {

    fun source(sourceId: String, init: SourceBuilder.() -> Unit): SourceBuilder {
        if (sourceBuilders.containsKey(sourceId)) {
            throw Exception("Cannot create a new source with the id of an existing source. Source $sourceId")
        }

        val sourceBuilder = SourceBuilder(sourceId, allQueueBuilders)
        sourceBuilder.init()
        sourceBuilders[sourceId] = sourceBuilder
        return sourceBuilder
    }

    fun build(): Route {
        return Route(routeId, sourceBuilders.map { it.value.build(allBuiltQueues) })
    }
}
