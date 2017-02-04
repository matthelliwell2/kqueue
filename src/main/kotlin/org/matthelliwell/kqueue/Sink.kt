package org.matthelliwell.kqueue

class Sink(override val id: String) : Id, Consumer {
    // A sinkBuilder will always accept whatever is passed to it
    override fun canConsume(entity: Entity): Boolean = true

    override fun consumer(entity: Entity) {
        println("Sink $id consumed ${entity.id}")
    }

    override fun toString(): String {
        return toString(0)
    }

    override fun toString(indent: Int): String {
        return "${"--".repeat(indent)}Sink($id)\n"
    }
}