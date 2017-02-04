package org.matthelliwell.kqueue

import io.kotlintest.specs.StringSpec

class QueueTest : StringSpec(){
    init {
        "should return indented string" {
            val q = Queue("q1", listOf(Queue("q2", listOf())))
            q.toString() shouldBe "Queue(q1)\n--Queue(q2)\n"
        }
    }
}