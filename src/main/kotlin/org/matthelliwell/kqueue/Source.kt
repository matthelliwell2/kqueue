package org.matthelliwell.kqueue

class Source(override val id: String, override val consumers: List<Queue>) : Producer, Id {
    override fun produce(entity: Entity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toString(): String {
        return toString(0)
    }

    internal fun toString(indent: Int): String {
        return "${"--".repeat(indent)}Source($id)\n" + consumers.map({it.toString(indent + 1)}).joinToString("")
    }
}