package org.matthelliwell.kqueue

class Queue(override val id: String, override val consumers: List<Consumer>) : Id, Consumer, Producer {
    override fun produce(entity: Entity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val entities: MutableList<Entity> = mutableListOf()

    override fun canConsume(entity: Entity): Boolean = true

    override fun consumer(entity: Entity) {
        entities.add(entity)
    }

    override fun toString(): String {
        return toString(0)
    }

    override fun toString(indent: Int): String {
        return "${"--".repeat(indent)}Queue($id)\n" + consumers.map({it.toString(indent + 1)}).joinToString("")
    }



    // TODO allow looking up queues etc by id
    // TODO all queues and sink methods that map to the relevant consumers
}