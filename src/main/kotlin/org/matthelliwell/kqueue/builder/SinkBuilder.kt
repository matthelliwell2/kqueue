package org.matthelliwell.kqueue.builder

import org.matthelliwell.kqueue.Sink

/**
 * Builder for a sink.
 *
 * @param sinkId Id of the sink. If the id doesn't exist, a new sink is created other an existing sink will be returned.
 */
class SinkBuilder(val sinkId: String) {
    companion object {
        val allBuiltSinks: MutableMap<String, Sink> = mutableMapOf()
    }
    fun build(): Sink {
        return if (allBuiltSinks.containsKey(sinkId)) {
            allBuiltSinks[sinkId]!!
        } else {
            val sink = Sink(sinkId)
            allBuiltSinks[sinkId] = sink
            sink
        }
    }
}
