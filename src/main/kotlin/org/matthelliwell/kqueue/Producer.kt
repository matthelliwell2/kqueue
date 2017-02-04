package org.matthelliwell.kqueue

interface Producer {
    val consumers: List<Consumer>
    fun produce(entity: Entity): Unit
}