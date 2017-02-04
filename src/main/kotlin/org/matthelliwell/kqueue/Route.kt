package org.matthelliwell.kqueue

class Route(val id: String, val sources: List<Source>) {
    override fun toString(): String {
        return "Route($id)\n" +  sources.map({it.toString(1)}).joinToString("")
    }
}
