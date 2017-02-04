package org.matthelliwell.kqueue

interface Consumer {
    fun consumer(entity: Entity) : Unit
    fun canConsume(entity: Entity) : Boolean
    fun toString(indent: Int) : String
}